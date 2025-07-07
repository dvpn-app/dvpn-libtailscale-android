package app.dvpn.libtailscale.internal.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class AutoUpdatePrefs(
    @SerialName("Check")
    val check: Boolean? = null,
    @SerialName("Apply")
    val apply: Boolean? = null,
)