package app.dvpn.libtailscale

import android.content.Context
import android.content.Context.MODE_PRIVATE
import app.dvpn.libtailscale.internal.api.TailscaleApi
import app.dvpn.libtailscale.internal.api.adapter.TailscaleApiAdapter
import app.dvpn.libtailscale.internal.api.model.LoginProfile
import app.dvpn.libtailscale.internal.api.serialization.AnySerializer
import app.dvpn.libtailscale.internal.client.TailscaleAndroidClient
import app.dvpn.libtailscale.internal.context.TailscaleContext
import app.dvpn.libtailscale.internal.context.network.DNSConfigProvider
import app.dvpn.libtailscale.internal.context.preferences.TailscalePreferences
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import libtailscale.IPNService
import libtailscale.Libtailscale

interface Tailscale {

    suspend fun login(authKey: String)

    suspend fun getProfile(): LoginProfile

    suspend fun getProfiles(): List<LoginProfile>

    suspend fun useExitNode(id: String)

    suspend fun logout()

    fun enableVPN(service: IPNService)

    fun disableVPN(service: IPNService)

    companion object {

        fun create(
            context: Context,
            logger: (String, String) -> Unit,
        ): Tailscale {
            val dnsProvider = DNSConfigProvider(context)
            val preferences = TailscalePreferences(
                json = TailscaleJson,
                delegate = context.getSharedPreferences("secret_shared_prefs", MODE_PRIVATE)
            )
            val application = Libtailscale.start(
                context.filesDir.absolutePath,
                context.filesDir.absolutePath,
                TailscaleContext(preferences, dnsProvider::provide, logger)
            )
            val tailscaleApi = TailscaleApi(
                json = TailscaleJson,
                adapter = TailscaleApiAdapter(application)
            )

            return TailscaleAndroidClient(tailscaleApi)
        }
    }
}

internal val TailscaleJson = Json {
    ignoreUnknownKeys = true
    serializersModule = SerializersModule {
        contextual(Any::class, AnySerializer)
    }
}
