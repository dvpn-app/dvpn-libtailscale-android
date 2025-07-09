package dvpn.libtailscale.example

import android.app.Application
import dvpn.libtailscale.example.di.AppModule
import dvpn.libtailscale.example.notification.NotificationChannels
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin
import timber.log.Timber
import timber.log.Timber.DebugTree

class ExampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
        initTimber()
        NotificationChannels.create(this)
    }

    private fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@ExampleApplication)
            modules(AppModule)
        }
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }
}
