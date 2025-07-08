package app.dvpn.libtailscale.internal.client

import app.dvpn.libtailscale.Tailscale
import app.dvpn.libtailscale.internal.api.TailscaleApi
import app.dvpn.libtailscale.internal.api.model.LoginProfile
import app.dvpn.libtailscale.internal.api.model.StartOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import libtailscale.IPNService
import libtailscale.Libtailscale

internal class TailscaleAndroidClient(
    private val api: TailscaleApi,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.IO),
) : Tailscale {

    override suspend fun login(authKey: String) {
        runCatching {
            api.editSettings {
                Patch(
                    controlURL = DefaultControlUrl,
                    wantRunning = true,
                    loggedOut = false
                )
            }
        }.mapCatching { settings ->
            api.start(StartOptions(settings, authKey))
        }.mapCatching {
            api.loginInteractive()
        }
        .getOrThrow()
    }

    override suspend fun getProfile(): LoginProfile {
        return api.getProfile()
    }

    override suspend fun getProfiles(): List<LoginProfile> {
        return api.getProfiles()
    }

    override suspend fun useExitNode(id: String) {
        api.editSettings { Patch(exitNodeID = id) }
    }

    override suspend fun logout() {
        api.logout()
    }

    override fun enableVPN(service: IPNService) {
        coroutineScope.launch {
            runCatching {
                api.editSettings { Patch(wantRunning = true) }
            }.onSuccess {
                withContext(Dispatchers.Main) {
                    Libtailscale.requestVPN(service)
                }
            }
        }
    }

    override fun disableVPN(service: IPNService) {
        coroutineScope.launch {
            runCatching {
                api.editSettings { Patch(wantRunning = false) }
            }.onSuccess {
                withContext(Dispatchers.Main) {
                    Libtailscale.serviceDisconnect(service)
                }
            }
        }
    }

    private companion object {
        @Suppress("ConstPropertyName")
        private const val DefaultControlUrl = "https://controlplane.tailscale.com"
    }
}
