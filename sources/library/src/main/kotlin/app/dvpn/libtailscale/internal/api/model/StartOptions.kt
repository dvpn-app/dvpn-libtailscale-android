package app.dvpn.libtailscale.internal.api.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class StartOptions(
    @SerialName("FrontendLogID")
    val frontendLogID: String? = null,
    @SerialName("UpdatePrefs")
    val updatePrefs: TailscaleSettings? = null,
    @SerialName("AuthKey")
    val authKey: String? = null,
)