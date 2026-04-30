package com.ElOuedUniv.maktaba.data.repository

import com.ElOuedUniv.maktaba.domain.repository.AuthRepository
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.SessionStatus // 👇 استيراد جديد
import io.github.jan.supabase.gotrue.providers.Github
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val auth: Auth
) : AuthRepository {

    override suspend fun loginWithGithub() {
        withContext(Dispatchers.IO) {
            try {
                // 👇 عاد الكود نظيفاً وبسيطاً بدون أي إضافات
                auth.signInWith(provider = Github)
            } catch (e: Exception) {
                android.util.Log.e("MAKTABA_AUTH", "Error login: ${e.message}")
                throw e
            }
        }
    }

    override suspend fun logout() {
        withContext(Dispatchers.IO) {
            try {
                auth.signOut()
            } catch (e: Exception) {
                android.util.Log.e("MAKTABA_AUTH", "Error logout: ${e.message}")
            }
        }
    }

    // 👇 الإضافة الجديدة: نحول حالة جلسة Supabase إلى قيمة منطقية (True = مسجل، False = غير مسجل)
    override fun getUserSession(): Flow<Boolean> {
        return auth.sessionStatus.map { status ->
            status is SessionStatus.Authenticated
        }
    }
}