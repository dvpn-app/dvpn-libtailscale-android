import com.android.build.gradle.BaseExtension
import com.android.builder.internal.ClassFieldImpl
import app.dvpn.environment.Environment
import app.dvpn.environment.config.BuildConfigField

configure<BaseExtension> {
    buildTypes {
        getByName("release") {
            isMinifyEnabled = !isAndroidLibrary
            isShrinkResources = !isAndroidLibrary
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
            Environment.production.buildConfig
                .map { it.asClassField }
                .onEach(::addBuildConfigField)
        }
        getByName("debug") {
            versionNameSuffix("-dev")
            isMinifyEnabled = false
            isShrinkResources = false
            isDebuggable = true
            matchingFallbacks += listOf("release")
            Environment.debug.buildConfig
                .map { it.asClassField }
                .onEach(::addBuildConfigField)
        }
    }
}

inline val BuildConfigField.asClassField: ClassFieldImpl
    get() = ClassFieldImpl(value.javaClass.simpleName, key, "\"$value\"")
inline val isAndroidLibrary: Boolean
    get() = project.pluginManager.hasPlugin("convention.android-library")
