package dvpn.libtailscale.example.service

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import app.dvpn.libtailscale.Tailscale
import app.dvpn.libtailscale.service.TailscaleVPNService
import dvpn.libtailscale.example.MainActivity
import dvpn.libtailscale.example.R
import org.koin.android.ext.android.inject
import timber.log.Timber

class DVPNService : TailscaleVPNService() {

    override val client: Tailscale by inject()

    override val notificationId: Int = 666

    override val pendingIntent: PendingIntent
        get() = PendingIntent.getActivity(
            this,
            0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE
        )

    override fun buildNotification(): Notification {
        return NotificationCompat.Builder(this, "dvpn")
            .setContentTitle("DVPN")
            .setContentText("VPN is running")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentIntent(pendingIntent)
            .build()
    }

    override fun updateVpnStatus(active: Boolean) {
        Timber.tag("DVPNService").d("VPN status updated: $active")
    }
}
