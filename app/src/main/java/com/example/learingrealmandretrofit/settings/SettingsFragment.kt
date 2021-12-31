package com.example.learingrealmandretrofit.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.learingrealmandretrofit.*
import com.example.learingrealmandretrofit.databinding.SettingsFragmentBinding

class SettingsFragment : Fragment() {

    private lateinit var binding: SettingsFragmentBinding
    private lateinit var viewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SettingsFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        viewModel = ViewModelProvider(this).get(SettingsViewModel::class.java)

        viewModel.showSpinner.observe(viewLifecycleOwner, Observer { showSpinner ->
            if (showSpinner) {
                activity?.showProgress()
            } else {
                activity?.hideProgress()
            }
        })

        viewModel.showToast.observe(viewLifecycleOwner, Observer { message ->
            context?.showErrorCodeOrStringResource(message)
        })

        viewModel.success.observe(viewLifecycleOwner, Observer { success ->
            if (success) {
                SharedPreferencesManager(requireContext()).clearSharedPreferences()
                val mainNavController: NavController? = activity?.findNavController(R.id.containerView)
                mainNavController?.navigate(R.id.action_tabsFragment_to_authenticationFragment)
            }
        })

        binding.buttonSignOut.setOnClickListener {
            viewModel.signOutRetrofit(
                SharedPreferencesManager(requireContext())
                    .fetchAuthentication()
                    .sessionToken ?: return@setOnClickListener
            )
        }
    }
}
