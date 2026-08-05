package ru.example.gitsource.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

internal object NetworkUtils {
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        return connectivityManager?.activeNetwork != null
    }

    @Composable
    fun rememberNetworkConnectivity(): Boolean {
        val context = LocalContext.current
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        var isConnected by remember { mutableStateOf(isNetworkAvailable(context)) }

        DisposableEffect(Unit) {
            val callback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    isConnected = true
                }
                override fun onLost(network: Network) {
                    isConnected = false
                }
            }
            connectivityManager.registerDefaultNetworkCallback(callback)
            onDispose {
                connectivityManager.unregisterNetworkCallback(callback)
            }
        }

        return isConnected
    }
}