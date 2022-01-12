package edu.rpl.careaction.feature.motivation.data.dao

import javax.inject.Inject
import edu.rpl.careaction.feature.motivation.domain.entity.Motivation

class MotivationLocalDataSource @Inject constructor() {
    fun load() =
        when ((0..10).random()) {
            1 -> Motivation("Never give up")
            else -> Motivation("Never surrender")
        }
}