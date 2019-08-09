package jp.co.cyberagent.dojo2019

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.AndroidRuntimeException
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_qrcode.*
import java.util.ArrayList
import kotlin.concurrent.thread


class QRcodeActivity : AppCompatActivity() {

    var db:AppDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrcode)

        db = AppDatabase.get(this)//databaseに入っている全てのデータを呼び出し

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




    fun save(user_data:String){//読み取ったデータを保存する処理

        val splitResult = user_data.split("ca-tech://dojo/share?iam=", "&tw=", "&gh=")

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
            // val test = db?.userDao()?.getAll()
            //test?.get(0)?.nameId//0番目のnameを取ってくる
            //Log.v("testtest",test?.get(0)?.nameId)//リストをString型に変換して確認したい
//            Handler(Looper.getMainLooper()).post({
//                    val array = ArrayList<RowModel>()
//
//                    test?.forEach {//testの回数分以下のコードを繰り返す
//                        val data =RowModel()
//                        data.nameId = it.nameId
//                        data.gitId = it.gitId
//                        data.twiId = it.twiId
//                        array.add(data)
//                    }
//

            //})
        }
    }




    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()

                save(result.contents.toString())
                val intent =Intent(this, ProfileActivity::class.java)
                //intent.putExtra("Result",result.contents)//読み取った結果のデータresult.contentsをProfileActivityに引き渡す
                startActivity(intent)

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


}
