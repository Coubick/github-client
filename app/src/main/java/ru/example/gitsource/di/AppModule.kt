package ru.example.gitsource.di

import android.app.Application
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.components.SingletonComponent

@AndroidEntryPoint
@InstallIn(SingletonComponent::class)
object AppModule : Application()