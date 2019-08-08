package jp.co.cyberagent.dojo2019

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {//コピペ

    // シンプルなSELECTクエリ
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

//    // メソッドの引数をSQLのパラメーターにマッピングするには :引数名 と書く
//    @Query("SELECT * FROM user WHERE uid IN (:userIds)")
//    fun loadAllaByIds(vararg userIds: Int): List<User>
//
//    // 複数の引数も渡せる
//    @Query("SELECT * FROM user WHERE name_Id LIKE :first AND git_Id LIKE :last LIMIT 1")
//    fun findByName(first: String, last: String): User
//
//    //テスト用
//    @Query("SELECT * FROM user WHERE name_Id LIKE :first LIMIT 1")
//    fun findName(first: String): User

    // データモデルのクラスを引数に渡すことで、データの作成ができる。
    @Insert
    fun insert(user: User)

//    // 可変長引数にしたり
//    @Insert
//    fun insertAll(vararg users: User)

//    // Listで渡したりもできる
//    @Insert
//    fun insertAll(users: List<User>)

//    // データモデルのクラスを引数に渡すことで、データの削除ができる。主キーでデータを検索して削除する場合。
//    @Delete
//    fun delete(user: User)

    // 複雑な条件で削除したい場合は、@Queryを使ってSQLを書く
    //@Query("DELETE FROM user WHERE age < :age")
    //fun deleteYoungerThan(age: Int)
}