package com.simple.firstapp

import android.os.Bundle
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.simple.firstapp.base.BaseActivity
import com.simple.firstapp.databinding.MainActivityBinding

class MainActivity : BaseActivity(), BaseActivity.ActivityListener {

    private lateinit var navController: NavController

    private lateinit var binding: MainActivityBinding

    private val viewModel by viewModels<MainActivityViewModel>()

    override fun getNavController(): NavController = navController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity)
        binding.lifecycleOwner = this
        val navHost = supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        navController = navHost.navController
        setSupportActionBar(binding.toolbar)
        setupActionBarWithNavController(navController)
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.toastValueMLD.observe(this, {
            Toast.makeText(this, it, LENGTH_SHORT).show()
        })
    }
    override fun onNavigate(navDirections: NavDirections) {
        navigateTo(navDirections)
    }

    override fun updateToolbarTitle(title: String) {
        binding.toolbar.title = title
    }

    override fun hideBackButton(hideBackButton: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(!hideBackButton)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
