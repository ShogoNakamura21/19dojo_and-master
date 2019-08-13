package jp.co.cyberagent.dojo2019

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Deeplink : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deeplink)

        val intent = Intent(this, QRcodeActivity::class.java)
        intent.putExtra("Result", getIntent().dataString)//読み取ったデータのこと
        startActivity(intent)
    }
}
