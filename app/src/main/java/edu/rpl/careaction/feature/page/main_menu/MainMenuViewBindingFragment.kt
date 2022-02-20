package edu.rpl.careaction.feature.page.main_menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewbinding.ViewBinding
import edu.rpl.careaction.R
import edu.rpl.careaction.core.presentation.ApplicationViewModel
import edu.rpl.careaction.databinding.FragmentMainMenuBinding
import edu.rpl.careaction.module.presentation.ViewBindingFragment

class MainMenuViewBindingFragment : ViewBindingFragment<FragmentMainMenuBinding>() {

    private val applicationViewModel: ApplicationViewModel by activityViewModels()
    override val bindingInflater: (LayoutInflater) -> ViewBinding = FragmentMainMenuBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applicationViewModel.hideSplashScreenWhenActive()
        binding.bottomNavigationView.setupWithNavController(
            NavHostFragment.findNavController(
                binding.fragmentContainerView.getFragment()
            )
        )

        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                var pressedTime = 0L
                override fun handleOnBackPressed() {
                    if (pressedTime + 2000 > System.currentTimeMillis()) requireActivity().finish()
                    else Toast.makeText(
                        requireContext(),
                        getString(R.string.toast_back_to_exit),
                        Toast.LENGTH_SHORT
                    ).show()
                    pressedTime = System.currentTimeMillis()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
}