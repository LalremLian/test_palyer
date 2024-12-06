package com.example.di

import com.example.network.ApiClient
import com.example.network.api.ApiInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
//    @Singleton
//    @Provides
//    fun provideApplicationContext(@ApplicationContext context: Context): Context {
//        return context.applicationContext
//    }

    @Singleton
    @Provides
    fun provideInterfaceSecond(): ApiInterface {
        return ApiClient.getRetrofit().create(ApiInterface::class.java)
    }
}
