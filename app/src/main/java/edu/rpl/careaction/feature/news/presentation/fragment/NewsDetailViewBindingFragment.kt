package edu.rpl.careaction.feature.news.presentation.fragment

import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import edu.rpl.careaction.databinding.FragmentNewsDetailBinding
import edu.rpl.careaction.module.ui.ViewBindingFragment

class NewsDetailViewBindingFragment : ViewBindingFragment<FragmentNewsDetailBinding>() {

    override val bindingInflater: (LayoutInflater) -> ViewBinding = FragmentNewsDetailBinding::inflate


}