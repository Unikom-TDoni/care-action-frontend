package edu.rpl.careaction.feature.news.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.rpl.careaction.R
import edu.rpl.careaction.databinding.ItemNewsOverviewBinding
import edu.rpl.careaction.feature.news.domain.entity.NewsOverview

class NewsOverviewRecyclerViewAdapter(
    val onOverviewNewsClicked: (id: Int) -> Unit
) :
    RecyclerView.Adapter<NewsOverviewRecyclerViewAdapter.ViewHolder>(), Filterable {

    private lateinit var filteredItems: List<NewsOverview>

    private val differCallback = object : DiffUtil.ItemCallback<NewsOverview>() {
        override fun areItemsTheSame(oldItem: NewsOverview, newItem: NewsOverview): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: NewsOverview, newItem: NewsOverview): Boolean =
            oldItem == newItem
    }

    private val newsOverviewsDiffer = AsyncListDiffer(this, differCallback)

    fun submitList(newsOverviews: List<NewsOverview>) =
        newsOverviewsDiffer.submitList(newsOverviews)

    fun setFilteredItems() {
        filteredItems = newsOverviewsDiffer.currentList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemNewsOverviewBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val newsOverview = newsOverviewsDiffer.currentList[position]
        holder.binding.text.text = newsOverview.tittle
        Glide.with(holder.binding.image).load(newsOverview.thumbnail)
            .placeholder(R.drawable.img_placeholder).into(holder.binding.image)
        holder.binding.button.setOnClickListener {
            onOverviewNewsClicked(newsOverview.id)
        }
    }

    override fun getItemCount(): Int =
        newsOverviewsDiffer.currentList.size

    override fun getFilter(): Filter =
        object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults =
                FilterResults().apply {
                    values = constraint?.let {
                        filteredItems.filter {
                            it.tittle.contains(constraint, true)
                        }
                    }
                }
            
            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                results?.let { if (it.values != null) submitList(it.values as List<NewsOverview>) }
            }
        }

    inner class ViewHolder(val binding: ItemNewsOverviewBinding) :
        RecyclerView.ViewHolder(binding.root)
}