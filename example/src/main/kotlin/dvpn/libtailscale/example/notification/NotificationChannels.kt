package dvpn.libtailscale.example.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

object NotificationChannels {

    fun create(context: Context) {
        val notificationManager = context.getSystemService(NotificationManager::class.java)

        with(notificationManager) {
            createNotificationChannel(getDVPNChannel())
        }
    }

    private fun getDVPNChannel(): NotificationChannel {
        return NotificationChannel(
            "dvpn",
            "DVPN",
            NotificationManager.IMPORTANCE_DEFAULT
        )
    }
}
