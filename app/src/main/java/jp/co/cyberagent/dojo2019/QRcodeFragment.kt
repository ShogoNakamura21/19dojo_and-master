package jp.co.cyberagent.dojo2019


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.AndroidRuntimeException
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import kotlin.concurrent.thread


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class QRcodeFragment : Fragment() {


    var db:AppDatabase? = null
    var user = User()//uri用に追加

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_qrcode, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        db = AppDatabase.get(view.context)//databaseに入っている全てのデータを呼び出し

        //Activityからflagmentで変わったもの
        //this→view.context
        //intent→this.activity?.intent?
        //findBiewById→view.findViewById



        if(this.activity?.intent?.getStringExtra("Url") != null){
            val url1 = this.activity?.intent?.getStringExtra("Url")//mainActivityのval...urlを読み込む

            val result = Uri.parse(url1)
            user.nameId = result.getQueryParameter("iam")
            user.gitId = result.getQueryParameter("gh")
            user.twiId= result.getQueryParameter("tw")
            val url2 = Uri.Builder().scheme("ca-tech").authority("dojo").path("/share").appendQueryParameter("iam",user.nameId).appendQueryParameter("tw",user.twiId).appendQueryParameter("gh",user.gitId)
//            "ca-tech://dojo/share?iam="+name+"&tw="+twi+"&gh="+git
            //↑ここまでuriのtestで追加

            //val url = intent.getStringExtra("Url")//mainActivityのvalを読み込む

            val size = 500
            //view.findView
            try{
                val barcodeEncoder =  BarcodeEncoder() //QRコードをbitmapで作成
                val bitmap = barcodeEncoder.encodeBitmap(url2.toString(), BarcodeFormat.QR_CODE, size, size)//urlを変換するuriを使うためにurl→url.toString()へ
                val imageViewQrCode = view.findViewById<View>(R.id.imageView2) as ImageView //qrcode.xmlのimageView2にQRコードを表示
                imageViewQrCode.setImageBitmap(bitmap)

            }catch(e: WriterException){
                throw AndroidRuntimeException("Barcode Error.", e)
            }
        }else{//外部QRコードからの読み取りの時の動作
            val result = Uri.parse(this.activity?.intent?.getStringExtra("Result"))
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
                val imageViewQrCode = view.findViewById<View>(R.id.imageView2) as ImageView //qrcode.xmlのimageView2にQRコードを表示
                imageViewQrCode.setImageBitmap(bitmap)

                save(this.activity?.intent!!.getStringExtra("Result"))//☆外部QRから読み込んだデータをデータベースに保存している
                // これによりプロフィールを開くと読み取った外部QRコードのデータが表示される☆
                //→ここflagmentに変えてから怪しい

                //QRcodeActivityを一瞬経由してデータを保存し、プロフィール一覧に飛ばしている
                val intent = Intent(view.context, ProfileActivity::class.java)
                startActivity(intent)

                //Toast.makeText(this, "プロフィール一覧に登録しました", Toast.LENGTH_LONG).show()

            }catch(e: WriterException){
                throw AndroidRuntimeException("Barcode Error.", e)
            }
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





}
