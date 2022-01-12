package edu.rpl.careaction.core.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import edu.rpl.careaction.R
import edu.rpl.careaction.feature.user.presentation.UserViewModel
import edu.rpl.careaction.module.api.ApiResult
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ApplicationActivity : AppCompatActivity() {

    private val applicationViewModel: ApplicationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initSharedFlowEvent()
        applicationViewModel.fetchUser()

        installSplashScreen().apply { setKeepVisibleCondition { !applicationViewModel.isDataReady } }
        setContentView(R.layout.activity_application)
    }

    private fun initSharedFlowEvent() =
        lifecycleScope.launch {
            applicationViewModel.userSharedFlow.collect {
                when (it) {
                    is ApiResult.Success -> {
                        if (it.response.isHaveCompleteProfile())
                            findNavController(R.id.fragment_container_view).navigate(R.id.action_to_mainMenuViewBindingFragment)
                        else
                            findNavController(R.id.fragment_container_view).navigate(R.id.action_welcomeViewBindingFragment_to_registerProfileViewBindingFragment)
                    }
                    else -> applicationViewModel.hideSplashScreenWhenActive()
                }
                this.cancel()
            }
        }
}