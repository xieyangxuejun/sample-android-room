package cn.foretree.db.star

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Created by silen on 06/09/2018
 */
@Entity(tableName = "edit_data")
data class EditData(
        @field: SerializedName("user_id")
        val userId: Long,
        @field: SerializedName("edit_type")
        val editType: Int,
        @field: SerializedName("edit_data")
        val editData: String,
        @field: PrimaryKey(autoGenerate = true)
        val id: Long = 0,
        val timestamp: Long = System.currentTimeMillis()
)