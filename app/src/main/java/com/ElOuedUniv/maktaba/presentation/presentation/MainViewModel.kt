package com.ElOuedUniv.maktaba.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ElOuedUniv.maktaba.domain.repository.AuthRepository
import com.ElOuedUniv.maktaba.presentation.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    // المتغير الذي سيحتفظ بمسار البداية (يبدأ فارغاً حتى يتم التحقق)
    var startDestination by mutableStateOf<String?>(null)
        private set

    init {
        checkUserSession()
    }

    private fun checkUserSession() {
        viewModelScope.launch {
            // نراقب الجلسة باستمرار
            authRepository.getUserSession().collect { isLoggedIn ->
                // إذا كان مسجلاً -> اذهب للمكتبة، إذا لم يكن -> اذهب للترحيب
                startDestination = if (isLoggedIn) {
                    Screen.BookList.route
                } else {
                    Screen.Onboarding.route
                }
            }
        }
    }
}