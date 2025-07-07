@file:OptIn(ExperimentalSerializationApi::class)

package app.dvpn.libtailscale.internal.api

import app.dvpn.libtailscale.internal.api.adapter.TailscaleApiAdapter
import app.dvpn.libtailscale.internal.api.adapter.TailscaleApiAdapter.Endpoint
import app.dvpn.libtailscale.internal.api.model.IpnStatus
import app.dvpn.libtailscale.internal.api.model.LoginProfile
import app.dvpn.libtailscale.internal.api.model.StartOptions
import app.dvpn.libtailscale.internal.api.model.TailscaleSettings
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import libtailscale.LocalAPIResponse

internal class TailscaleApi(
    private val json: Json,
    private val adapter: TailscaleApiAdapter,
) {

    suspend fun start(options: StartOptions): Result<Unit> {
        return runCatching { json.encodeToString(options).toByteArray() }
            .mapCatching { body -> adapter.post(Endpoint.Start, body) }
    }

    suspend fun loginInteractive(): Result<Unit> {
        return runCatching { adapter.post(Endpoint.LoginInteractive) }
    }

    suspend fun getStatus(): Result<IpnStatus> {
        return runCatching { adapter.get(Endpoint.Status) }
            .mapCatching { response -> response.decode() }
    }

    suspend fun getSettings(): Result<TailscaleSettings> {
        return runCatching { adapter.get(Endpoint.Preferences) }
            .mapCatching { response -> response.decode() }
    }

    suspend fun editSettings(patch: TailscaleSettings.Companion.() -> String): Result<TailscaleSettings> {
        return runCatching { patch(TailscaleSettings.Companion).toByteArray() }
            .mapCatching { data -> adapter.patch(Endpoint.Preferences, data) }
            .mapCatching { response -> response.decode() }
    }

    suspend fun setUseExitNode(enabled: Boolean): Result<TailscaleSettings> {
        return runCatching {
            val endpoint = if (enabled) Endpoint.EnableExitNode else Endpoint.DisableExitNode
            adapter.post(endpoint)
        }.mapCatching { response ->
            response.decode()
        }
    }

    suspend fun getProfile(): Result<LoginProfile> {
        return runCatching { adapter.get(Endpoint.CurrentProfile) }
            .mapCatching { response -> response.decode() }
    }

    suspend fun getProfiles(): Result<List<LoginProfile>> {
        return runCatching { adapter.get(Endpoint.Profiles) }
            .mapCatching { response -> response.decode() }
    }

    suspend fun logout(): Result<Unit> {
        return runCatching { adapter.post(Endpoint.Logout) }
    }

    private inline fun <reified T : Any> LocalAPIResponse.decode(): T {
        val data = bodyBytes() ?: ByteArray(0)
        return json.decodeFromStream<T>(data.inputStream())
    }
}
