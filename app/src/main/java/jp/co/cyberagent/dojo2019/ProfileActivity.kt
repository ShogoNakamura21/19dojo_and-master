package jp.co.cyberagent.dojo2019

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_profile.*
import java.util.*
import kotlin.concurrent.thread
import android.webkit.WebView



class ProfileActivity : AppCompatActivity() {

    var db:AppDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val scanResult = intent.getStringExtra("Result")//QRcodeActivityで読み込んだQRのデータを受け取る


        db = AppDatabase.get(this)//databaseに入っている全てのデータを呼び出し

        val splitResult = scanResult.split("ca-tech://dojo/share?iam=", "&tw=", "&gh=")
        //editText.setText(splitResult[1])//splitResult[1]...name, [2]...twitter, [3]...github, データが分かれてるかの確認

        val scanName = splitResult[1]//スキャンした名前, String型
        val scanTwi = splitResult[2]//Twitterアカウント
        val scanGit = splitResult[3]//Githubアカウント

        //editText.setText(splitResult[1])//splitResult[1]...name, [2]...twitter, [3]...github, データが分かれてるかの確認

        // データモデルを作成
        val user = User()
        user.nameId = scanName
        user.twiId = scanTwi
        user.gitId = scanGit

        thread {
        // データを保存
            db?.userDao()?.insert(user)//name,twi,gitのデータが入っているuserをインサートする
            val test = db?.userDao()?.getAll()
            test?.get(0)?.nameId//0番目のnameを取ってくる
            //Log.v("testtest",test?.get(0)?.nameId)//リストをString型に変換して確認したい
            Handler(Looper.getMainLooper()).post({
                val array = ArrayList<RowModel>()

                test?.forEach {//testの回数分以下のコードを繰り返す
                    val data =RowModel()
                    data.nameId = it.nameId
                    data.gitId = it.gitId
                    data.twiId = it.twiId
                    array.add(data)
                }


                //RecyclerViewの参照を取得
                //val mRecyclerView = (RecyclerView) mView.findViewById(R.id.recycler_view)
                val mRecyclerView = findViewById<RecyclerView>(R.id.recycler_view)
                //レイアウトマネージャーを設定（ここで縦方向の標準リストであることを指定）
                mRecyclerView.setLayoutManager( LinearLayoutManager(this))

                val mAdapter = ViewAdapter(array, object : ViewAdapter.ListListener{
                    override fun onClickRow(tappedView: View, rowModel: RowModel) {
                        //アダプターがタップされたときの処理
//                        val webView = WebView(this)
//                        setContentView(webView)
//                        webView.loadUrl("https://twitter.com/")
//                        //twitter-> https://twitter.com/{account_name}
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                },this)
                mRecyclerView.setAdapter(mAdapter)

            })
        }





        //logでデータ取れてるかの確認

        //scanResultからName,Twitter,Gitのデータを取り出してRoomに保存したい→いったんurlを分解する必要あり？uriを使うらしい
        //Roomに保存、roomからの呼び出し、呼び出したデータの表示
        //bitballey
        //それを登録順で表示する


    }
}
