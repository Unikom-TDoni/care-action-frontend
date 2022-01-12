package edu.rpl.careaction.feature.news.presentation.adapter

import edu.rpl.careaction.R
import android.view.ViewGroup
import com.bumptech.glide.Glide
import android.view.LayoutInflater
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import edu.rpl.careaction.databinding.ItemNewsCategoryBinding
import edu.rpl.careaction.feature.news.domain.entity.NewsCategory

class NewsCategoryRecyclerViewAdapter :
    RecyclerView.Adapter<NewsCategoryRecyclerViewAdapter.ViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<NewsCategory>() {
        override fun areItemsTheSame(oldItem: NewsCategory, newItem: NewsCategory): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: NewsCategory, newItem: NewsCategory): Boolean =
            oldItem == newItem
    }

    private val newsCategoriesDiffer = AsyncListDiffer(this, differCallback)

    fun submitList(newscategories: List<NewsCategory>) =
        newsCategoriesDiffer.submitList(newscategories)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemNewsCategoryBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val newsCategory = newsCategoriesDiffer.currentList[position]
        holder.binding.cardView.setOnClickListener {
            it.findNavController().navigate(R.id.welcomeViewBindingFragment)
        }
        Glide.with(holder.binding.icon).load(newsCategory.icon).into(holder.binding.icon)
        holder.binding.tittle.text = newsCategory.name
    }

    override fun getItemCount(): Int =
        newsCategoriesDiffer.currentList.size

    inner class ViewHolder(val binding: ItemNewsCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)
}