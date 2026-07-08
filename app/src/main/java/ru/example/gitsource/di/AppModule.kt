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
import ru.example.gitsource.data.auth.AuthRepositoryImpl
import ru.example.gitsource.data.auth.AuthTokenInterceptor
import ru.example.gitsource.data.auth.TokenManager
import ru.example.gitsource.data.local.DataConstants.PREFERENCES_NAME
import ru.example.gitsource.data.local.LocalDataStore
import ru.example.gitsource.data.network.JsonAcceptInterceptor
import ru.example.gitsource.data.network.NetworkClient
import ru.example.gitsource.data.network.NetworkConstants.BASE_API_URL
import ru.example.gitsource.domain.AuthRepository
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
        networkClient: NetworkClient,
        tokenManager: TokenManager
    ): AuthRepository {
        return AuthRepositoryImpl(networkClient, tokenManager)
    }

    @Provides
    @Singleton
    fun provideTokenManager(localDataStore: LocalDataStore): TokenManager {
        return TokenManager(localDataStore)
    }
}