package jp.co.cyberagent.dojo2019

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class User {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0

    @ColumnInfo(name = "name_Id")
    var nameId: String? = null//読み取った名前を保存する場所

    @ColumnInfo(name = "git_Id")
    var gitId: String? = null

    @ColumnInfo(name = "twi_Id")
    var twiId: String? = null

//    override fun toString(): String {
//        return nameId.toString()//orEmpty(), ここをnameIdにすると名前、twiIdにするとtwiになる
//    }

}