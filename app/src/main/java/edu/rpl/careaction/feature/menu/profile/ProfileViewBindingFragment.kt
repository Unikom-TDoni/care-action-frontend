package edu.rpl.careaction.feature.menu.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import edu.rpl.careaction.R
import edu.rpl.careaction.core.domain.ErrorResponse
import edu.rpl.careaction.core.utility.DefaultApiCallbackUtility
import edu.rpl.careaction.databinding.FragmentMenuProfileBinding
import edu.rpl.careaction.feature.notification.NotificationWorkManager
import edu.rpl.careaction.feature.support.setting.domain.Setting
import edu.rpl.careaction.feature.user.domain.entity.User
import edu.rpl.careaction.module.api.ApiCallback
import edu.rpl.careaction.module.api.ApiResult
import edu.rpl.careaction.module.ui.ViewBindingFragment
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.ResponseBody

class ProfileViewBindingFragment : ViewBindingFragment<FragmentMenuProfileBinding>() {

    private val notificationWorkManager = NotificationWorkManager()
    private val profileViewModel: ProfileViewModel by hiltNavGraphViewModels(R.id.bottom_toolbar_menu_nav_graph)

    override val bindingInflater: (LayoutInflater) -> ViewBinding =
        FragmentMenuProfileBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initButtonEvent()
        initNavigationEvent()
        initSharedFlowEvent()
    }

    override fun onResume() {
        super.onResume()
        profileViewModel.fetchUser()
        profileViewModel.fetchSetting()

        binding.switchNotification.setOnCheckedChangeListener { _, isChecked ->
            profileViewModel.saveSetting(Setting(isChecked))
            if (isChecked) notificationWorkManager.start(requireContext()) else notificationWorkManager.cancel(
                requireContext()
            )
        }
    }

    private fun initNavigationEvent() {
        binding.toolbar.setNavigationOnClickListener { findNavController().popBackStack() }
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().popBackStack()
                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun initButtonEvent() {
        binding.btnLogout.setOnClickListener {
            profileViewModel.logout()
        }

        binding.btnEditProfile.setOnClickListener {
            requireParentFragment().requireParentFragment().findNavController()
                .navigate(R.id.action_mainMenuViewBindingFragment_to_updateProfileViewBindingFragment)
        }

        binding.btnChangePassword.setOnClickListener {
            requireParentFragment().requireParentFragment().findNavController()
                .navigate(R.id.action_mainMenuViewBindingFragment_to_updatePasswordViewBindingFragment)
        }

        binding.btnContact.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO)
            intent.data = Uri.parse("mailto:")
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("budi@gmail.com"))
            intent.putExtra(Intent.EXTRA_SUBJECT, "Contact")
            intent.resolveActivity(requireContext().packageManager)?.let {
                startActivity(intent)
            }
        }

        val onProgressButtons = listOf(
            binding.btnAchievement,
            binding.btnPrivacyPolicy,
            binding.btnActivityHistory,
            binding.btnWorkoutProgress
        )
        onProgressButtons.forEach {
            it.setOnClickListener {
                requireParentFragment().requireParentFragment().findNavController()
                    .navigate(R.id.action_mainMenuViewBindingFragment_to_onProgressViewBindingFragment)
            }
        }
    }

    private fun initSharedFlowEvent() =
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    val callback = generateFetchApiCallback()
                    profileViewModel.userSharedFlow.collect {
                        when (it) {
                            is ApiResult.Success -> callback.successCallback(it)
                            is ApiResult.Loading -> callback.loadingCallback(it)
                            is ApiResult.Error -> callback.errorCallback(it)
                        }
                    }
                }

                launch {
                    val callback = generateLogoutApiCallback()
                    profileViewModel.defaultUserUpdateSharedFlow.collect {
                        when (it) {
                            is ApiResult.Success -> callback.successCallback(it)
                            is ApiResult.Loading -> callback.loadingCallback(it)
                            is ApiResult.Error -> callback.errorCallback(it)
                        }
                    }
                }

                launch {
                    profileViewModel.settingSharedFlow.collect {
                        if (it is ApiResult.Success) binding.switchNotification.isChecked =
                            it.response.isNotificationActive
                    }
                }

            }
        }

    private fun generateFetchApiCallback(): ApiCallback<User, ErrorResponse> =
        ApiCallback(
            successCallback = {
                DefaultApiCallbackUtility.successCallback()

                binding.txtViewName.text = it.response.name
                binding.txtViewAge.text = it.response.age.toString()

                "${it.response.height} ${getString(R.string.txt_view_cm)}".also { text ->
                    binding.txtViewHeight.text = text
                }

                "${it.response.weight} ${getString(R.string.txt_view_kg)}".also { text ->
                    binding.txtViewWeight.text = text
                }

                Glide.with(requireContext())
                    .load(it.response.picture)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.img_profile_placeholder)
                    .into(binding.image)
            },
            loadingCallback = {
                DefaultApiCallbackUtility.loadingCallback(parentFragmentManager)
            },
            errorCallback = {
                DefaultApiCallbackUtility.errorCallback(
                    requireContext(),
                    requireParentFragment().requireParentFragment().findNavController(),
                    it.response
                )
            }
        )

    private fun generateLogoutApiCallback(): ApiCallback<ResponseBody, ErrorResponse> =
        ApiCallback(
            successCallback = {
                DefaultApiCallbackUtility.successCallback()
                requireParentFragment().requireParentFragment().findNavController()
                    .navigate(R.id.welcome_nav_graph)
            },
            loadingCallback = {
                DefaultApiCallbackUtility.loadingCallback(parentFragmentManager)
            },
            errorCallback = {
                DefaultApiCallbackUtility.errorCallback(
                    requireContext(),
                    requireParentFragment().requireParentFragment().findNavController(),
                    it.response
                )
            }
        )
}