@file:OptIn(ExperimentalSerializationApi::class)

package app.dvpn.libtailscale.client.internal

import app.dvpn.libtailscale.client.internal.adapter.TailscaleApiAdapter
import app.dvpn.libtailscale.client.internal.adapter.TailscaleApiAdapter.Endpoint
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

    suspend fun getPreferences(): TailscalePreferences {
        return runCatching { adapter.get(Endpoint.Preferences) }
            .mapCatching { response -> response.decode<TailscalePreferences>() }
            .getOrThrow()
    }

    private inline fun <reified T : Any> LocalAPIResponse.decode(): T {
        val data = bodyBytes() ?: ByteArray(0)
        return json.decodeFromStream<T>(data.inputStream())
    }
}