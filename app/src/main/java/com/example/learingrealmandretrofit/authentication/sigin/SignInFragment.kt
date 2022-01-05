package com.example.learingrealmandretrofit.authentication.sigin

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
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.learingrealmandretrofit.*
import com.example.learingrealmandretrofit.databinding.SignInFragmentBinding

class SignInFragment : Fragment() {

    private lateinit var binding: SignInFragmentBinding
    private lateinit var viewModel: SignInViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SignInFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val appBarConfiguration = AppBarConfiguration(setOf(R.id.signUpFragment, R.id.signInFragment))

        NavigationUI.setupWithNavController(binding.toolbarContainer.toolbarId, findNavController(), appBarConfiguration)

        viewModel = ViewModelProvider(this).get(SignInViewModel::class.java)

        viewModel.showSpinner.observe(viewLifecycleOwner, Observer { showSpinner ->
            if (showSpinner) {
                activity?.showProgress()
            } else {
                activity?.hideProgress()
            }
        })

        viewModel.showToast.observe(viewLifecycleOwner, Observer { message->
            context?.showErrorCodeOrStringResource(message)
        })

        viewModel.saveSession.observe(viewLifecycleOwner, Observer { sessions ->
            SharedPreferencesManager(requireContext()).saveAuthentication(
                token = sessions.sessionToken,
                email = sessions.email,
                userId = sessions.userId
            )
        })

        viewModel.success.observe(viewLifecycleOwner, Observer { success ->
            val mainNavController: NavController?  = activity?.findNavController(R.id.containerView)
            if (success) {
                mainNavController?.navigate(R.id.action_authenticationFragment_to_tabsFragment)
            }
        })

        binding.buttonEntry.setOnClickListener {
            viewModel.entryApp(
                emailView = binding.email.text.toString(),
                passwordView = binding.password.text.toString()
            )
        }

        binding.textViewSignUp.setOnClickListener {
            findNavController().popBackStack()
        }
    }
}
