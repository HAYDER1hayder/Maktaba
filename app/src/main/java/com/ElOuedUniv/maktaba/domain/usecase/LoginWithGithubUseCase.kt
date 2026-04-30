package com.ElOuedUniv.maktaba.domain.usecase

import com.ElOuedUniv.maktaba.domain.repository.AuthRepository
import javax.inject.Inject

class LoginWithGithubUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() {
        authRepository.loginWithGithub()
    }
}