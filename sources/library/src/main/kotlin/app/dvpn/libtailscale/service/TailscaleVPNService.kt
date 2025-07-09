@file:Suppress("PrivatePropertyName", "ConstPropertyName")

package app.dvpn.libtailscale.service

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.net.VpnService
import android.system.OsConstants
import android.util.Log
import app.dvpn.libtailscale.Tailscale
import java.util.UUID

abstract class TailscaleVPNService : VpnService(), libtailscale.IPNService {

    private val id = UUID.randomUUID().toString()

    abstract val client: Tailscale

    abstract val pendingIntent: PendingIntent

    abstract val notificationId: Int

    abstract fun buildNotification(): Notification

    override fun id(): String {
        return id
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return when (intent?.action) {
            ActionStopVPN -> {
                close()
                START_NOT_STICKY
            }

            ActionRestartVPN -> {
                client.enableVPN(this)
                START_NOT_STICKY
            }

            ActionStartVPN -> {
                client.enableVPN(this)
                startForeground(notificationId, buildNotification())
                START_STICKY
            }

            ActionSystemStartVPN -> {
                client.enableVPN(this)
                START_STICKY
            }

            else -> {
                client.enableVPN(this)
                startForeground(notificationId, buildNotification())
                START_STICKY
            }
        }
    }

    override fun close() {
        disconnectVPN()
        client.disableVPN(this)
    }

    override fun disconnectVPN() {
        stopSelf()
    }

    override fun onDestroy() {
        close()
        updateVpnStatus(false)
        super.onDestroy()
    }

    override fun onRevoke() {
        close()
        updateVpnStatus(false)
        super.onRevoke()
    }

    override fun newBuilder(): VPNServiceBuilder {
        return Builder()
            .setConfigureIntent(pendingIntent)
            .allowFamily(OsConstants.AF_INET)
            .allowFamily(OsConstants.AF_INET6)
            .setMetered(false)
            .setUnderlyingNetworks(null)
            .let(::VPNServiceBuilder)
    }

    companion object {
        const val ActionStartVPN = "app.dvpn.action.START_VPN"
        const val ActionStopVPN = "app.dvpn.action.STOP_VPN"
        const val ActionRestartVPN = "app.dvpn.action.RESTART_VPN"
        const val ActionSystemStartVPN = "android.net.VpnService"
    }
}
