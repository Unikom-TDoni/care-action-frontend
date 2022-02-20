package edu.rpl.careaction.feature.activity_tracker.domain.dto.request

import com.google.gson.annotations.SerializedName
import java.util.*

data class ActivityTrackerRequest(
    val date: Date,
    val checked: Boolean? = null,
    @SerializedName("id_activity") val id: Int? = null,
)