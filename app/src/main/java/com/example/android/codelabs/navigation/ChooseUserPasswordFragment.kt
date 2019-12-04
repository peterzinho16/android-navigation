package com.example.android.codelabs.navigation


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.addCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.android.codelabs.navigation.RegistrationViewModel.RegistrationState.REGISTRATION_COMPLETED
import kotlinx.android.synthetic.main.choose_user_password_fragment.*
import kotlinx.android.synthetic.main.login_fragment.*

/**
 * A simple [Fragment] subclass.
 */
class ChooseUserPasswordFragment : Fragment() {

    private val loginViewModel: LoginViewModel by activityViewModels()
    private val registrationViewModel: RegistrationViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.choose_user_password_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val navController = findNavController()


        // When the register button is clicked, collect the current values from
        // the two edit texts and pass to the ViewModel to complete registration.
        view.findViewById<Button>(R.id.register_fl_button).setOnClickListener {
            registrationViewModel.createAccountAndLogin(
                    userEditText.text.toString(),
                    passEditText.text.toString()
            )
        }

        // RegistrationViewModel updates the registrationState to
        // REGISTRATION_COMPLETED when ready, and for this example, the username
        // is accessed as a read-only property from RegistrationViewModel and is
        // used to directly authenticate with loginViewModel.
        registrationViewModel.registrationState.observe(
                viewLifecycleOwner, Observer { state ->
            if (state == REGISTRATION_COMPLETED) {

                // Here we authenticate with the token provided by the ViewModel
                // then pop back to the profie_fragment, where the user authentication
                // status will be tested and should be authenticated.
                val authToken = registrationViewModel.authToken
                loginViewModel.authenticate(authToken, "Password")
                navController.popBackStack(R.id.settings_dest, false)
            }
        }
        )

        // If the user presses back, cancel the user registration and pop back
        // to the login fragment. Since this ViewModel is shared at the activity
        // scope, its state must be reset so that it is in the initial state if
        // the user comes back to register later.
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            registrationViewModel.userCancelledRegistration()
            navController.popBackStack(R.id.login_fragment, false)
        }

    }


}
