package edu.rpl.careaction.feature.menu.welcome

import android.view.View
import android.os.Bundle
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import edu.rpl.careaction.databinding.FragmentWelcomeContentBinding
import edu.rpl.careaction.module.ui.ViewBindingFragment
import android.R.attr.defaultValue
import androidx.navigation.fragment.findNavController
import edu.rpl.careaction.R

class WelcomeContentViewBindingFragment : ViewBindingFragment<FragmentWelcomeContentBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding =
        FragmentWelcomeContentBinding::inflate

    companion object {
        private const val bundleKey = "Position"
        fun newInstance(position: Int): WelcomeContentViewBindingFragment {
            val fragment = WelcomeContentViewBindingFragment()
            val bundle = Bundle()
            bundle.putInt(bundleKey, position)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        changeContentBasedOnPosition(arguments!!)
    }

    private fun changeContentBasedOnPosition(bundle: Bundle) {
        when (val position = bundle.getInt(bundleKey, defaultValue)) {
            in 0..2 -> {
                binding.txtViewTittle.text =
                    resources.getStringArray(R.array.txt_view_welcome_tittle)[position]
                binding.txtViewContent.text =
                    resources.getStringArray(R.array.txt_view_welcome_content)[position]
            }
            else ->
                requireParentFragment().findNavController()
                    .navigate(R.id.action_welcomeViewBindingFragment_to_registerViewBindingFragment)
        }
    }
}