package edu.rpl.careaction.feature.welcome.presentation.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.viewbinding.ViewBinding
import edu.rpl.careaction.R
import edu.rpl.careaction.databinding.FragmentSplashBinding
import edu.rpl.careaction.databinding.FragmentWelcomeBinding
import edu.rpl.careaction.feature.user.presentation.UserViewModel
import edu.rpl.careaction.module.ui.ViewBindingFragment


class SplashViewBindingFragment : ViewBindingFragment<FragmentSplashBinding>() {

    private val userViewModel : UserViewModel by activityViewModels()
    override val bindingInflater: (LayoutInflater) -> ViewBinding = FragmentWelcomeBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}