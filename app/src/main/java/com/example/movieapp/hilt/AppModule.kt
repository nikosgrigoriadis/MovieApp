package com.example.movieapp.hilt

import android.content.Context
import androidx.room.Room
import com.example.movieapp.database.AppDatabase
import com.example.movieapp.database.FavoriteMovieDao
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
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "movies_db"
        ).build()
    }

    @Provides
    fun provideFavoriteMovieDao(db: AppDatabase): FavoriteMovieDao {
        return db.favoriteMovieDao()
    }
}
