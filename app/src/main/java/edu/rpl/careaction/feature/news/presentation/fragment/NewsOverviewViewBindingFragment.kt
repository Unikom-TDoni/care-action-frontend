package edu.rpl.careaction.feature.news.presentation.fragment

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import edu.rpl.careaction.databinding.FragmentNewsOverviewBinding
import edu.rpl.careaction.module.ui.ViewBindingFragment

class NewsOverviewViewBindingFragment : ViewBindingFragment<FragmentNewsOverviewBinding>() {
    override val bindingInflater: (LayoutInflater) -> ViewBinding = FragmentNewsOverviewBinding::inflate

}