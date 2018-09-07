package cn.foretree.db

import android.arch.persistence.room.*

/**
 * 发布操作类
 * Created by silen on 03/09/2018
 */
@Dao
interface RepoDao {
    //多个数据
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg repo: Repo): List<Long>

    @Delete
    fun delete(vararg repo: Repo): Int

    @Update
    fun update(vararg repo: Repo): Int

    @Query("SELECT * FROM repo")
    fun queryAll(): List<Repo>

    @Query("SELECT * FROM repo WHERE id IN (:repoIds)")
    fun queryByUserIds(vararg repoIds: Long): List<Repo>

    @Query("SELECT * FROM Repo WHERE id = :id")
    fun queryById(id: Long): Repo
}