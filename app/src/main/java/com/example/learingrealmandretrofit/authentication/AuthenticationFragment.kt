package com.example.learingrealmandretrofit.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.learingrealmandretrofit.MainActivity
import com.example.learingrealmandretrofit.R
import com.example.learingrealmandretrofit.databinding.AuthenticationFragmentBinding

class AuthenticationFragment : Fragment() {

    private lateinit var binding: AuthenticationFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = AuthenticationFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val navHost = childFragmentManager.findFragmentById(R.id.containerViewAuthentication) as NavHostFragment
        val navController = navHost.navController
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.signUpFragment, R.id.signInFragment)
        )
        NavigationUI.setupActionBarWithNavController(
            (requireActivity() as MainActivity), navController, appBarConfiguration
        )
    }
}
