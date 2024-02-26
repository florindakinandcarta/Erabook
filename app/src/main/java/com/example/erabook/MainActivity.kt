package com.example.erabook

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.erabook.databinding.ActivityMainBinding
import com.example.erabook.firebaseActivities.LogInActivity
import com.example.erabook.util.GetCurrentUser

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavBar.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { controller, destination, _ ->
            if (destination.id == R.id.userProfileFragment && GetCurrentUser.getCurrentUser() == null) {
                controller.popBackStack()
                startActivity(Intent(this, LogInActivity::class.java))
            }
        }
    }
}
