package app.dvpn.environment.config

import app.dvpn.environment.config.internal.Field
import app.dvpn.environment.config.internal.buildConfig

/**
 * Represents application BuildConfig.
 * Each field will be automatically applied to `debug` and `release` build types.
 */
internal val LibTailscaleBuildConfig = buildConfig {

    +field("LANGUAGES") {
        value { +"en,de,es,fr,it,pt" }
    }
}

internal inline val debugBuildConfig: List<BuildConfigField>
    get() = LibTailscaleBuildConfig.map(Field::debug)

internal inline val releaseBuildConfig: List<BuildConfigField>
    get() = LibTailscaleBuildConfig.map(Field::release)
