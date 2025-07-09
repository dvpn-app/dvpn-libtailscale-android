package dvpn.libtailscale.example.di

import android.util.Log
import app.dvpn.libtailscale.Tailscale
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val AppModule = module {

    single<Tailscale> {
        Tailscale.create(
            context = androidContext(),
            logger = Log::d
        )
    }
}
