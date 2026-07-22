package ru.example.gitsource

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import dagger.hilt.android.AndroidEntryPoint
import ru.example.gitsource.domain.LoginStatusChecker
import ru.example.gitsource.navigation.Navigator
import javax.inject.Inject

@AndroidEntryPoint
internal class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var navigator: Navigator

    @Inject
    lateinit var loginChecker: LoginStatusChecker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initNavigator()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.activity_main) as NavHostFragment
        val navController = navHostFragment.navController
        navController.handleDeepLink(intent)
    }

    private fun initNavigator() {
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.activity_main) as NavHostFragment
        val navController = navHostFragment.navController

        navigator.setNavController(navController)
        navigator.setOnBackPressedDispatcher(onBackPressedDispatcher)
        navigator.setActivity(this)
        navigator.setupBackPressedHandler()
    }
}