package cn.foretree.db

import android.arch.persistence.room.*
import com.google.gson.annotations.SerializedName

/**
 * Created by silen on 04/09/2018
 */
@Entity(
        indices = [
            Index("id"),
            (Index("owner_login"))]
        //primaryKeys = ["name", "owner_login"]
)
data class Repo(
        @field:SerializedName("name")
        val name: String,
        @field:SerializedName("description")
        val description: String?,
        @field:SerializedName("owner")
        @field:Embedded(prefix = "owner_")
        val owner: Owner = Owner("xieyangxuejun", "http://"),
        @field:PrimaryKey(autoGenerate = true)
        val id: Long = 0
) {

    data class Owner(
            @field:SerializedName("login")
            val login: String,
            @field:SerializedName("url")
            val url: String?
    )
}