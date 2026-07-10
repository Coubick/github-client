package ru.example.gitsource.data

import android.content.Context
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.net.toUri
import dagger.hilt.android.qualifiers.ApplicationContext
import ru.example.gitsource.data.network.NetworkConstants.AUTHORIZE_URL
import ru.example.gitsource.data.network.NetworkConstants.REDIRECT_URI
import ru.example.gitsource.domain.OAuthLauncher
import javax.inject.Inject

internal class OAuthLauncherImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : OAuthLauncher {

    override fun launchAuth(clientId: String) {
        val customTabsIntent = CustomTabsIntent.Builder().build()
        val uri = buildUrl(clientId).toUri()
        customTabsIntent.launchUrl(context, uri)
    }

    private fun buildUrl(clientId: String): String {
        return AUTHORIZE_URL +
                "?client_id=$clientId" +
                "&redirect_uri=$REDIRECT_URI"
    }
}