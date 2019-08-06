package jp.co.cyberagent.dojo2019

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AndroidRuntimeException
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_qrcode.*


class QRcodeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrcode)

        val url = intent.getStringExtra("Url")//mainActivityのvalを読み込む
        val size = 500

        try{
            val barcodeEncoder =  BarcodeEncoder() //QRコードをbitmapで作成
            val bitmap = barcodeEncoder.encodeBitmap(url, BarcodeFormat.QR_CODE, size, size)//urlを変換する
            val imageViewQrCode = findViewById<View>(R.id.imageView2) as ImageView //qrcode.xmlのimageView2にQRコードを表示
            imageViewQrCode.setImageBitmap(bitmap)

        }catch(e: WriterException){
            throw AndroidRuntimeException("Barcode Error.", e)
        }

        QrButton.setOnClickListener {
            IntentIntegrator(this).initiateScan();
        }

        button_profile.setOnClickListener {//プロフィール一覧に遷移
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        button_regist.setOnClickListener { //登録画面へ遷移
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()

                val intent =Intent(this, ProfileActivity::class.java)
                intent.putExtra("Result",result.contents)//読み取った結果のデータresult.contentsをProfileActivityに引き渡す
                startActivity(intent)

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }



}
