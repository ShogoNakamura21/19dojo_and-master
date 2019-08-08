package jp.co.cyberagent.dojo2019

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(User::class), version = 1) // Kotlin 1.2からは arrayOf(User::class)の代わりに[User::class]と書ける
abstract class AppDatabase : RoomDatabase() {
    // DAOを取得する。
    abstract fun userDao(): UserDao

    companion object {
        private var instance: AppDatabase? = null

        //↓データベースの作成部分
        fun get(context: Context): AppDatabase {
            val instance = instance
            if (instance != null) {
                return instance
            }
            val instance_new = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "user").build()//userというデータベースを呼び出してる
            this.instance = instance_new
            return instance_new
        }
    }
}