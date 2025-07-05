@file:Suppress("ConstPropertyName")

package app.dvpn.libtailscale.client.internal.adapter

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import libtailscale.LocalAPIResponse
import kotlin.coroutines.CoroutineContext

internal class TailscaleApiAdapter(
    private val api: libtailscale.Application,
    private val json: Json,
    private val coroutineContext: CoroutineContext = Dispatchers.IO,
) {

    suspend fun get(
        endpoint: Endpoint,
        body: Any? = null,
    ): LocalAPIResponse {
        return request(endpoint, Method.Get, body)
    }

    suspend fun post(
        endpoint: Endpoint,
        body: Any? = null,
    ): LocalAPIResponse {
        return request(endpoint, Method.Post, body)
    }

    suspend fun patch(
        endpoint: Endpoint,
        body: Any?,
    ): LocalAPIResponse {
        return request(endpoint, Method.Patch, body)
    }

    private suspend fun request(
        endpoint: Endpoint,
        method: Method,
        body: Any?,
    ): LocalAPIResponse {
        return withContext(coroutineContext) {
            val bodyInputStream = body
                ?.let(json::encodeToString)
                ?.toByteArray()
                ?.let(::InputStreamAdapter)

            api.callLocalAPI(
                RequestTimeoutMillis,
                method.value,
                "$BaseUrl/${endpoint.path}",
                bodyInputStream,
            )
        }
    }

    enum class Endpoint(val path: String) {
        Preferences("prefs"),
        Start("start"),
        Status("status"),
        CurrentProfile("profiles/current"),
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
        private const val BaseUrl = "/localapi/v0/"
        private const val RequestTimeoutMillis = 10_000L
    }
}