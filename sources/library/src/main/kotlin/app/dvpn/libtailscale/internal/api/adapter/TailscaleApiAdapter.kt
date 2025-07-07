@file:Suppress("ConstPropertyName")

package app.dvpn.libtailscale.internal.api.adapter

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import libtailscale.LocalAPIResponse
import kotlin.coroutines.CoroutineContext

internal class TailscaleApiAdapter(
    private val api: libtailscale.Application,
    private val coroutineContext: CoroutineContext = Dispatchers.IO,
) {

    suspend fun get(
        endpoint: Endpoint,
        body: ByteArray? = null,
    ): LocalAPIResponse {
        return request(endpoint, Method.Get, body)
    }

    suspend fun post(
        endpoint: Endpoint,
        body: ByteArray? = null,
    ): LocalAPIResponse {
        return request(endpoint, Method.Post, body)
    }

    suspend fun patch(
        endpoint: Endpoint,
        body: ByteArray?,
    ): LocalAPIResponse {
        return request(endpoint, Method.Patch, body)
    }

    private suspend fun request(
        endpoint: Endpoint,
        method: Method,
        body: ByteArray?,
    ): LocalAPIResponse {
        return withContext(coroutineContext) {
            api.callLocalAPI(
                RequestTimeoutMillis,
                method.value,
                "$BaseUrl/${endpoint.path}",
                body?.let(::InputStreamAdapter),
            )
        }
    }

    enum class Endpoint(val path: String) {
        Preferences("prefs"),
        Start("start"),
        LoginInteractive("login-interactive"),
        Status("status"),
        CurrentProfile("profiles/current"),
        Profiles("profiles/"),
        Logout("logout"),
        EnableExitNode("set-use-exit-node-enabled?enabled=true"),
        DisableExitNode("set-use-exit-node-enabled?enabled=false"),
    }

    private enum class Method(val value: String) {
        Get("GET"),
        Post("POST"),
        Patch("PATCH"),
    }

    private companion object {
        private const val BaseUrl = "/localapi/v0"
        private const val RequestTimeoutMillis = 10_000L
    }
}
