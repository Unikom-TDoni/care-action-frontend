package edu.rpl.careaction.feature.support.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import androidx.viewbinding.ViewBinding
import edu.rpl.careaction.core.presentation.ApplicationViewModel
import edu.rpl.careaction.databinding.FragmentMainMenuBinding
import edu.rpl.careaction.module.ui.ViewBindingFragment

class MainMenuViewBindingFragment : ViewBindingFragment<FragmentMainMenuBinding>() {

    private val applicationViewModel: ApplicationViewModel by activityViewModels()
    override val bindingInflater: (LayoutInflater) -> ViewBinding = FragmentMainMenuBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applicationViewModel.hideSplashScreenWhenActive()
        binding.bottomNavBar.setupWithNavController(NavHostFragment.findNavController(binding.fragmentContainerView.getFragment()))
    }
}