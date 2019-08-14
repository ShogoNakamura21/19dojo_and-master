package jp.co.cyberagent.dojo2019

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class Deeplink : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deeplink)

        val intent = Intent(this, MainActivity::class.java)//元はQrcodeActivity
        intent.putExtra("Result", getIntent().dataString)//読み取ったデータのこと
        Toast.makeText(this, "QRを読み取りました", Toast.LENGTH_LONG).show()
        startActivity(intent)
    }
}
