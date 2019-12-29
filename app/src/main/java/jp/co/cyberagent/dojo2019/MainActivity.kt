package jp.co.cyberagent.dojo2019

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() , QRcodeFragment.Listner {

    override fun move_to_qr(url: String) {

        //Qrcodeフラグメントにurlを渡す
            val bundle = Bundle()
            // Key/Pairの形で値をセットする
            bundle.putString("Url", url)
            // Fragmentに値をセットする
            val fragment = QRcodeFragment()
            fragment.setArguments(bundle)

            val fragmentManager = this.getSupportFragmentManager()
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit()


    }


    var db:AppDatabase? = null
    var user = User()//uri用に追加


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        setSupportActionBar(toolbar)
//        supportActionBar?.setTitle("test")

        //MainActivityを開くと登録画面が開くようにする
        val fragment = MainFragment()
        val fragmentManager = this.getSupportFragmentManager()
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()

//        val dataStore: SharedPreferences = getSharedPreferences("DataStore", Context.MODE_PRIVATE)//sharedpreのインスタンス生成
//
//        val button_save = findViewById<Button>(R.id.button_save)
//        val button_qr = findViewById<Button>(R.id.button_create_qr)
//        val editText = findViewById<EditText>(R.id.editTextName)

//        val name  = dataStore.getString("InputName", null)//Sharedpreferenceに保存した名前を取り出す
//        editTextName.setText(name)//editTextNameにセットする
//
//        val twi = dataStore.getString("InputTwi", null)
//        editTextTwi.setText(twi)
//
//        val git = dataStore.getString("InputGit", null)
//        editTextGit.setText(git)

//        button_save.setOnClickListener {//保存ボタンが押されたときの処理
//
//            val name_data = editTextName.text.toString()//名前情報
//            val git_data = editTextGit.text.toString()//git情報
//            val twi_data = editTextTwi.text.toString()//twitter情報
//
//            val editor = dataStore.edit()
//            editor.putString("InputName",name_data)
//            editor.putString("InputGit",git_data)
//            editor.putString("InputTwi", twi_data)
//            editor.apply()
//
//            Toast.makeText(this, "保存",Toast.LENGTH_LONG).show()
//        }


//        button_qr.setOnClickListener {
//
//            if (editTextGit.text.toString() == "") {//textGitの内容が空だった場合の処理
//
//                val builder = AlertDialog.Builder(this)//アラートを出す
//                builder.setMessage("GitHubアカウント名は必須です")
//                    .setPositiveButton("閉じる") { dialog, id -> }
//                builder.show()
//            }else {
//
//
//                val name_data = editText.text.toString()//名前情報
//                val git_data = editTextGit.text.toString()//git情報
//                val twi_data = editTextTwi.text.toString()//twitter情報
//
//                val intent = Intent(this, QRcodeActivity::class.java)
//                val url =
//                    "ca-tech://dojo/share?iam=" + name_data + "&tw=" + twi_data + "&gh=" + git_data  //nameとgitとtwitterのデータをまとめたもの
//                intent.putExtra("Url", url)//urlをQRcodeActivityに引き渡す
//                startActivity(intent)
//            }
//        }


        //下のツールバー
        //プロフィール登録のフラグメントを表示
        button_regist.setOnClickListener{
            val fragment = MainFragment()
            val fragmentManager = this.getSupportFragmentManager()
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit()
        }


        button_profile.setOnClickListener {//プロフィール一覧のフラグメントを表示
            val fragment = ProfileFragment()
            val fragmentManager = this.getSupportFragmentManager()
            val fragmentTransaction = fragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.container, fragment)
                                .addToBackStack(null)
                                .commit()
//            val intent = Intent(this, ProfileActivity::class.java)
//            startActivity(intent)
        }


        //button_myQR.setOnClickListener{//QRcodeのフラグメントを表示

//            //Qrcodeフラグメントにurlを渡す
//            val bundle = Bundle()
//            // Key/Pairの形で値をセットする
//            bundle.putString("Url", url)
//            // Fragmentに値をセットする
//            val fragment = QRcodeFragment()
//            fragment.setArguments(bundle)


//            val fragment = QRcodeFragment()
//            val fragmentManager = this.getSupportFragmentManager()
//            val fragmentTransaction = fragmentManager.beginTransaction()
//            fragmentTransaction.replace(R.id.container, fragment)
//                .addToBackStack(null)
//                .commit()
        //}


        qr_camera.setOnClickListener {//QRコードを読み取るカメラを起動
            IntentIntegrator(this).initiateScan();
        }

    }

    fun save(user_data:String){//読み取ったデータを保存する処理
        db = AppDatabase.get(this)//databaseに入っている全てのデータを呼び出し

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                //Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
                Toast.makeText(this, "QRを読み取りました" , Toast.LENGTH_LONG).show()


                save(result.contents.toString())
                val intent =Intent(this, MainActivity::class.java)
                intent.putExtra("Result",result.contents)//読み取った結果のデータresult.contentsをProfileActivityに引き渡す
                startActivity(intent)

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

}
