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
import ru.example.gitsource.data.auth.OAuthLauncherImpl
import ru.example.gitsource.data.auth.AuthRepositoryImpl
import ru.example.gitsource.data.auth.AuthTokenInterceptor
import ru.example.gitsource.data.auth.TokenManager
import ru.example.gitsource.data.local.DataConstants.PREFERENCES_NAME
import ru.example.gitsource.data.local.LocalDataStore
import ru.example.gitsource.data.network.JsonAcceptInterceptor
import ru.example.gitsource.data.network.NetworkClient
import ru.example.gitsource.data.network.NetworkConstants.BASE_API_URL
import ru.example.gitsource.data.network.api.GitHubApi
import ru.example.gitsource.data.network.api.GitHubOAuthApi
import ru.example.gitsource.data.popular.RepositorySearchServiceImpl
import ru.example.gitsource.domain.auth.AuthRepository
import ru.example.gitsource.domain.OAuthLauncher
import ru.example.gitsource.domain.popular.RepositorySearchService
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthTokenInterceptor,
        jsonAcceptInterceptor: JsonAcceptInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(jsonAcceptInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_API_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideNetworkClient(retrofit: Retrofit): NetworkClient {
        return NetworkClient(retrofit)
    }

    @Provides
    @Singleton
    fun provideGitHubOAuthService(networkClient: NetworkClient): GitHubOAuthApi {
        return networkClient.create(GitHubOAuthApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGitHubApi(networkClient: NetworkClient): GitHubApi {
        return networkClient.create(GitHubApi::class.java)
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
        gitHubOAuthApi: GitHubOAuthApi,
        networkClient: NetworkClient,
        tokenManager: TokenManager
    ): AuthRepository {
        return AuthRepositoryImpl(
            gitHubOAuthApi = gitHubOAuthApi,
            networkClient = networkClient,
            tokenManager = tokenManager
        )
    }

    @Provides
    @Singleton
    fun provideTokenManager(localDataStore: LocalDataStore): TokenManager {
        return TokenManager(localDataStore)
    }

    @Provides
    @Singleton
    fun provideRepositorySearchService(gitHubApi: GitHubApi, networkClient: NetworkClient): RepositorySearchService {
        return RepositorySearchServiceImpl(
            githubApi = gitHubApi,
            networkClient = networkClient
        )
    }
}