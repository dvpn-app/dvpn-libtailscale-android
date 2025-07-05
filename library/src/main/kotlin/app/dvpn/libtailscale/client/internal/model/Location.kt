package app.dvpn.libtailscale.client.internal.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class Location(
    @SerialName("Country")
    val country: String? = null,
    @SerialName("CountryCode")
    val countryCode: String? = null,
    @SerialName("City")
    val city: String? = null,
    @SerialName("CityCode")
    val cityCode: String? = null,
    @SerialName("Priority")
    val priority: Int? = null
)