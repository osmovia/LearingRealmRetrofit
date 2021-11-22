package com.example.learingrealmandretrofit.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.learingrealmandretrofit.R
import com.example.learingrealmandretrofit.databinding.SettingsFragmentBinding

class SettingFragment : Fragment() {

    private lateinit var binding: SettingsFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SettingsFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val mainNavController: NavController?  = activity?.findNavController(R.id.containerView)
        binding.textViewSetting.setOnClickListener {
            mainNavController?.navigate(R.id.action_tabsFragment_to_authenticationFragment)
        }
    }
}
