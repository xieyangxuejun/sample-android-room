package cn.foretree.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

/**
 * Created by silen on 03/09/2018
 */
@Database(entities = arrayOf(
        User::class
), version = databaseVersion)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getUserDao(): UserDao
}