package app.dvpn.libtailscale.internal.context

import android.os.Build
import app.dvpn.libtailscale.internal.context.preferences.TailscalePreferences
import kotlinx.serialization.json.Json
import libtailscale.AppContext
import java.net.NetworkInterface
import java.util.Collections
import java.util.Locale
import androidx.core.content.edit

internal class TailscaleContext(
    private val sharedPreferences: TailscalePreferences,
    private val dnsConfigProvider: () -> String,
    private val logger: (String, String) -> Unit,
) : AppContext {

    private val locale = Locale.getDefault()

    override fun encryptToPref(key: String, value: String?) {
        sharedPreferences.edit { putString(key, value) }
    }

    override fun decryptFromPref(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    override fun getInstallSource(): String {
        return "solana-dappstore"
    }

    override fun getInterfacesAsString(): String {
        val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())

        val sb = StringBuilder()
        for (nif in interfaces) {
            try {
                sb.append(
                    String.format(
                        Locale.ROOT,
                        "%s %d %d %b %b %b %b %b |",
                        nif.name,
                        nif.index,
                        nif.mtu,
                        nif.isUp,
                        nif.supportsMulticast(),
                        nif.isLoopback,
                        nif.isPointToPoint,
                        nif.supportsMulticast()))

                for (ia in nif.interfaceAddresses) {
                    val parts = ia.toString().split("/", limit = 0)
                    if (parts.size > 1) {
                        sb.append(String.format(Locale.ROOT, "%s/%d ", parts[1], ia.networkPrefixLength))
                    }
                }
            } catch (e: Exception) {
                continue
            }
            sb.append("\n")
        }

        return sb.toString()
    }

    override fun getModelName(): String {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        val modelSuffix = model
            .lowercase(locale)
            .indexOf(manufacturer.lowercase(locale))
            .takeIf { it != -1 }
            ?.let { model.substring(it + manufacturer.length).trim() }
            ?: model
        return "$manufacturer $modelSuffix"
    }

    override fun getOSVersion(): String {
        return Build.VERSION.RELEASE
    }

    override fun getPlatformDNSConfig(): String {
        return dnsConfigProvider()
    }

    override fun getStateStoreKeysJSON(): String {
        return sharedPreferences.getStateStorePreferencesJson()
    }

    override fun getSyspolicyBooleanValue(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    override fun getSyspolicyStringArrayJSONValue(key: String): String {
        val value = sharedPreferences.getStringSet(key, emptySet())
        return Json.encodeToString(value)
    }

    override fun getSyspolicyStringValue(key: String): String {
        return sharedPreferences.getString(key, null).orEmpty()
    }

    override fun isChromeOS(): Boolean {
        return false
    }

    override fun log(tag: String, message: String) {
        logger(tag, message)
    }

    override fun shouldUseGoogleDNSFallback(): Boolean {
        return true
    }
}
//
//class TailscaleAppContext(
//    json: Json,
//    context: Context,
//    coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main)
//) : AppContext {
//
//    private val locale = Locale.getDefault()
//    private val networkInfoObserver: NetworkInfoObserver = NetworkInfoObserverImpl(context)
//    private val tailscalePreferences = TailscalePreferences(
//        json,
//        context.getSharedPreferences("secret_shared_prefs", Context.MODE_PRIVATE),
//    )
//
//    init {
//        networkInfoObserver.networkInfo
//            .filterNotNull()
//            .onEach { info ->
//                Libtailscale.onDNSConfigChanged(info.dnsConfig)
//            }
//            .launchIn(coroutineScope)
//    }
//
//    override fun decryptFromPref(key: String): String? {
//        return tailscalePreferences.getString(key, null)
//    }
//
//    override fun encryptToPref(key: String, value: String?) {
//        tailscalePreferences.edit { putString(key, value) }
//    }
//
//    override fun getInstallSource(): String {
//        return "solana-dappstore"
//    }
//
//    override fun getInterfacesAsString(): String {
//        val interfaces = Collections.list(NetworkInterface.getNetworkInterfaces())
//
//        val sb = StringBuilder()
//        for (nif in interfaces) {
//            try {
//                sb.append(
//                    String.format(
//                        Locale.ROOT,
//                        "%s %d %d %b %b %b %b %b |",
//                        nif.name,
//                        nif.index,
//                        nif.mtu,
//                        nif.isUp,
//                        nif.supportsMulticast(),
//                        nif.isLoopback,
//                        nif.isPointToPoint,
//                        nif.supportsMulticast()))
//
//                for (ia in nif.interfaceAddresses) {
//                    val parts = ia.toString().split("/", limit = 0)
//                    if (parts.size > 1) {
//                        sb.append(String.format(Locale.ROOT, "%s/%d ", parts[1], ia.networkPrefixLength))
//                    }
//                }
//            } catch (e: Exception) {
//                continue
//            }
//            sb.append("\n")
//        }
//
//        return sb.toString()
//    }
//
//    override fun getModelName(): String {
//        val manufacturer = Build.MANUFACTURER
//        val model = Build.MODEL
//        val modelSuffix = model
//            .lowercase(locale)
//            .indexOf(manufacturer.lowercase(locale))
//            .takeIf { it != -1 }
//            ?.let { model.substring(it + manufacturer.length).trim() }
//            ?: model
//        return "$manufacturer $modelSuffix"
//    }
//
//    override fun getOSVersion(): String {
//        return Build.VERSION.RELEASE
//    }
//
//    override fun getPlatformDNSConfig(): String {
//        return networkInfoObserver.dnsConfig
//    }
//
//    override fun getStateStoreKeysJSON(): String {
//        return tailscalePreferences.getStateStorePreferencesJson()
//    }
//
//    override fun getSyspolicyBooleanValue(key: String): Boolean {
//        return tailscalePreferences.getBoolean(key, false)
//    }
//
//    override fun getSyspolicyStringArrayJSONValue(key: String): String {
//        val value = tailscalePreferences.getStringSet(key, emptySet())
//        return Json.encodeToString(value)
//    }
//
//    override fun getSyspolicyStringValue(key: String): String {
//        return tailscalePreferences.getString(key, null).orEmpty()
//    }
//
//    override fun isChromeOS(): Boolean {
//        return false
//    }
//
//    override fun log(tag: String, message: String) {
//        Log.d(tag, message)
//    }
//
//    override fun shouldUseGoogleDNSFallback(): Boolean {
//        return true
//    }
//}