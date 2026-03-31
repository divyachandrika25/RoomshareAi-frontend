package com.simats.roomshareairentalapp

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class SessionManager(context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "roomshare_prefs"
        private const val KEY_TOKEN = "auth_token"
        private const val KEY_EMAIL = "user_email"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_IS_PREMIUM = "is_premium_user"
        private const val KEY_AI_USAGE = "ai_usage_count"
        private const val KEY_AI_SEARCH_USAGE = "ai_search_usage_count"
    }

    fun saveAuthToken(token: String) {
        prefs.edit {
            putString(KEY_TOKEN, token)
        }
    }

    fun fetchAuthToken(): String? {
        return prefs.getString(KEY_TOKEN, null)
    }

    fun saveUserEmail(email: String) {
        prefs.edit {
            putString(KEY_EMAIL, email)
            putBoolean(KEY_IS_LOGGED_IN, true)
        }
    }

    fun fetchUserEmail(): String? {
        return prefs.getString(KEY_EMAIL, null)
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun logout() {
        prefs.edit {
            clear()
        }
    }

    // --- Subscription & Rate Limiting ---

    fun isPremiumUser(): Boolean {
        return prefs.getBoolean(KEY_IS_PREMIUM, false)
    }

    fun setPremiumUser(isPremium: Boolean) {
        prefs.edit {
            putBoolean(KEY_IS_PREMIUM, isPremium)
        }
    }

    fun getAiUsageCount(): Int {
        return prefs.getInt(KEY_AI_USAGE, 0)
    }

    fun incrementAiUsage() {
        prefs.edit {
            putInt(KEY_AI_USAGE, getAiUsageCount() + 1)
        }
    }

    fun getAiSearchUsageCount(): Int {
        return prefs.getInt(KEY_AI_SEARCH_USAGE, 0)
    }

    fun incrementAiSearchUsage() {
        prefs.edit {
            putInt(KEY_AI_SEARCH_USAGE, getAiSearchUsageCount() + 1)
        }
    }
}
