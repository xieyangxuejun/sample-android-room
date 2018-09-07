package cn.foretree.db.star

import android.annotation.SuppressLint
import android.app.Application
import android.arch.paging.LivePagedListBuilder
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.paging.LimitOffsetDataSource
import android.content.Context
import android.support.v4.util.ArrayMap

/**
 * Created by silen on 03/09/2018
 */
@SuppressLint("StaticFieldLeak")
class AppDatabaseManager {

    private var mContext: Context? = null
    private var mDbMap: ArrayMap<String, RoomDatabase> = ArrayMap()

    companion object {
        @Volatile
        private var mInstance: AppDatabaseManager? = null

        fun getInstance(): AppDatabaseManager =
                mInstance ?: synchronized(this) {
                    mInstance ?: AppDatabaseManager().also { mInstance = it }
                }
    }

    fun init(app: Application) {
        if (mContext == null) {
            mContext = app.applicationContext
        }
        build(EDIT_DATABASE_NAME, EditDatabase::class.java)
    }

    fun build(databaseName: String, clazz: Class<out RoomDatabase>) {
        mInstance?.run {
            if (mContext != null) {
                val database = Room.databaseBuilder(
                        mContext!!, clazz, databaseName
                ).build()
                mDbMap[databaseName] = database
            } else {
                throw IllegalStateException("context is null or don't init")
            }
        }
    }

    private fun getEditDatabase(): EditDatabase? {
        val db = mDbMap[EDIT_DATABASE_NAME]
        return if (db != null && db is EditDatabase) db else null
    }

    fun getDatabase(databaseName: String) = mDbMap[databaseName]

    fun getEditDataDao(): EditDataDao = getEditDatabase()?.getEditDataDao()!!
}