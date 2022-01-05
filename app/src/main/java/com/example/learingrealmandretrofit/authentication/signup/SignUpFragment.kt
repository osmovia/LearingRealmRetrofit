package com.example.learingrealmandretrofit.authentication.signup

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
import com.example.learingrealmandretrofit.databinding.SignUpFragmentBinding

class SignUpFragment : Fragment() {

    private lateinit var binding: SignUpFragmentBinding
    private lateinit var viewModel: SignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SignUpFragmentBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val appBarConfiguration = AppBarConfiguration(setOf(R.id.signUpFragment, R.id.signInFragment))

        NavigationUI.setupWithNavController(binding.toolbarContainer.toolbarId, findNavController(), appBarConfiguration)

        val mainNavController: NavController?  = activity?.findNavController(R.id.containerView)

        viewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)

        viewModel.saveSession.observe(viewLifecycleOwner, Observer { sessions ->
            SharedPreferencesManager(requireContext()).saveAuthentication(
                token = sessions.sessionToken,
                email = sessions.email,
                userId = sessions.userId
            )
        })

        viewModel.success.observe(viewLifecycleOwner, Observer { userRegistered ->
            if (userRegistered) {
                mainNavController?.navigate(R.id.action_authenticationFragment_to_tabsFragment)
            }
        })

        viewModel.showToast.observe(viewLifecycleOwner, Observer { message ->
            context?.showErrorCodeOrStringResource(message)
        })

        viewModel.inputLayoutEmail.observe(viewLifecycleOwner, Observer { errorEmail ->
            if (errorEmail == null) {
                binding.layoutEmail.error = errorEmail
            } else {
                binding.layoutEmail.error = getString(errorEmail)
            }
        })

        viewModel.inputLayoutPassword.observe(viewLifecycleOwner, Observer { errorPassword ->
            if (errorPassword == null) {
                binding.layoutPassword.error = errorPassword
            } else {
                binding.layoutPassword.error = getString(errorPassword)
            }
        })

        viewModel.inputLayoutPasswordConfirmation.observe(viewLifecycleOwner, Observer { errorPasswordConfirmation ->
            if (errorPasswordConfirmation == null) {
                binding.layoutPasswordConfirmation.error = errorPasswordConfirmation
            } else {
                binding.layoutPasswordConfirmation.error = getString(errorPasswordConfirmation)
            }
        })

        viewModel.spinner.observe(viewLifecycleOwner, Observer { showSpinner ->
            if (showSpinner) {
                activity?.showProgress()
            } else {
                activity?.hideProgress()
            }
        })

        binding.textViewSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }

        binding.email.setOnFocusChangeListener { _, _ ->
            viewModel.inputLayoutEmail.value = null
        }

        binding.password.setOnFocusChangeListener { _, _ ->
            viewModel.inputLayoutPassword.value = null
        }

        binding.passwordConfirmation.setOnFocusChangeListener { _, _ ->
            viewModel.inputLayoutPasswordConfirmation.value = null
        }

        binding.buttonRegistration.setOnClickListener {
            viewModel.clickRegistration(
                emailView = binding.email.text.toString(),
                passwordView = binding.password.text.toString(),
                passwordConfirmationView = binding.passwordConfirmation.text.toString()
            )
        }
    }
}
