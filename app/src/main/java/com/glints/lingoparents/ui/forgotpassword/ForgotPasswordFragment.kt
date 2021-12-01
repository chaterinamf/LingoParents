package com.glints.lingoparents.ui.forgotpassword

import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.glints.lingoparents.R
import com.glints.lingoparents.databinding.FragmentForgotPasswordBinding
import com.glints.lingoparents.utils.AuthFormValidator
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collect

class ForgotPasswordFragment : Fragment(R.layout.fragment_forgot_password) {

    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ForgotPasswordViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        _binding = FragmentForgotPasswordBinding.bind(view)

        binding.apply {
            mbtnBackToLogin.setOnClickListener {
                viewModel.onBackToLoginButtonClick()
            }
            mbtnSubmit.setOnClickListener {
                viewModel.onSubmitButtonClick(tilEmail.editText?.text.toString())
            }
        }

        activity?.onBackPressedDispatcher?.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            }
        )

        lifecycleScope.launchWhenStarted {
            viewModel.forgotPasswordEvent.collect { event ->
                when (event) {
                    is ForgotPasswordViewModel.ForgotPasswordEvent.NavigateBackToLogin -> {
                        findNavController().popBackStack()
                    }
                    is ForgotPasswordViewModel.ForgotPasswordEvent.TryToSubmitForgotPassword -> {
                        binding.apply {
                            AuthFormValidator.apply {
                                hideFieldError(tilEmail)

                                val email = event.email
                                if (isValidEmail(email)) {
                                    viewModel.sendForgotPasswordRequest(email)
                                } else {
                                    showFieldError(tilEmail, EMAIL_WRONG_FORMAT_ERROR)
                                }
                            }
                        }
                    }
                    is ForgotPasswordViewModel.ForgotPasswordEvent.Error -> {
                        showLoading(false)
                        if (event.message.contains("email not found", ignoreCase = true)) {
                            AuthFormValidator.showFieldError(binding.tilEmail, event.message)
                        }
                    }
                    is ForgotPasswordViewModel.ForgotPasswordEvent.Loading -> {
                        showLoading(true)
                    }
                    is ForgotPasswordViewModel.ForgotPasswordEvent.Success -> {
                        showLoading(false)
                        binding.apply {
                            Snackbar.make(root, event.message, Snackbar.LENGTH_LONG).show()
                            tilEmail.editText?.setText("")
                        }

                    }
                }
            }
        }
    }

    private fun showLoading(bool: Boolean) {
        binding.apply {
            when (bool) {
                true -> {
                    vLoadingBackground.visibility = View.VISIBLE
                    vLoadingProgress.visibility = View.VISIBLE
                }
                else -> {
                    vLoadingBackground.visibility = View.GONE
                    vLoadingProgress.visibility = View.GONE
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}