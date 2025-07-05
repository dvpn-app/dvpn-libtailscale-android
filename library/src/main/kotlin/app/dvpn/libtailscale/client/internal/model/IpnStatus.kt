package app.dvpn.libtailscale.client.internal.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class IpnStatus(
    @SerialName("Version")
    val version: String,
    @SerialName("TUN")
    val tun: Boolean,
    @SerialName("BackendState")
    val backendState: String,
    @SerialName("AuthURL")
    val authURL: String,
    @SerialName("TailscaleIPs")
    val tailscaleIPs: List<String>? = null,
    @SerialName("Self")
    val self: PeerStatus? = null,
    @SerialName("ExitNodeStatus")
    val exitNodeStatus: ExitNodeStatus? = null,
    @SerialName("Health")
    val health: List<String>? = null,
    @SerialName("CurrentTailnet")
    val currentTailnet: TailnetStatus? = null,
    @SerialName("CertDomains")
    val certDomains: List<String>? = null,
    @SerialName("Peer")
    val peer: Map<String, PeerStatus>? = null,
)

@Serializable
internal data class PeerStatus(
    @SerialName("ID")
    val id: String,
    @SerialName("HostName")
    val hostName: String,
    @SerialName("DNSName")
    val dnsName: String,
    @SerialName("TailscaleIPs")
    val tailscaleIPs: List<String>? = null,
    @SerialName("Tags")
    val tags: List<String>? = null,
    @SerialName("PrimaryRoutes")
    val primaryRoutes: List<String>? = null,
    @SerialName("Strings")
    val strings: List<String>? = null,
    @SerialName("CurString")
    val curString: String? = null,
    @SerialName("Relay")
    val relay: String? = null,
    @SerialName("Online")
    val online: Boolean,
    @SerialName("ExitNode")
    val exitNode: Boolean,
    @SerialName("ExitNodeOption")
    val exitNodeOption: Boolean,
    @SerialName("Active")
    val active: Boolean,
    @SerialName("PeerAPIURL")
    val peerAPIURL: List<String>? = null,
    @SerialName("Capabilities")
    val capabilities: List<String>? = null,
    @SerialName("SSH_HostKeys")
    val sshHostKeys: List<String>? = null,
    @SerialName("ShareeNode")
    val shareeNode: Boolean? = null,
    @SerialName("Expired")
    val expired: Boolean? = null,
    @SerialName("Location")
    val location: Location? = null,
) {
    fun computedName(status: IpnStatus): String {
        val name = dnsName
        val suffix = status.currentTailnet?.magicDNSSuffix

        suffix ?: return name

        if (!(name.endsWith(".$suffix."))) {
            return name
        }

        return name.dropLast(suffix.count() + 2)
    }
}

@Serializable
internal data class ExitNodeStatus(
    @SerialName("ID")
    val id: String,
    @SerialName("Online")
    val online: Boolean,
    @SerialName("TailscaleIPs")
    val tailscaleIPs: List<String>? = null,
)

@Serializable
internal data class TailnetStatus(
    @SerialName("Name")
    val name: String,
    @SerialName("MagicDNSSuffix")
    val magicDNSSuffix: String,
    @SerialName("MagicDNSEnabled")
    val magicDNSEnabled: Boolean,
)
