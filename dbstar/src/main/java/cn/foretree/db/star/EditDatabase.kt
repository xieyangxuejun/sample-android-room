package cn.foretree.db.star

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

/**
 * Created by silen on 03/09/2018
 */
@Database(entities = arrayOf(
        EditData::class
), version = EDIT_DATABASE_VERSION)
abstract class EditDatabase : RoomDatabase() {
    abstract fun getEditDataDao(): EditDataDao
}