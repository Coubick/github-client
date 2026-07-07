package ru.example.gitsource.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.example.gitsource.data.api.AuthRepository
import ru.example.gitsource.data.api.AuthRepositoryImpl
import ru.example.gitsource.data.api.GitHubApi
import ru.example.gitsource.data.api.GitHubOAuthService
import ru.example.gitsource.data.auth.AuthTokenInterceptor
import ru.example.gitsource.data.auth.TokenManager
import ru.example.gitsource.data.common.DataConstants.PREFERENCES_NAME
import ru.example.gitsource.data.common.NetworkConstants.BASE_API_URL
import ru.example.gitsource.data.common.NetworkConstants.BASE_URL
import ru.example.gitsource.data.local.LocalDataStore
import ru.example.gitsource.data.network.GitHubApiAcceptInterceptor
import ru.example.gitsource.data.network.JsonAcceptInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object AppModule {
    @Provides
    @Singleton
    @Named("oauth")
    fun provideOAuthOkHttpClient(jsonAcceptInterceptor: JsonAcceptInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(jsonAcceptInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @Named("oauth")
    fun provideOAuthRetrofit(@Named("oauth") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("rest_api")
    fun provideRestApiOkHttpClient(
        authInterceptor: AuthTokenInterceptor,
        gitHubApiAcceptInterceptor: GitHubApiAcceptInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(gitHubApiAcceptInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    @Named("rest_api")
    fun provideRestApiRetrofit(@Named("rest_api") okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_API_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    @Provides
    @Singleton
    fun provideGitHubOAuth(@Named("oauth") retrofit: Retrofit): GitHubOAuthService {
        return retrofit.create(GitHubOAuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideGitHubApi(@Named("rest_api") retrofit: Retrofit): GitHubApi {
        return retrofit.create(GitHubApi::class.java)
    }

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { context.preferencesDataStoreFile(PREFERENCES_NAME) }
        )
    }

    @Singleton
    @Provides
    fun provideLocalDataStore(dataStore: DataStore<Preferences>): LocalDataStore {
        return LocalDataStore(dataStore)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        oAuthService: GitHubOAuthService,
        tokenManager: TokenManager
    ): AuthRepository {
        return AuthRepositoryImpl(oAuthService, tokenManager)
    }

    @Provides
    @Singleton
    fun provideTokenManager(localDataStore: LocalDataStore): TokenManager {
        return TokenManager(localDataStore)
    }
}