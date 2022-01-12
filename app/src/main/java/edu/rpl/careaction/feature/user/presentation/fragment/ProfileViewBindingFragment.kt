package edu.rpl.careaction.feature.user.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import edu.rpl.careaction.ApplicationNavGraphDirections
import edu.rpl.careaction.R
import edu.rpl.careaction.databinding.FragmentMenuProfileBinding
import edu.rpl.careaction.feature.support.error.ErrorParcelable
import edu.rpl.careaction.feature.support.loading.LoadingDialogFragment
import edu.rpl.careaction.feature.user.domain.entity.User
import edu.rpl.careaction.feature.user.presentation.UserViewModel
import edu.rpl.careaction.module.api.ApiCallback
import edu.rpl.careaction.module.api.ApiResult
import edu.rpl.careaction.module.api.ErrorResponse
import edu.rpl.careaction.module.ui.ViewBindingFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.ResponseBody

class ProfileViewBindingFragment : ViewBindingFragment<FragmentMenuProfileBinding>() {

    private val userViewModel: UserViewModel by hiltNavGraphViewModels(R.id.menu_nav_graph)

    override val bindingInflater: (LayoutInflater) -> ViewBinding =
        FragmentMenuProfileBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar()
        initButtonEvent()
        initSharedFlowEvent()
    }

    override fun onResume() {
        super.onResume()
        userViewModel.fetch()
    }

    private fun initToolbar() =
        binding.toolbar.setNavigationOnClickListener { findNavController().popBackStack() }

    private fun initButtonEvent() {
        binding.btnLogout.setOnClickListener {
            userViewModel.logout()
        }
    }

    private fun initSharedFlowEvent() =
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    val callback = generateFetchApiCallback()
                    userViewModel.userSharedFlow.collect {
                        when (it) {
                            is ApiResult.Success -> callback.successCallback(it)
                            is ApiResult.Loading -> callback.loadingCallback(it)
                            is ApiResult.Error -> callback.errorCallback(it)
                        }
                    }
                }

                launch {
                    val callback = generateLogoutApiCallback()
                    userViewModel.defaultUserUpdateSharedFlow.collect {
                        when (it) {
                            is ApiResult.Success -> callback.successCallback(it)
                            is ApiResult.Loading -> callback.loadingCallback(it)
                            is ApiResult.Error -> callback.errorCallback(it)
                        }
                    }
                }

            }
        }

    private fun generateFetchApiCallback(): ApiCallback<User, ErrorResponse> =
        ApiCallback(
            successCallback = fun(success) {
                LoadingDialogFragment.dismiss()
                binding.txtViewName.text = success.response.name
                binding.txtViewAge.text = success.response.age.toString()
                binding.txtViewHeight.text =
                    getString(R.string.txt_view_defined_cm, success.response.height)
                binding.txtViewWeight.text =
                    getString(R.string.txt_view_defined_kg, success.response.weight)

                val circularProgressDrawable = CircularProgressDrawable(requireContext())
                circularProgressDrawable.strokeWidth = 5f
                circularProgressDrawable.centerRadius = 30f
                circularProgressDrawable.setTint(ContextCompat.getColor(requireContext(), R.color.green))
                circularProgressDrawable.start()

                Glide.with(this)
                    .load("https://sunrift.com/wp-content/uploads/2014/12/Blake-profile-photo-square.jpg")
                    .placeholder(circularProgressDrawable)
                    .into(binding.image)
            },
            loadingCallback = fun(_) {
                LoadingDialogFragment.show(
                    parentFragmentManager,
                    String()
                )
            },
            errorCallback = fun(error) {
                LoadingDialogFragment.dismiss()
                error.response.throwable?.let {
                    findNavController().navigate(
                        ApplicationNavGraphDirections.actionToErrorViewBindingFragment(
                            ErrorParcelable(it)
                        )
                    )
                } ?: Toast.makeText(context, error.response.message, Toast.LENGTH_SHORT).show()
            }
        )

    private fun generateLogoutApiCallback(): ApiCallback<ResponseBody, ErrorResponse> =
        ApiCallback(
            successCallback = fun(_) {
                LoadingDialogFragment.dismiss()
                requireParentFragment().requireParentFragment().findNavController()
                    .navigate(R.id.welcome_nav_graph)
            },
            loadingCallback = fun(_) {
                LoadingDialogFragment.show(
                    parentFragmentManager,
                    String()
                )
            },
            errorCallback = fun(error) {
                LoadingDialogFragment.dismiss()
                error.response.throwable?.let {
                    findNavController().navigate(
                        ApplicationNavGraphDirections.actionToErrorViewBindingFragment(
                            ErrorParcelable(it)
                        )
                    )
                } ?: Toast.makeText(context, error.response.message, Toast.LENGTH_SHORT).show()
            }
        )
}