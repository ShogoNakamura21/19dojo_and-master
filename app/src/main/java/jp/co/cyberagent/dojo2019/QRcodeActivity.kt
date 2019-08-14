package jp.co.cyberagent.dojo2019

import android.content.Intent
import android.net.Uri
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
import kotlin.concurrent.thread


class QRcodeActivity : AppCompatActivity() {

    var db:AppDatabase? = null
    var user = User()//uri用に追加

    //supportActionBar?.title =  "プロフィール入力画面"//ツールバーのタイトルを変える


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrcode)

        db = AppDatabase.get(this)//databaseに入っている全てのデータを呼び出し


        if(intent.getStringExtra("Url") != null){
            val url1 = intent.getStringExtra("Url")//mainActivityのval...urlを読み込む

            val result = Uri.parse(url1)
            user.nameId = result.getQueryParameter("iam")
            user.gitId = result.getQueryParameter("gh")
            user.twiId= result.getQueryParameter("tw")
            val url2 = Uri.Builder().scheme("ca-tech").authority("dojo").path("/share").appendQueryParameter("iam",user.nameId).appendQueryParameter("tw",user.twiId).appendQueryParameter("gh",user.gitId)
//            "ca-tech://dojo/share?iam="+name+"&tw="+twi+"&gh="+git
            //↑ここまでuriのtestで追加

            //val url = intent.getStringExtra("Url")//mainActivityのvalを読み込む

            val size = 500

            try{
                val barcodeEncoder =  BarcodeEncoder() //QRコードをbitmapで作成
                val bitmap = barcodeEncoder.encodeBitmap(url2.toString(), BarcodeFormat.QR_CODE, size, size)//urlを変換するuriを使うためにurl→url.toString()へ
                val imageViewQrCode = findViewById<View>(R.id.imageView2) as ImageView //qrcode.xmlのimageView2にQRコードを表示
                imageViewQrCode.setImageBitmap(bitmap)

            }catch(e: WriterException){
                throw AndroidRuntimeException("Barcode Error.", e)
            }
        }else{//外部QRコードからの読み取りの時の動作
            val result = Uri.parse(intent.getStringExtra("Result"))
            user.nameId = result.getQueryParameter("iam")
            user.gitId = result.getQueryParameter("gh")
            user.twiId= result.getQueryParameter("tw")
            val url2 = Uri.Builder().scheme("ca-tech").authority("dojo").path("/share").appendQueryParameter("iam",user.nameId).appendQueryParameter("tw",user.twiId).appendQueryParameter("gh",user.gitId)
//            "ca-tech://dojo/share?iam="+name+"&tw="+twi+"&gh="+git
            //↑ここまでuriのtestで追加

            //val url = intent.getStringExtra("Url")//mainActivityのvalを読み込む

            val size = 500

            try{
                val barcodeEncoder =  BarcodeEncoder() //QRコードをbitmapで作成
                val bitmap = barcodeEncoder.encodeBitmap(url2.toString(), BarcodeFormat.QR_CODE, size, size)//urlを変換するuriを使うためにurl→url.toString()へ
                val imageViewQrCode = findViewById<View>(R.id.imageView2) as ImageView //qrcode.xmlのimageView2にQRコードを表示
                imageViewQrCode.setImageBitmap(bitmap)

                save(intent.getStringExtra("Result"))//☆外部QRから読み込んだデータをデータベースに保存している
                // これによりプロフィールを開くと読み取った外部QRコードのデータが表示される☆

                //QRcodeActivityを一瞬経由してデータを保存し、プロフィール一覧に飛ばしている
                val intent =Intent(this, ProfileActivity::class.java)
                startActivity(intent)

                //Toast.makeText(this, "プロフィール一覧に登録しました", Toast.LENGTH_LONG).show()

            }catch(e: WriterException){
                throw AndroidRuntimeException("Barcode Error.", e)
            }
        }
        //↓test
        //val url1 = intent.getStringExtra("Url")//mainActivityのval...urlを読み込む



        qr_camera.setOnClickListener {
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
                Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()

                save(result.contents.toString())
                val intent =Intent(this, ProfileActivity::class.java)
                intent.putExtra("Result",result.contents)//読み取った結果のデータresult.contentsをProfileActivityに引き渡す
                startActivity(intent)

            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


}
