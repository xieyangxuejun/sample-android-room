package cn.foretree.db.star

import android.arch.paging.DataSource
import android.arch.persistence.db.SupportSQLiteQuery
import android.arch.persistence.room.*
import io.reactivex.Flowable

/**
 * Created by silen on 06/09/2018
 */
@Dao
interface EditDataDao {
    //paging query
    @Query("SELECT * FROM edit_data WHERE id > :id ORDER BY timestamp DESC LIMIT 20")
    fun rxQuery(id: Long): Flowable<List<EditData>>

    @Query("SELECT * FROM edit_data WHERE id > :id ORDER BY timestamp DESC LIMIT :limit")
    fun rxQuery(id: Long, limit: Int): Flowable<List<EditData>>

    @Query("SELECT * FROM edit_data WHERE id BETWEEN :minId AND :maxId ORDER BY timestamp DESC")
    fun rxQuery(minId: Long, maxId: Long): Flowable<List<EditData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg data: EditData): List<Long>

    @Delete
    fun delete(vararg data: EditData): Int

    @Query("DELETE FROM edit_data WHERE id IN (:ids)")
    fun delete(vararg ids: Long): Int

    @Query("DELETE FROM edit_data")
    fun deleteAll(): Int

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(vararg data: EditData): Int

    @Query("SELECT * FROM edit_data ORDER BY timestamp DESC")
    fun queryAll(): List<EditData>

    @Query("SELECT * FROM edit_data ORDER BY timestamp DESC")
    fun rxQueryAll(): Flowable<List<EditData>>

    @Query("SELECT * FROM edit_data ORDER BY timestamp DESC")
    fun queryAllToDS(): DataSource.Factory<Int, EditData>

    @Query("SELECT * FROM edit_data WHERE id > :beginId ORDER BY timestamp DESC LIMIT 20")
    fun pagingQuery(beginId: Long): DataSource.Factory<Int, EditData>

    @Query("SELECT * FROM edit_data WHERE user_id IN (:userIDs) ORDER BY timestamp DESC")
    fun queryByUserIds(vararg userIDs: Long): List<EditData>

    @Query("SELECT * FROM edit_data WHERE user_id IN (:userIDs) ORDER BY timestamp DESC")
    fun rxQueryByUserIds(vararg userIDs: Long): Flowable<List<EditData>>

    @Query("SELECT * FROM edit_data WHERE user_id = :userID ORDER BY timestamp DESC")
    fun queryByUserIds(userID: Long): List<EditData>

    @Query("SELECT * FROM edit_data WHERE user_id = :userID ORDER BY timestamp DESC")
    fun rxQueryByUserIds(userID: Long): Flowable<List<EditData>>

    @Query("SELECT * FROM edit_data WHERE id = :id")
    fun queryById(id: Long): EditData

    @Query("SELECT * FROM edit_data WHERE id = :id")
    fun rxQueryById(id: Long): Flowable<EditData>

    @RawQuery
    fun getDataList(sqLiteQuery: SupportSQLiteQuery): List<EditData>

    @RawQuery
    fun getDataViaQuery(sqLiteQuery: SupportSQLiteQuery): EditData
}