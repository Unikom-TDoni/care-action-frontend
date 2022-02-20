package edu.rpl.careaction.feature.page.landing

import android.R.attr.defaultValue
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import edu.rpl.careaction.R
import edu.rpl.careaction.databinding.FragmentLandingContentBinding
import edu.rpl.careaction.module.presentation.ViewBindingFragment

class LandingContentViewBindingFragment : ViewBindingFragment<FragmentLandingContentBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding =
        FragmentLandingContentBinding::inflate

    private val landingImages = listOf(
        R.drawable.img_bg_landing_1,
        R.drawable.img_bg_landing_2,
        R.drawable.img_bg_landing_3
    )

    companion object {
        private const val bundleKey = "Position"
        fun newInstance(position: Int): LandingContentViewBindingFragment {
            val fragment = LandingContentViewBindingFragment()
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

    private fun changeContentBasedOnPosition(bundle: Bundle) =
        when (val position = bundle.getInt(bundleKey, defaultValue)) {
            in 0..2 -> {
                binding.txtViewTittle.text =
                    resources.getStringArray(R.array.txt_view_landing_tittle)[position]
                binding.txtViewContent.text =
                    resources.getStringArray(R.array.txt_view_landing_content)[position]
                binding.image.setImageResource(landingImages[position])
            }
            else ->
                requireParentFragment().findNavController()
                    .navigate(R.id.action_landingViewBindingFragment_to_registerViewBindingFragment)
        }
}