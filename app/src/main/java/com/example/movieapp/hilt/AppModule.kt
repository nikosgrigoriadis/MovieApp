package com.example.movieapp.hilt

import android.content.Context
import androidx.room.Room
import com.example.movieapp.database.AppDatabase
import com.example.movieapp.database.FavoriteMovieDao
import com.example.movieapp.database.MovieScheduleDao
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
        )
            .fallbackToDestructiveMigration() //delete the db if change schema (change also to DatabaseProvider)
            .build()
    }

    @Provides
    fun provideFavoriteMovieDao(db: AppDatabase): FavoriteMovieDao {
        return db.favoriteMovieDao()
    }

    @Provides
    fun provideScheduledMovieDao(db: AppDatabase): MovieScheduleDao {
        return db.movieScheduleDao()
    }
}
