package jp.co.cyberagent.dojo2019

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_profile.*
import java.util.*
import kotlin.concurrent.thread

class ProfileActivity : AppCompatActivity() {

    var db:AppDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val scanResult = intent.getStringExtra("Result")//QRcodeActivityで読み込んだQRのデータ


        db = AppDatabase.get(this)//databaseに入っている全てのデータを呼び出し

        val splitResult = scanResult.split("ca-tech://dojo/share?iam=", "&tw=", "&gh=")
        editText.setText(splitResult[1])//splitResult[1]...name, [2]...twitter, [3]...github, データが分かれてるかの確認

        val scanName = splitResult[1]//スキャンした名前, String型
        val scanTwi = splitResult[2]//Twitterアカウント
        val scanGit = splitResult[3]//Githubアカウント

        editText.setText(splitResult[1])//splitResult[1]...name, [2]...twitter, [3]...github, データが分かれてるかの確認

        // データモデルを作成
        val user = User()
        user.nameId = scanName
        user.twiId = scanTwi
        user.gitId = scanGit

        thread {
        // データを保存
            db?.userDao()?.insert(user)//name,twi,gitのデータが入っているuserをインサートする
            Log.v("testtest",db?.userDao()?.getAll().toString())//リストをString型に変換して確認したい
        }



        //logでデータ取れてるかの確認

        //scanResultからName,Twitter,Gitのデータを取り出してRoomに保存したい→いったんurlを分解する必要あり？uriを使うらしい
        //Roomに保存、roomからの呼び出し、呼び出したデータの表示
        //bitballey
        //それを登録順で表示する


    }
}
