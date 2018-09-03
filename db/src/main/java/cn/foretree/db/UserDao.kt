package cn.foretree.db

import android.arch.persistence.room.*

/**
 * 发布操作类
 * Created by silen on 03/09/2018
 */
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg model: User)

    @Delete
    fun delete(model: User)

    @Update
    fun update(model: User)

    @Query("SELECT * FROM user")
    fun queryAll(): List<User>

    @Query("SELECT * FROM user WHERE id IN (:userIDs)")
    fun queryByUserIds(vararg userIDs: Long): List<User>

    @Query("SELECT * FROM user WHERE id = :id")
    fun queryById(id: Long): User
}