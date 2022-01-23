package edu.rpl.careaction.feature.support.on_progress

import android.view.View
import android.os.Bundle
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import edu.rpl.careaction.module.ui.ViewBindingFragment
import edu.rpl.careaction.databinding.FragmentOnProgressBinding

class OnProgressViewBindingFragment : ViewBindingFragment<FragmentOnProgressBinding>() {
    override val bindingInflater: (LayoutInflater) -> ViewBinding =
        FragmentOnProgressBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initNavigationEvent()
    }

    private fun initNavigationEvent() {
        binding.button.setOnClickListener { findNavController().popBackStack() }
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }
}