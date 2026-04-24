package com.ElOuedUniv.maktaba.data.di

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage

object SupabaseModule {
    val client = createSupabaseClient(
        supabaseUrl = "https://YOUR_PROJECT_ID.supabase.co",
        supabaseKey = "YOUR_ANON_KEY"
    ) {
        install(Postgrest)
        install(Storage)
    }
    
}