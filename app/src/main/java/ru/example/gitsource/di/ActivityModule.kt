package ru.example.gitsource.di

import android.app.Activity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import ru.example.gitsource.data.auth.OAuthLauncherImpl
import ru.example.gitsource.domain.OAuthLauncher

@Module
@InstallIn(ActivityComponent::class)
internal object ActivityModule {
    @Provides
    @ActivityScoped
    fun provideOAuthLauncher(activity: Activity): OAuthLauncher {
        return OAuthLauncherImpl(activity)
    }
}