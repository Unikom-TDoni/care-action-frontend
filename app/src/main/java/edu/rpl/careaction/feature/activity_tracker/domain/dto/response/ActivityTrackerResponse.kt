package edu.rpl.careaction.feature.activity_tracker.domain.dto.response

import com.google.gson.annotations.SerializedName
import edu.rpl.careaction.feature.activity_tracker.domain.entity.ActivityTracker

data class ActivityTrackerResponse(
    val data: Collection<Data>
) {
    data class Data(
        val id: Int,
        val icon: String,
        val checked: Int,
        @SerializedName("activity_name") val name: String
    )

    fun toActivityTrackers(): Collection<ActivityTracker> =
        data.map { ActivityTracker(it.id, it.icon, it.name, it.checked == 1) }
}