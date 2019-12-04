package com.example.android.codelabs.navigation


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.android.codelabs.navigation.RegistrationViewModel.RegistrationState.COLLECT_USER_PASSWORD
import kotlinx.android.synthetic.main.enter_profile_data_fragment.*

/**
 * A simple [Fragment] subclass.
 */
class EnterProfileDataFragment : Fragment() {

    val registrationViewModel by activityViewModels<RegistrationViewModel>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.enter_profile_data_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val navController = findNavController()

        // When the next button is clicked, collect the current values from the
        // two edit texts and pass to the ViewModel to store.
        view.findViewById<Button>(R.id.next_register_button).setOnClickListener {
            val name = full_name_edit_text.text.toString()
            val bio = bio_edit_text.text.toString()
            registrationViewModel.collectProfileData(name, bio)
        }

        // RegistrationViewModel will update the registrationState to
        // COLLECT_USER_PASSWORD when ready to move to the choose username and
        // password screen.
        registrationViewModel.registrationState.observe(
                viewLifecycleOwner, Observer { state ->
            if (state == COLLECT_USER_PASSWORD) {
                navController.navigate(R.id.move_to_choose_user_password)
            }
        })

        // If the user presses back, cancel the user registration and pop back
        // to the login fragment. Since this ViewModel is shared at the activity
        // scope, its state must be reset so that it will be in the initial
        // state if the user comes back to register later.
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            registrationViewModel.userCancelledRegistration()
            navController.popBackStack(R.id.login_fragment, false)
        }
    }
}
