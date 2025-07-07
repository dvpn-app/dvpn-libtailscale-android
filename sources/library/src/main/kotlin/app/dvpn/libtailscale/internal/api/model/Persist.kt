@file:Suppress("PropertyName")

package app.dvpn.libtailscale.internal.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class Persist(
    @SerialName("PrivateMachineKey")
    val privateMachineKey: String = "privkey:0000000000000000000000000000000000000000000000000000000000000000",
    @SerialName("PrivateNodeKey")
    val privateNodeKey: String = "privkey:0000000000000000000000000000000000000000000000000000000000000000",
    @SerialName("OldPrivateNodeKey")
    val oldPrivateNodeKey: String = "privkey:0000000000000000000000000000000000000000000000000000000000000000",
    @SerialName("Provider")
    val provider: String = "",
)
