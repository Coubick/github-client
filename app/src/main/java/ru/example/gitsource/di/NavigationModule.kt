package ru.example.gitsource.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped
import ru.example.gitsource.domain.OAuthLauncher
import ru.example.gitsource.navigation.Navigator

@Module
@InstallIn(ActivityComponent::class)
internal class NavigationModule {
    @Provides
    @ActivityScoped
    fun provideNavigator(oAuthLauncher: OAuthLauncher): Navigator{
        return Navigator(oAuthLauncher)
    }
}