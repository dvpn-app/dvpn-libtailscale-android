@file:OptIn(ExperimentalSerializationApi::class)

package app.dvpn.libtailscale.client.internal

import app.dvpn.libtailscale.client.internal.adapter.TailscaleApiAdapter
import app.dvpn.libtailscale.client.internal.adapter.TailscaleApiAdapter.Endpoint
import app.dvpn.libtailscale.client.internal.model.IpnStatus
import app.dvpn.libtailscale.client.internal.model.StartOptions
import app.dvpn.libtailscale.client.internal.model.TailscalePreferences
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import libtailscale.LocalAPIResponse

internal class TailscaleApi(
    private val adapter: TailscaleApiAdapter,
    private val json: Json,
) {

    suspend fun start(options: StartOptions) {
        val body = json.encodeToString(options).toByteArray()
        adapter.post(Endpoint.Start, body)
    }

    suspend fun getStatus(): IpnStatus {
        val response = adapter.get(Endpoint.Status)
        return response.decode<IpnStatus>()
    }

    suspend fun getPreferences(): TailscalePreferences {
        val response = adapter.get(Endpoint.Preferences)
        return response.decode<TailscalePreferences>()
    }

    suspend fun editPreferences(
        update: TailscalePreferences.() -> TailscalePreferences
    ): TailscalePreferences {
        val data = json.encodeToString(getPreferences().update()).toByteArray()
        val response = adapter.patch(Endpoint.Preferences, data)
        return response.decode<TailscalePreferences>()
    }

    suspend fun setUseExitNode(enabled: Boolean): TailscalePreferences {
        val endpoint = if (enabled) {
            Endpoint.EnableExitNode
        } else {
            Endpoint.DisableExitNode
        }
        val response = adapter.post(endpoint)

        return response.decode<TailscalePreferences>()
    }

    private inline fun <reified T : Any> LocalAPIResponse.decode(): T {
        val data = bodyBytes() ?: ByteArray(0)
        return json.decodeFromStream<T>(data.inputStream())
    }
}
