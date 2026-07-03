package ru.example.gitsource

import android.os.Bundle
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        onBackPressedDispatcher.addCallback(this) {
            if (navController.currentDestination?.id == R.id.authFragment) {
                finish()
            } else {
                navController.popBackStack()
            }
        }
    }
}