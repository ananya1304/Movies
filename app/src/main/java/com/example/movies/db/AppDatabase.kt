package com.example.movies.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.example.movies.model.Movie

@Database(entities = [Movie::class], version = AppDatabase.VERSION)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getMovieDao(): MovieDao

    companion object{
        private const val DB_NAME = "movie.db"
        const val VERSION = 1


        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase = INSTANCE
            ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context.applicationContext).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context, AppDatabase::class.java,
                DB_NAME
            )
                .fallbackToDestructiveMigration()
                .build()
    }
}
