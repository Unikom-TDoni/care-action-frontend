package edu.rpl.careaction.feature.support.error

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class ErrorParcelable(
    val throwable: Throwable
) : Parcelable