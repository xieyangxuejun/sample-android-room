package cn.foretree.db

import android.annotation.SuppressLint
import android.app.Application
import android.arch.persistence.room.Room
import android.content.Context

/**
 * Created by silen on 03/09/2018
 */
@SuppressLint("StaticFieldLeak")
class AppDatabaseManager {

    private var mContext: Context? = null
    private var mAppDatabase: AppDatabase? = null

    companion object {
        @Volatile
        private var mInstance: AppDatabaseManager? = null

        fun getInstance(): AppDatabaseManager {
            if (mInstance == null) {
                synchronized(AppDatabaseManager::class.java) {
                    if (mInstance == null) {
                        mInstance = AppDatabaseManager()
                    }
                }
            }
            return mInstance!!
        }
    }

    fun init(app: Application) {
        mInstance?.run {
            if (mContext == null) {
                mContext = app.applicationContext
            }
            Room.inMemoryDatabaseBuilder()
            mAppDatabase = Room.databaseBuilder(
                    mContext!!, AppDatabase::class.java, databaseName
            ).build()
        }
    }


    fun getUserDao(): UserDao = mAppDatabase?.getUserDao()!!
}