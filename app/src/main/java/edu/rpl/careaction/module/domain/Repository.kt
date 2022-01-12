package edu.rpl.careaction.module.domain

import kotlinx.coroutines.CoroutineDispatcher

abstract class Repository {
    protected abstract val defaultDispatcher: CoroutineDispatcher
}