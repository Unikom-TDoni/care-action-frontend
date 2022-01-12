package edu.rpl.careaction.feature.support.error

import android.view.LayoutInflater
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import edu.rpl.careaction.R
import edu.rpl.careaction.databinding.FragmentErrorBinding
import edu.rpl.careaction.module.ui.ViewBindingFragment
import java.net.ConnectException

class ErrorViewBindingFragment : ViewBindingFragment<FragmentErrorBinding>() {
    override val bindingInflater: (LayoutInflater) -> ViewBinding = FragmentErrorBinding::inflate

    private val args: ErrorViewBindingFragmentArgs by navArgs()

    override fun onResume() {
        super.onResume()
        initView()
    }

    private fun initView() {
        when (args.error.throwable) {
            is ConnectException -> {
                initErrorView(
                    R.string.connection_error_tittle,
                    R.string.connection_error_content,
                    R.drawable.ic_custom_connection_error_24
                )
            }
            else -> {
                initErrorView(
                    R.string.unknown_error_tittle,
                    R.string.unknown_error_content,
                    R.drawable.ic_custom_error_24
                )
            }
        }

        binding.button.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initErrorView(tittleId: Int, contentId: Int, drawableId: Int) {
        binding.tittle.text = getString(tittleId)
        binding.content.text = getString(contentId)
        binding.image.setImageDrawable(ResourcesCompat.getDrawable(resources, drawableId, null))
    }
}
