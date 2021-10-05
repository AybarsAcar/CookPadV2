package com.aybarsacar.cookpadversion2.di

import android.content.Context
import androidx.room.Room
import com.aybarsacar.cookpadversion2.data.database.RecipesDatabase
import com.aybarsacar.cookpadversion2.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


/**
 * to tell our app for the 3rd party component to be used as dependency
 * this is our dependency injection container for our Database
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

  @Singleton
  @Provides
  fun provideDatabase(@ApplicationContext context: Context) =
    Room.databaseBuilder(context, RecipesDatabase::class.java, Constants.ROOM_DATABASE_NAME).build()


  @Singleton
  @Provides
  fun provideDao(database: RecipesDatabase) = database.recipesDao()
}