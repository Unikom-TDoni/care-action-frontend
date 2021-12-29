package edu.rpl.careaction.core.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.rpl.careaction.core.domain.dao.ApplicationSharedPreferences
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val applicationSharedPreferences: ApplicationSharedPreferences
) : ViewModel() {

}