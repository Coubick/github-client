package ru.example.gitsource.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import ru.example.gitsource.data.auth.OAuthLauncherImpl
import ru.example.gitsource.domain.OAuthLauncher

@Module
@InstallIn(ActivityComponent::class)
internal abstract class ActivityModule {
    @Binds
    @ActivityScoped
    abstract fun provideOAuthLauncher(impl: OAuthLauncherImpl): OAuthLauncher
}