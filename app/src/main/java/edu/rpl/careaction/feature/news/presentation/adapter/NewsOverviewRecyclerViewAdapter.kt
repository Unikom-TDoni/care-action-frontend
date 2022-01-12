package edu.rpl.careaction.feature.news.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.rpl.careaction.databinding.ItemNewsOverviewBinding
import edu.rpl.careaction.feature.news.domain.entity.NewsOverview

class NewsOverviewRecyclerViewAdapter: RecyclerView.Adapter<NewsOverviewRecyclerViewAdapter.ViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<NewsOverview>() {
        override fun areItemsTheSame(oldItem: NewsOverview, newItem: NewsOverview): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: NewsOverview, newItem: NewsOverview): Boolean =
            oldItem == newItem
    }

    private val newsOverviewsDiffer = AsyncListDiffer(this, differCallback)

    fun submitList(newsOverviews: List<NewsOverview>) =
        newsOverviewsDiffer.submitList(newsOverviews)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemNewsOverviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val newsOverview = newsOverviewsDiffer.currentList[position]
        holder.binding.text.text = newsOverview.tittle
        Glide.with(holder.binding.image).load(newsOverview.thumbnail).into(holder.binding.image)
        holder.binding.button.setOnClickListener {
            newsOverview.id
        }
    }

    override fun getItemCount(): Int =
        newsOverviewsDiffer.currentList.size

    inner class ViewHolder(val binding: ItemNewsOverviewBinding) :
        RecyclerView.ViewHolder(binding.root)
}