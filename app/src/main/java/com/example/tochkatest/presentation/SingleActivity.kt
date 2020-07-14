package com.example.tochkatest.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tochkatest.R
import com.google.android.material.navigation.NavigationView

class SingleActivity : AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    lateinit var customToolbar: Toolbar
    lateinit var navigationView: NavigationView
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        customToolbar = findViewById(R.id.toolbar)
        setSupportActionBar(customToolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(setOf(R.id.nav_search_users), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)

        navigationView = findViewById(R.id.nav_view)
        navigationView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    // Экран с авторизацией пользователя
    fun startAuthFragment() {
        navController.navigate(R.id.nav_auth)
    }

    // Экран с поиском пользователей
    fun startSearchUsersFragment() {
        navController.navigate(R.id.nav_search_users)
    }
}