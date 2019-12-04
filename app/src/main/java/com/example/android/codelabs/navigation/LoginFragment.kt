package com.example.android.codelabs.navigation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.android.codelabs.navigation.LoginViewModel.AuthenticationState.AUTHENTICATED
import com.example.android.codelabs.navigation.LoginViewModel.AuthenticationState.INVALID_AUTHENTICATION

/**
 * A simple [Fragment] subclass.
 */
class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by activityViewModels()

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        usernameEditText = view.findViewById(R.id.username_edit_text)
        passwordEditText = view.findViewById(R.id.password_edit_text)

        loginButton = view.findViewById(R.id.login_button)
        loginButton.setOnClickListener {
            viewModel.authenticate(usernameEditText.text.toString(),
                    passwordEditText.text.toString())
        }

        registerButton = view.findViewById(R.id.register_button)
        registerButton?.setOnClickListener{
            findNavController().navigate(R.id.action_login_fragment_to_register_fragment, null)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.refuseAuthentication()
            findNavController().popBackStack(R.id.home_dest, false)
        }

        val navController = findNavController()
        viewModel.authenticationState.observe(viewLifecycleOwner, Observer { authenticationState ->
            when (authenticationState) {
                AUTHENTICATED -> navController.navigate(R.id.nav_seller_graph)
                INVALID_AUTHENTICATION -> showErrorMessage()
            }
        })
    }

    private fun showErrorMessage() {
        Toast.makeText(activity,"Credenciales incorrectas" , Toast.LENGTH_LONG).show()
    }

}
