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
    suspend fun start(options: StartOptions) {
        val body = json.encodeToString(options).toByteArray()
        adapter.post(Endpoint.Start, body)
    }

    suspend fun loginInteractive() {
        adapter.post(Endpoint.LoginInteractive)
    }

    suspend fun getStatus(): IpnStatus {
        return adapter.get(Endpoint.Status).decode()
    }

    suspend fun getSettings(): TailscaleSettings {
        return adapter.get(Endpoint.Preferences).decode()
    }

    suspend fun editSettings(patch: TailscaleSettings.Companion.() -> String): TailscaleSettings {
        val data = patch(TailscaleSettings.Companion).toByteArray()
        return adapter.patch(Endpoint.Preferences, data).decode()
    }

    suspend fun setUseExitNode(enabled: Boolean): TailscaleSettings {
        val endpoint = if (enabled) Endpoint.EnableExitNode else Endpoint.DisableExitNode
        return adapter.post(endpoint).decode()
    }

    suspend fun getProfile(): LoginProfile {
        return adapter.get(Endpoint.CurrentProfile).decode()
    }

    suspend fun getProfiles(): List<LoginProfile> {
        return adapter.get(Endpoint.Profiles).decode()
    }

    suspend fun logout() {
        adapter.post(Endpoint.Logout)
    }

    private inline fun <reified T : Any> LocalAPIResponse.decode(): T {
        val data = bodyBytes() ?: ByteArray(0)
        return json.decodeFromStream<T>(data.inputStream())
    }
}
