package edu.rpl.careaction.feature.page.error

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class ErrorParcelable(
    val throwable: Throwable
) : Parcelable