package com.ElOuedUniv.maktaba.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ElOuedUniv.maktaba.domain.repository.AuthRepository
import com.ElOuedUniv.maktaba.domain.usecase.LoginWithGithubUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val loginWithGithubUseCase: LoginWithGithubUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    val isLoggedIn: Flow<Boolean> = authRepository.getUserSession()

    fun onLoginWithGithubClick() {
        viewModelScope.launch {
            try {
                // هذا السطر يطلب من Supabase فتح نافذة المتصفح لـ GitHub
                loginWithGithubUseCase()
            } catch (e: Exception) {
                android.util.Log.e("MAKTABA_UI", "Login Failed: ${e.message}")
            }
        }
    }
}