package app.dvpn.libtailscale.internal.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.net.URL

@Serializable
internal data class LoginProfile(
    @SerialName("ID")
    val id: String,
    @SerialName("Name")
    val name: String,
    @SerialName("Key")
    val key: String,
    @SerialName("UserProfile")
    val userProfile: UserProfile,
    @SerialName("NetworkProfile")
    val networkProfile: NetworkProfile? = null,
    @SerialName("LocalUserID")
    val localUserID: String,
    @SerialName("ControlURL")
    var controlURL: String? = null,
)

@Serializable
internal data class UserProfile(
    @SerialName("ID")
    val id: Long,
    @SerialName("DisplayName")
    val displayName: String,
    @SerialName("LoginName")
    val loginName: String,
    @SerialName("ProfilePicURL")
    val profilePicURL: String? = null,
)

@Serializable
internal data class NetworkProfile(
    @SerialName("MagicDNSName")
    var magicDNSName: String? = null,
    @SerialName("DomainName")
    var domainName: String? = null
)
