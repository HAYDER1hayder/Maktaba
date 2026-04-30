package com.ElOuedUniv.maktaba.domain.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun loginWithGithub()
    suspend fun logout()

    // 👇 الإضافة الجديدة: دالة تراقب هل المستخدم مسجل دخوله أم لا
    fun getUserSession(): Flow<Boolean>
}