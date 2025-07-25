package com.spacex.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.spacex.model.FalconInfo
import com.spacex.repository.FalconRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.launch

class FalconDetailViewModel(
    private val falconRepository: FalconRepository,
) : ViewModel() {

    var isBookmarked by mutableStateOf(false)

    fun isFalconBookMarked(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            isBookmarked = falconRepository.getRocket(id).isBookMark
        }
    }

    fun bookmarkArticle(falconInfo: FalconInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            if (!isBookmarked) {
                falconRepository.bookmark(falconInfo.id)
            } else {
                falconRepository.unBookmark(falconInfo.id)
            }
            isBookmarked = !isBookmarked
        }
    }

}