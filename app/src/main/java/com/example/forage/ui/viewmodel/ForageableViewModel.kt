/*
 * Copyright (C) 2021 The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.forage.ui.viewmodel

import androidx.lifecycle.*
import com.example.forage.data.ForageableDao
import com.example.forage.model.Forageable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

/**
 * Shared [ViewModel] to provide data to the [ForageableListFragment], [ForageableDetailFragment],
 * and [AddForageableFragment] and allow for interaction the the [ForageableDao]
 */

class ForageableViewModel(
    private val forageableDao: ForageableDao
) : ViewModel() {


    val forageables = liveData {
        forageableDao.getForageable().collectLatest {
            emit(it)
        }
    }


    fun getForageable(id : Long) = liveData{
        forageableDao.getForageable(id).collect {
            emit(it)
        }
    }


    fun addForageable(
        name: String,
        address: String,
        inSeason: Boolean,
        notes: String
    ) {
        if(isValidEntry(name, address)) {
            val forageable = Forageable(
                name = name,
                address = address,
                inSeason = inSeason,
                notes = notes
            )

            viewModelScope.launch {
                forageableDao.insert(forageable)
            }
        }

        if (name.isBlank()) throw IllegalArgumentException("Insert a valid name")
        if (address.isBlank()) throw IllegalArgumentException("Insert a valid addres")
    }

    fun updateForageable(
        id: Long,
        name: String,
        address: String,
        inSeason: Boolean,
        notes: String
    ) {
        if(isValidEntry(name, address)) {
            val forageable = Forageable(
                id = id,
                name = name,
                address = address,
                inSeason = inSeason,
                notes = notes
            )
            viewModelScope.launch(Dispatchers.IO) {
                forageableDao.update(forageable)
            }
        }

        if (name.isBlank()) throw IllegalArgumentException("Insert a valid name")
        if (address.isBlank()) throw IllegalArgumentException("Insert a valid addres")
    }

    fun deleteForageable(forageable: Forageable) {
        viewModelScope.launch(Dispatchers.IO) {
            forageableDao.delete(forageable)
        }
    }

    fun isValidEntry(name: String, address: String): Boolean {
        return name.isNotBlank() && address.isNotBlank()
    }
}

