package edu.rpl.careaction.feature.activity_tracker.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import edu.rpl.careaction.databinding.ItemActivityTrackerBinding
import edu.rpl.careaction.feature.activity_tracker.domain.entity.ActivityTracker

class ActivityTrackerRecyclerViewAdapter :
    RecyclerView.Adapter<ActivityTrackerRecyclerViewAdapter.ViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<ActivityTracker>() {
        override fun areItemsTheSame(
            oldItem: ActivityTracker,
            newItem: ActivityTracker
        ): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: ActivityTracker,
            newItem: ActivityTracker
        ): Boolean =
            oldItem == newItem
    }

    private val activityTrackerDiffer = AsyncListDiffer(this, differCallback)
    var onTaskIsChecked: (activityTrackers: Collection<ActivityTracker>) -> Unit =
        {}

    fun submitList(activityTrackers: List<ActivityTracker>) =
        activityTrackerDiffer.submitList(activityTrackers)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemActivityTrackerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val activityTracker = activityTrackerDiffer.currentList[position]
        holder.binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
            activityTracker.isChecked = isChecked
            onTaskIsChecked.invoke(activityTrackerDiffer.currentList)
        }

        holder.binding.tittle.text = activityTracker.tittle
        Glide.with(holder.binding.image).load(activityTracker.icon).into(holder.binding.image)
        holder.binding.checkBox.isChecked = activityTracker.isChecked
    }

    override fun getItemCount(): Int = activityTrackerDiffer.currentList.size

    inner class ViewHolder(val binding: ItemActivityTrackerBinding) :
        RecyclerView.ViewHolder(binding.root)
}