package jp.co.cyberagent.dojo2019

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val scanResult = intent.getStringExtra("Result")//QRcodeActivityで読み込んだQRのデータ

        //val uri01: Uri = Uri.parse(scanResult)

        val splitResult = scanResult.split("ca-tech://dojo/share?iam=", "&tw=", "&gh=")
        editText.setText(splitResult[3])//splitResult[1]...name, [2]...twitter, [3]...github, データが分かれてるかの確認

        val scanName = splitResult[1]//スキャンした名前, String型
        val scanTwi = splitResult[2]//Twitterアカウント
        val scanGit = splitResult[3]//Githunアカウント




        //url = "ca-tech://dojo/share?iam="+name_data +"&tw="+ twi_data +"&gh="+ git_data


        //scanResultからName,Twitter,Gitのデータを取り出してRoomに保存したい→いったんurlを分解する必要あり？uriを使うらしい
        //Roomに保存したデータをリスト形式で表示したい
        //それをabcdの順で表示する


    }
}
