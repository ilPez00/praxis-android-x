package com.praxis.app.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.auth.Auth
import io.ktor.client.engine.okhttp.OkHttp
import javax.inject.Singleton

private const val SUPABASE_URL = "https://kuyzjjbeiawnnkkrjsda.supabase.co"
private const val SUPABASE_ANON_KEY = "sb_publishable_gI3zbQ0L6rt7IMe8XgW2UQ_Hzzxadtd"

@Module
@InstallIn(SingletonComponent::class)
object SupabaseModule {

    @Provides
    @Singleton
    fun provideSupabaseClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = SUPABASE_URL,
            supabaseKey = SUPABASE_ANON_KEY,
        ) {
            httpEngine = OkHttp.create()
            install(Auth)
        }
    }
}
