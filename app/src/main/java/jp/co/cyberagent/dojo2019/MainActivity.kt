package jp.co.cyberagent.dojo2019

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AndroidRuntimeException
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*
import com.google.zxing.WriterException
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val button = findViewById<Button>(R.id.button)
        val button_qr = findViewById<Button>(R.id.button_qr)
        val editText = findViewById<EditText>(R.id.editText)

        button.setOnClickListener {


            val name_data = editText.text.toString()//名前情報
            val git_data = editText2.text.toString()//git情報
            val twi_data = editText3.text.toString()//twitter情報

            val data = "ca-tech://dojo/share?iam="+name_data +"&tw"+ git_data +"&gh"+ twi_data  //nameとgitとtwitterのデータをまとめたもの

            //ca-tech://dojo/share?iam=Keita%20Kagurazaka&tw=kkagurazaka&gh=k-kagurazaka
            //intent.putExtra("qr_data",data)//dataを別のActivityに引き渡す
            val size = 500//QRコード画像の大きさ(pixel)

            try{
                val barcodeEncoder =  BarcodeEncoder() //QRコードをbitmapで作成
                val bitmap = barcodeEncoder.encodeBitmap(data, BarcodeFormat.QR_CODE, size, size)
                val imageViewQrCode = findViewById<View>(R.id.imageView) as ImageView

                // MainActivityを起動するための、intentを初期化する
                val intent: Intent = Intent(this, MainActivity::class.java)

                // intentにbitmapをセットする
                intent.putExtra("bitmap", bitmap)

                // MainActivityを起動する
                //startActivity(intent)

                imageViewQrCode.setImageBitmap(bitmap)
            }catch(e: WriterException){
                throw AndroidRuntimeException("Barcode Error.", e)
            }


            //startActivity(intent)

        }


        button_qr.setOnClickListener {
            val intent =Intent(this, QRcodeActivity::class.java)
            startActivity(intent)

        }

        button_profile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }




    }
}
