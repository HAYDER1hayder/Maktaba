package com.ElOuedUniv.maktaba.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth // غيرنا GoTrue إلى Auth
import io.github.jan.supabase.gotrue.auth // غيرنا gotrue إلى auth
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import javax.inject.Singleton // تأكد من استخدام javax

@Module
@InstallIn(SingletonComponent::class) // هذا السطر هو الذي يربط الملف بـ Hilt
object SupabaseModule {

    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = "https://yheyntmfwzkhzxwdodkr.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InloZXludG1md3praHp4d2RvZGtyIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzcwNTcxNDMsImV4cCI6MjA5MjYzMzE0M30.xsl-mokq_BuqC6qef4fgEdlP4wIPvi46AjUnZVFMV2I"
        ) {
            install(Postgrest)
            install(Storage)

            // 👇 الإضافة الجديدة الخاصة بتهيئة تسجيل الدخول (Auth)
            // 👇 هنا التعديل
            install(Auth) {
                scheme = "com.eloueduniv.maktaba"
                host = "login-callback"
            }
        }
    }

    @Provides
    @Singleton
    fun providePostgrest(client: SupabaseClient): Postgrest = client.postgrest

    @Provides
    @Singleton
    fun provideStorage(client: SupabaseClient): Storage = client.storage

    @Provides
    @Singleton
    fun provideAuth(client: SupabaseClient): Auth = client.auth
}