package cn.foretree.db

import android.arch.persistence.room.*

/**
 * 发布操作类
 * Created by silen on 03/09/2018
 */
@Dao
interface UserDao {
    //多个数据
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg user: User): List<Long>

    @Delete
    fun delete(vararg user: User): Int

    @Update
    fun update(vararg user: User): Int

    @Query("SELECT * FROM user")
    fun queryAll(): List<User>

    @Query("SELECT * FROM user WHERE id IN (:userIDs)")
    fun queryByUserIds(vararg userIDs: Long): List<User>

    @Query("SELECT * FROM user WHERE id = :id")
    fun queryById(id: Long): User
}