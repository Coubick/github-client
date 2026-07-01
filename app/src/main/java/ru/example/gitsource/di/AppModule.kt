package ru.example.gitsource.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.example.gitsource.data.api.GitHubOAuthService
import ru.example.gitsource.common.NetworkConstants.BASE_URL
import javax.inject.Singleton
import okhttp3.OkHttpClient
import ru.example.gitsource.common.DataConstants.SOURCE_FILE_NAME
import ru.example.gitsource.data.local.LocalDataStore
import ru.example.gitsource.network.JsonAcceptInterceptor
import java.util.concurrent.TimeUnit

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(SOURCE_FILE_NAME)
@Module
@InstallIn(SingletonComponent::class)
internal object AppModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(JsonAcceptInterceptor())
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideGitHubApi(retrofit: Retrofit): GitHubOAuthService {
        return retrofit.create(GitHubOAuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context) : DataStore<Preferences> {
        return context.dataStore
    }

    @Provides
    @Singleton
    fun provideLocalDataStore(dataStore: DataStore<Preferences>): LocalDataStore {
        return LocalDataStore(dataStore)
    }
}