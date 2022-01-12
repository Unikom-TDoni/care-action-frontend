package edu.rpl.careaction.feature.task.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import edu.rpl.careaction.databinding.ItemHealthyLifestyleRoutineBinding
import edu.rpl.careaction.feature.task.domain.entity.HealthyLifestyleRoutines

class HealthyLifestyleRoutineRecyclerViewAdapter :
    RecyclerView.Adapter<HealthyLifestyleRoutineRecyclerViewAdapter.ViewHolder>() {

    private val differCallback = object : DiffUtil.ItemCallback<HealthyLifestyleRoutines>() {
        override fun areItemsTheSame(
            oldItem: HealthyLifestyleRoutines,
            newItem: HealthyLifestyleRoutines
        ): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: HealthyLifestyleRoutines,
            newItem: HealthyLifestyleRoutines
        ): Boolean =
            oldItem == newItem
    }

    private val healthyLifestyleRoutinesDiffer = AsyncListDiffer(this, differCallback)
    var onTaskIsChecked: (healthyLifestyleRoutines: Collection<HealthyLifestyleRoutines>) -> Unit =
        {}

    fun submitList(healthyLifestyleRoutines: List<HealthyLifestyleRoutines>) =
        healthyLifestyleRoutinesDiffer.submitList(healthyLifestyleRoutines)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(
            ItemHealthyLifestyleRoutineBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val healthyLifecycleRoutine = healthyLifestyleRoutinesDiffer.currentList[position]
        holder.binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
            healthyLifecycleRoutine.isChecked = isChecked
            onTaskIsChecked.invoke(healthyLifestyleRoutinesDiffer.currentList)
        }

        holder.binding.tittle.text = healthyLifecycleRoutine.tittle
        holder.binding.image.setImageDrawable(
            ContextCompat.getDrawable(
                holder.binding.root.context,
                healthyLifecycleRoutine.icon
            )
        )
        holder.binding.checkBox.isChecked = healthyLifecycleRoutine.isChecked
    }

    override fun getItemCount(): Int = healthyLifestyleRoutinesDiffer.currentList.size

    inner class ViewHolder(val binding: ItemHealthyLifestyleRoutineBinding) :
        RecyclerView.ViewHolder(binding.root)
}