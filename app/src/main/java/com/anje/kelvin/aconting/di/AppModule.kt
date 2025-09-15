package com.anje.kelvin.aconting.di

import android.content.Context
import android.content.SharedPreferences
import com.anje.kelvin.aconting.data.repository.AuthRepository
import com.anje.kelvin.aconting.data.repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    }
    
    @Provides
    @Singleton
    fun provideAuthRepository(
        @ApplicationContext context: Context,
        userRepository: UserRepository
    ): AuthRepository {
        return AuthRepository(context, userRepository)
    }
}
