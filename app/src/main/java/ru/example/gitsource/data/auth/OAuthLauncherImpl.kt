package ru.example.gitsource.data.auth

import android.app.Activity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import ru.example.gitsource.data.network.NetworkConstants
import ru.example.gitsource.domain.OAuthLauncher
import javax.inject.Inject

internal class OAuthLauncherImpl @Inject constructor(
    private val activity: Activity
) : OAuthLauncher {

    override fun launchOAuth() {
        val customTabsIntent = CustomTabsIntent.Builder().build()
        val uri = buildUrl().toUri()
        customTabsIntent.launchUrl(activity, uri)
    }

    private fun buildUrl(): String {
        return NetworkConstants.AUTHORIZE_URL +
                "?client_id=${NetworkConstants.GITHUB_CLIENT_ID}" +
                "&redirect_uri=${NetworkConstants.REDIRECT_URI}"
    }
}