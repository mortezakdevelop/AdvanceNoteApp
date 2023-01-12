package com.example.advancenoteapplication.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.advancenoteapplication.database.dao.CategoryDao
import com.example.advancenoteapplication.database.dao.ItemDao
import com.example.advancenoteapplication.database.entity.CategoryEntity
import com.example.advancenoteapplication.database.entity.ItemEntity

@Database(entities = [ItemEntity::class, CategoryEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        private var appDatabase: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            //use singleton for create database
            if (appDatabase != null) {
                return appDatabase!!
            }

            appDatabase =
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "note-database"
                )
                    .addMigrations(MIGRATION1_2())
                    .build()
            return appDatabase!!
        }
    }

    class MIGRATION1_2() : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                "CREATE TABLE IF NOT EXISTS `category_entity` (`id` INTEGER NOT NULL, `name` TEXT NOT NULL, " +
                        "PRIMARY KEY(`id`))"
            )
        }

    }

    abstract fun itemDao(): ItemDao
    abstract fun categoryDao(): CategoryDao

}