package com.example.forage.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.forage.data.ForageableDao
import java.lang.IllegalArgumentException

class ForageableViewModelFactory(
    private val forageableDao: ForageableDao
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ForageableViewModel::class.java))
            @Suppress("UNCHECKED_CAST")
            return ForageableViewModel(forageableDao) as T
        throw IllegalArgumentException("Unknown view model class")
    }
}