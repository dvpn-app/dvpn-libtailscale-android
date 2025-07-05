package app.dvpn.libtailscale.client.internal.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class TailscalePreferences(
    @SerialName("ControlURL")
    val controlURL: String = "",
    @SerialName("RouteAll")
    val routeAll: Boolean = false,
    @SerialName("AllowsSingleHosts")
    val allowsSingleHosts: Boolean = false,
    @SerialName("CorpDNS")
    val corpDNS: Boolean = false,
    @SerialName("WantRunning")
    val wantRunning: Boolean = false,
    @SerialName("LoggedOut")
    val loggedOut: Boolean = false,
    @SerialName("ShieldsUp")
    val shieldsUp: Boolean = false,
    @SerialName("AdvertiseRoutes")
    val advertiseRoutes: List<String>? = null,
    @SerialName("AdvertiseTags")
    val advertiseTags: List<String>? = null,
    @SerialName("ExitNodeID")
    val exitNodeID: String? = null,
    @SerialName("ExitNodeAllowLANAccess")
    val exitNodeAllowLANAccess: Boolean = false,
    @SerialName("Config")
    val config: Persist? = null,
    @SerialName("ForceDaemon")
    val forceDaemon: Boolean = false,
    @SerialName("HostName")
    val hostName: String = "",
    @SerialName("AutoUpdate")
    val autoUpdate: AutoUpdatePrefs? = AutoUpdatePrefs(true, true),
    @SerialName("InternalExitNodePrior")
    val internalExitNodePrior: String? = null,
) {
    val selectedExitNodeID: String?
        get() = internalExitNodePrior.takeIf { !it.isNullOrEmpty() }

    val activeExitNodeID: String?
        get() = exitNodeID.takeIf { !it.isNullOrEmpty() }
}
