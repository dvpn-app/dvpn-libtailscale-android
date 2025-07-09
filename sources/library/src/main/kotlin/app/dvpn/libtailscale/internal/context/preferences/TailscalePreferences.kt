package app.dvpn.libtailscale.internal.context.preferences

import android.content.SharedPreferences
import kotlinx.serialization.json.Json

internal class TailscalePreferences(
    private val json: Json,
    private val delegate: SharedPreferences,
) : SharedPreferences by delegate {

    fun getStateStorePreferencesJson(): String {
        return delegate.all.keys
            .filter { key -> KeyPrefixStateStore in key }
            .map { key -> key.removePrefix(KeyPrefixStateStore) }
            .let(json::encodeToString)
    }

    override fun getString(key: String, defValue: String?): String? {
        if (!delegate.contains(key)) return defValue
        return delegate.getString(key, defValue)
    }

    override fun getBoolean(key: String, defValue: Boolean): Boolean {
        if (!delegate.contains(key)) return defValue
        return delegate.getBoolean(key, defValue)
    }

    override fun getStringSet(key: String, defValues: Set<String>?): Set<String>? {
        if (!delegate.contains(key)) return defValues
        return delegate.getStringSet(key, defValues)
    }

    private companion object {
        @Suppress("ConstPropertyName")
        private const val KeyPrefixStateStore = "statestore-"
    }
}
