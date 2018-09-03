package cn.foretree.db

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * SerializedName是序列化的名字,ColumnInfo是表每一列的名字
 * Created by silen on 03/09/2018
 */
@Entity(tableName = "user")
data class User(
        val name: String,
        val sex: Int,
        @PrimaryKey(autoGenerate = true)
        val id: Long = 0,
        val createAt: Long = System.currentTimeMillis()
)

//@Entity(tableName = "user")
//data class User(
//        //@field: SerializedName("user_name") 另一种写法
//        @SerializedName("user_name") val name: String,
//        @SerializedName("user_sex") val sex: Int,
//        @PrimaryKey(autoGenerate = true)
//        @SerializedName("id") val id: Long = 0
//)

//@Entity(tableName = "user")
//data class User(
//        @ColumnInfo(name = "user_name") val name: String,
//        @ColumnInfo(name = "user_sex") val sex: Int,
//        @PrimaryKey(autoGenerate = true)
//        @ColumnInfo(name = "id") val id: Long = 0
//)