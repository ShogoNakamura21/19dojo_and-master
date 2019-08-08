package jp.co.cyberagent.dojo2019

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dataStore: SharedPreferences = getSharedPreferences("DataStore", Context.MODE_PRIVATE)//sharedpreのインスタンス生成

        val button_save = findViewById<Button>(R.id.button_save)
        val button_qr = findViewById<Button>(R.id.button_qr)
        val editText = findViewById<EditText>(R.id.editTextName)

        val name  = dataStore.getString("InputName", null)//Sharedpreferenceに保存した名前を取り出す
        editTextName.setText(name)//editTextNameにセットする

        val twi = dataStore.getString("InputTwi", null)
        editTextTwi.setText(twi)

        val git = dataStore.getString("InputGit", null)
        editTextGit.setText(git)

        button_save.setOnClickListener {//保存ボタンが押されたときの処理
            val name_data = editTextName.text.toString()//名前情報
            val git_data = editTextGit.text.toString()//git情報
            val twi_data = editTextTwi.text.toString()//twitter情報

            val editor = dataStore.edit()
            editor.putString("InputName",name_data)
            editor.putString("InputGit",git_data)
            editor.putString("InputTwi", twi_data)
            editor.apply()

            Toast.makeText(this, "保存",Toast.LENGTH_LONG).show()
        }


        button_qr.setOnClickListener {
            val name_data = editText.text.toString()//名前情報
            val git_data = editTextGit.text.toString()//git情報
            val twi_data = editTextTwi.text.toString()//twitter情報

            val intent =Intent(this, QRcodeActivity::class.java)
            val url = "ca-tech://dojo/share?iam="+name_data +"&tw="+ twi_data +"&gh="+ git_data  //nameとgitとtwitterのデータをまとめたもの
            intent.putExtra("Url",url)//urlをQRcodeActivityに引き渡す
            startActivity(intent)
        }

        button_profile.setOnClickListener {//プロフィール一覧に遷移
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }


    }
}
