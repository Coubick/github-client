package ru.example.gitsource.data.auth

import android.content.Context
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import ru.example.gitsource.data.network.NetworkConstants
import ru.example.gitsource.domain.OAuthLauncher
import javax.inject.Inject

internal class OAuthLauncherImpl @Inject constructor() : OAuthLauncher {

    override fun launchAuth(context: Context) {
        val customTabsIntent = CustomTabsIntent.Builder().build()
        val uri = buildUrl().toUri()
        customTabsIntent.launchUrl(context, uri)
    }

    private fun buildUrl(): String {
        return NetworkConstants.AUTHORIZE_URL +
                "?client_id=${NetworkConstants.GITHUB_CLIENT_ID}" +
                "&redirect_uri=${NetworkConstants.REDIRECT_URI}"
    }
}