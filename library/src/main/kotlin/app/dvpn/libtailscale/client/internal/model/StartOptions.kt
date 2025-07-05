package app.dvpn.libtailscale.client.internal.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class StartOptions(
    @SerialName("FrontendLogID")
    val frontendLogID: String? = null,
    @SerialName("UpdatePrefs")
    val updatePrefs: TailscalePreferences? = null,
    @SerialName("AuthKey")
    val authKey: String? = null,
)