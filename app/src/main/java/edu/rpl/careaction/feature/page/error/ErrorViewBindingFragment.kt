package edu.rpl.careaction.feature.page.error

import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewbinding.ViewBinding
import edu.rpl.careaction.R
import edu.rpl.careaction.databinding.FragmentErrorBinding
import edu.rpl.careaction.module.presentation.ViewBindingFragment
import java.net.UnknownHostException

class ErrorViewBindingFragment : ViewBindingFragment<FragmentErrorBinding>() {

    private val args: ErrorViewBindingFragmentArgs by navArgs()
    override val bindingInflater: (LayoutInflater) -> ViewBinding = FragmentErrorBinding::inflate

    override fun onResume() {
        super.onResume()

        initView()
        initNavigationEvent()
    }

    private fun initView() {
        when (args.error.throwable) {
            is UnknownHostException -> {
                setError(
                    R.string.connection_error_tittle,
                    R.string.connection_error_content,
                    R.drawable.ic_custom_connection_error_24
                )
            }
            else -> {
                setError(
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

    private fun initNavigationEvent() {
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

    private fun setError(tittleId: Int, contentId: Int, drawableId: Int) {
        binding.tittle.text = getString(tittleId)
        binding.content.text = getString(contentId)
        binding.image.setImageDrawable(ResourcesCompat.getDrawable(resources, drawableId, null))
    }
}
