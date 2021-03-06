package cn.foretree.db.star

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlin.math.E

/**
 * Created by silen on 06/09/2018
 */
@Entity(tableName = "edit_data")
data class EditData(
        @field:SerializedName("user_id")
        @field: ColumnInfo(name = "user_id")
        val userId: Long,
        @field:SerializedName("edit_type")
        @field: ColumnInfo(name = "edit_type")
        val editType: Int,
        @field:SerializedName("edit_data")
        @field: ColumnInfo(name = "edit_data")
        val editData: String,
        @field: PrimaryKey(autoGenerate = true)
        val id: Long = 0,
        val timestamp: Long = System.currentTimeMillis()
) {
    companion object {
        fun create(userId: Long, type: Int, data: String): EditData = EditData(userId, type, data)
        fun empty(userId: Long): EditData = EditData(userId, -1, "")
    }
}