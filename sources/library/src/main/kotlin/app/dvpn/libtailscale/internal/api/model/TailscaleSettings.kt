package app.dvpn.libtailscale.internal.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
internal data class TailscaleSettings(
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
    val loggedOut: Boolean = true,
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
    internal companion object {
        internal fun Patch(
            controlURL: String? = null,
            routeAll: Boolean? = null,
            allowsSingleHosts: Boolean? = null,
            corpDNS: Boolean? = null,
            wantRunning: Boolean? = null,
            loggedOut: Boolean? = null,
            shieldsUp: Boolean? = null,
            advertiseRoutes: List<String>? = null,
            advertiseTags: List<String>? = null,
            exitNodeID: String? = null,
            exitNodeAllowLANAccess: Boolean? = null,
            config: Persist? = null,
            forceDaemon: Boolean? = null,
            hostName: String? = null,
            autoUpdate: AutoUpdatePrefs? = null,
            internalExitNodePrior: String? = null,
        ): String {
            return buildMap {
                controlURL?.let {
                    put("ControlURL", it)
                    put("ControlURLSet", "true")
                }
                routeAll?.let {
                    put("RouteAll", it)
                    put("RouteAllSet", "true")
                }
                allowsSingleHosts?.let {
                    put("AllowsSingleHosts", it)
                    put("AllowsSingleHostsSet", "true")
                }
                corpDNS?.let {
                    put("CorpDNS", it)
                    put("CorpDNSSet", "true")
                }
                wantRunning?.let {
                    put("WantRunning", it)
                    put("WantRunningSet", "true")
                }
                loggedOut?.let {
                    put("LoggedOut", it)
                    put("LoggedOutSet", "true")
                }
                shieldsUp?.let {
                    put("ShieldsUp", it)
                    put("ShieldsUpSet", "true")
                }
                advertiseRoutes?.let {
                    put("AdvertiseRoutes", it)
                    put("AdvertiseRoutesSet", "true")
                }
                advertiseTags?.let {
                    put("AdvertiseTags", it)
                    put("AdvertiseTagsSet", "true")
                }
                exitNodeID?.let {
                    put("ExitNodeID", it)
                    put("ExitNodeIDSet", "true")
                }
                exitNodeAllowLANAccess?.let {
                    put("ExitNodeAllowLANAccess", it)
                    put("ExitNodeAllowLANAccessSet", "true")
                }
                config?.let {
                    put("Config", it)
                    put("ConfigSet", "true")
                }
                forceDaemon?.let {
                    put("ForceDaemon", it)
                    put("ForceDaemonSet", "true")
                }
                hostName?.let {
                    put("HostName", it)
                    put("HostNameSet", "true")
                }
                autoUpdate?.let {
                    put("AutoUpdate", it)
                    put("AutoUpdateSet", "true")
                }
                internalExitNodePrior?.let {
                    put("InternalExitNodePrior", it)
                    put("InternalExitNodePriorSet", "true")
                }
            }.let(Json::encodeToString)
        }
    }
}
