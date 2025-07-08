package app.dvpn.libtailscale.internal.context.network

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.LinkProperties
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import java.util.concurrent.ConcurrentHashMap

internal class DNSConfigProvider(
    context: Context,
    coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main),
) {
    private val dns = MutableStateFlow<String?>(null)
    private val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    init {
        observeNetworkState()
            .mapLatest { network -> network?.dnsConfig }
            .distinctUntilChanged()
            .onEach(dns::emit)
            .launchIn(coroutineScope)
    }

    fun provide(): String {
        return dns.value.orEmpty()
    }

    @SuppressLint("MissingPermission")
    private fun observeNetworkState(): Flow<NetworkState?> {
        return callbackFlow {
            val activeNetworks = ConcurrentHashMap<Network, NetworkState>()

            fun selectBestNetwork(): NetworkState? {
                return activeNetworks.entries
                    .filter { (_, info) -> info.isInternet && info.isNotVpn }
                    .sortedWith(
                        compareByDescending<Map.Entry<Network, NetworkState>> { it.value.hasDnsServers }
                            .thenByDescending { it.value.isNotMetered }
                    )
                    .firstOrNull()?.value
            }

            val networkCallback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    activeNetworks[network] = NetworkState(
                        NetworkCapabilities(),
                        LinkProperties()
                    )
                    trySend(selectBestNetwork())
                }

                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {
                    activeNetworks.compute(network) { _, info ->
                        info?.copy(capabilities = networkCapabilities) ?: NetworkState(
                            networkCapabilities,
                            info?.linkProperties ?: LinkProperties()
                        )
                    }
                    trySend(selectBestNetwork())
                }

                override fun onLinkPropertiesChanged(
                    network: Network,
                    linkProperties: LinkProperties
                ) {
                    activeNetworks.compute(network) { _, info ->
                        info?.copy(linkProperties = linkProperties) ?: NetworkState(
                            info?.capabilities ?: NetworkCapabilities(),
                            linkProperties
                        )
                    }
                    trySend(selectBestNetwork())
                }

                override fun onLost(network: Network) {
                    activeNetworks.remove(network)
                    trySend(selectBestNetwork())
                }
            }

            val networkRequest = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .addCapability(NetworkCapabilities.NET_CAPABILITY_NOT_VPN)
                .build()

            connectivityManager.registerNetworkCallback(networkRequest, networkCallback)

            val currentNetwork = connectivityManager.activeNetwork
            if (currentNetwork != null) {
                val capabilities = connectivityManager.getNetworkCapabilities(currentNetwork)
                val linkProperties = connectivityManager.getLinkProperties(currentNetwork)
                if (capabilities != null && linkProperties != null) {
                    activeNetworks[currentNetwork] = NetworkState(capabilities, linkProperties)
                    trySend(selectBestNetwork())
                }
            } else {
                trySend(null)
            }

            awaitClose {
                connectivityManager.unregisterNetworkCallback(networkCallback)
            }
        }
    }
}

private data class NetworkState(
    val capabilities: NetworkCapabilities,
    val linkProperties: LinkProperties,
) {
    val isInternet: Boolean
        get() = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)

    val isNotVpn: Boolean
        get() = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_VPN)

    val isNotMetered: Boolean
        get() = capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)

    val hasDnsServers: Boolean
        get() = linkProperties.dnsServers.isNotEmpty()

    val dnsConfig: String
        get() = buildString {
            linkProperties.dnsServers.joinToString(" ") { it.hostAddress.orEmpty() }
                .takeIf { it.isNotBlank() }
                ?.let { append(it) }

            linkProperties.domains?.takeIf { it.isNotBlank() }?.let {
                appendLine()
                append(it)
            }
        }
}
