package com.aybarsacar.cookpadversion2.di

import com.aybarsacar.cookpadversion2.utils.Constants
import com.aybarsacar.cookpadversion2.data.network.RecipesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


/**
 * to tell our app for the 3rd party component to be used as dependency
 * this is our dependency injection container
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

  @Singleton
  @Provides
  fun provideHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
      .readTimeout(15, TimeUnit.SECONDS)
      .connectTimeout(15, TimeUnit.SECONDS)
      .build()
  }


  @Singleton
  @Provides
  fun provideConverterFactory(): GsonConverterFactory {
    return GsonConverterFactory.create()
  }


  @Singleton
  @Provides
  fun provideRetrofitInstance(okHttpClient: OkHttpClient, gsonConverterFactory: GsonConverterFactory): Retrofit {

    return Retrofit.Builder()
      .baseUrl(Constants.BASE_URL)
      .client(okHttpClient)
      .addConverterFactory(gsonConverterFactory)
      .build()
  }


  @Singleton
  @Provides // since it is a 3rd party library
  fun provideApiService(retrofit: Retrofit): RecipesApi {
    return retrofit.create(RecipesApi::class.java)
  }

}