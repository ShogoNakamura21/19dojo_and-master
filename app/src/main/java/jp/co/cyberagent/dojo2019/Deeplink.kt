package jp.co.cyberagent.dojo2019

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlin.concurrent.thread

class Deeplink : AppCompatActivity() {


    var db:AppDatabase? = null
    var user = User()//uri用に追加


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deeplink)

        //追加
        db = AppDatabase.get(this)//databaseに入っている全てのデータを呼び出し
        save(getIntent().dataString)

        val intent = Intent(this, MainActivity::class.java)//元はQrcodeActivity
        intent.putExtra("Result", getIntent().dataString)//読み取ったデータのこと→MainActivityに送る
        Toast.makeText(this, "QRを読み取りました", Toast.LENGTH_LONG).show()
        startActivity(intent)
    }

    //追加
    fun save(user_data:String?){//読み取ったデータを保存する処理

        //uri用に追加urlを変換
        val result = Uri.parse(user_data)
        user.nameId = result.getQueryParameter("iam")
        user.gitId = result.getQueryParameter("gh")
        user.twiId= result.getQueryParameter("tw")


        //val splitResult = user_data.split("ca-tech://dojo/share?iam=", "&tw=", "&gh=")

        val scanName = user.nameId//スキャンした名前, String型
        val scanTwi = user.twiId //Twitterアカウント
        val scanGit = user.gitId//Githubアカウント

        //editText.setText(splitResult[1])//splitResult[1]...name, [2]...twitter, [3]...github, データが分かれてるかの確認

        // データモデルを作成
        val user = User()
        user.nameId = scanName
        user.twiId = scanTwi
        user.gitId = scanGit

        thread {
            // データを保存
            db?.userDao()?.insert(user)//name,twi,gitのデータが入っているuserをインサートする
        }
    }
}
