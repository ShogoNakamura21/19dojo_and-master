package jp.co.cyberagent.dojo2019

import android.content.Intent
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
import android.widget.Toast
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_profile.button_profile
import kotlinx.android.synthetic.main.activity_profile.button_regist
import kotlinx.android.synthetic.main.activity_qrcode.*


class ProfileActivity : AppCompatActivity() {

    var db:AppDatabase? = null
    var userData = mutableListOf<User>()
    lateinit var adapter: ViewAdapter//今後入ることを前提に定義することができる

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val userList :MutableList<RowModel> = userData.map { user ->
            RowModel(user.nameId, user.twiId, user.gitId)
        }.toMutableList()//Userをrowmodelに　中身なし

        adapter = ViewAdapter(userList, object : ViewAdapter.ListListener{
            override fun onClickRow(tappedView: View, rowModel: RowModel) {
                //アダプターがタップされたときの処理
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        },this)

        db = AppDatabase.get(this)//databaseに入っている全てのデータを呼び出し
        thread {
            userData = db?.userDao()?.getAll()!!.toMutableList()//中が(User)で既にデータは入っているこれを(Rowmodelにする)...!!はnull許容型をそうじゃなくするもの
            val userList :MutableList<RowModel> = userData.map { user ->
                RowModel(user.nameId, user.twiId, user.gitId)
            }.toMutableList()
            adapter.addUser(userList)//adapterにデータが入っている<rowmodel>の配列を渡す
            adapter.notifyDataSetChanged()//データが来たことをアダプターに教える
        }

        val mRecyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        //レイアウトマネージャーを設定（ここで縦方向の標準リストであることを指定）
        mRecyclerView.setLayoutManager( LinearLayoutManager(this))
        mRecyclerView.setAdapter(adapter)


//        QrButton.setOnClickListener {
//            IntentIntegrator(this).initiateScan();
//        }




        button_regist.setOnClickListener { //登録画面へ遷移
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


    }
}

        //scanResultからName,Twitter,Gitのデータを取り出してRoomに保存したい→いったんurlを分解する必要あり？uriを使うらしい
        //Roomに保存、roomからの呼び出し、呼び出したデータの表示
        //bitballey
        //それを登録順で表示する
