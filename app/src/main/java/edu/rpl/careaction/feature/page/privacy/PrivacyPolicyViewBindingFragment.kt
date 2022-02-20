package edu.rpl.careaction.feature.page.privacy

import android.os.Bundle
import android.text.Html
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import edu.rpl.careaction.R
import edu.rpl.careaction.databinding.FragmentPrivacyPolicyBinding
import edu.rpl.careaction.module.presentation.ViewBindingFragment

class PrivacyPolicyViewBindingFragment : ViewBindingFragment<FragmentPrivacyPolicyBinding>() {
    override val bindingInflater: (LayoutInflater) -> ViewBinding =
        FragmentPrivacyPolicyBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initNavigationEvent()
    }

    private fun initView() {
        binding.text.text =
            Html.fromHtml(getString(R.string.privacy_policy), Html.FROM_HTML_MODE_COMPACT)
        binding.text.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun initNavigationEvent() {
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
        binding.toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
    }
}