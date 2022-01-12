package edu.rpl.careaction.feature.support.welcome

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import edu.rpl.careaction.databinding.FragmentWelcomeBinding
import edu.rpl.careaction.module.ui.ViewBindingFragment

class WelcomeViewBindingFragment : ViewBindingFragment<FragmentWelcomeBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding = FragmentWelcomeBinding::inflate

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initButtonEvent()
        initViewPagerComponent()
    }

    private fun initViewPagerComponent() {
        binding.viewPager.apply {
            isUserInputEnabled = false
            adapter = object : FragmentStateAdapter(this@WelcomeViewBindingFragment) {
                override fun getItemCount(): Int = 4
                override fun createFragment(position: Int): Fragment =
                    WelcomeContentViewBindingFragment.newInstance(position)
            }
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    binding.progressCircular.progress += 34
                    super.onPageSelected(position)
                }
            })
        }
    }

    private fun initButtonEvent() =
        binding.btnNextContent.setOnClickListener {
            binding.viewPager.currentItem++
        }
}