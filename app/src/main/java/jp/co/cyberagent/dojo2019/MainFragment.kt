package jp.co.cyberagent.dojo2019


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dataStore: SharedPreferences? = this.activity?.getSharedPreferences("DataStore", Context.MODE_PRIVATE)//sharedpreのインスタンス生成

        //this.activity?をつけるーactivity上でflagmentを使用するから
        //this activity.contextのこと→view.contextに変える
        //findViewById→view.findViewByIdにした

        val button_save = view.findViewById<Button>(R.id.button_save)
        val button_create_qr = view.findViewById<Button>(R.id.button_create_qr)
        //val editText = findViewById<EditText>(R.id.editTextName)

        val name  = dataStore?.getString("InputName", null)//Sharedpreferenceに保存した名前を取り出す
        editTextName.setText(name)//editTextNameにセットする

        val twi = dataStore?.getString("InputTwi", null)
        editTextTwi.setText(twi)

        val git = dataStore?.getString("InputGit", null)
        editTextGit.setText(git)

        button_save.setOnClickListener {//保存ボタンが押されたときの処理

            val name_data = editTextName.text.toString()//名前情報
            val git_data = editTextGit.text.toString()//git情報
            val twi_data = editTextTwi.text.toString()//twitter情報

            val editor = dataStore?.edit()
            editor?.putString("InputName",name_data)
            editor?.putString("InputGit",git_data)
            editor?.putString("InputTwi", twi_data)
            editor?.apply()

            Toast.makeText(view.context, "保存", Toast.LENGTH_LONG).show()
        }


        button_save.setOnClickListener {//保存ボタンが押されたときの処理

            val name_data = editTextName.text.toString()//名前情報
            val git_data = editTextGit.text.toString()//git情報
            val twi_data = editTextTwi.text.toString()//twitter情報

            val editor = dataStore?.edit()//dataStore→dataStore?へ　＋　editor→editor?へ
            editor?.putString("InputName",name_data)
            editor?.putString("InputGit",git_data)
            editor?.putString("InputTwi", twi_data)
            editor?.apply()

            Toast.makeText(view.context, "保存",Toast.LENGTH_LONG).show()//this→view.contextへ
        }





        button_create_qr.setOnClickListener {

            if (editTextGit.text.toString() == "") {//textGitの内容が空だった場合の処理

                val builder = AlertDialog.Builder(view.context)//アラートを出す
                builder.setMessage("GitHubアカウント名は必須です")
                    .setPositiveButton("閉じる") { dialog, id -> }
                builder.show()
            }else {


                val name_data = editTextName.text.toString()//名前情報
                val git_data = editTextGit.text.toString()//git情報
                val twi_data = editTextTwi.text.toString()//twitter情報

                val intent = Intent(view.context, MainActivity::class.java)//元々QrcodeActivity
                val url =
                    "ca-tech://dojo/share?iam=" + name_data + "&tw=" + twi_data + "&gh=" + git_data  //nameとgitとtwitterのデータをまとめたもの
                intent.putExtra("Url", url)//urlをQRcodeActivityに引き渡す(→QRFragmentに渡す)


                Toast.makeText(view.context, "QRを作成しました",Toast.LENGTH_LONG).show()//this→view.contextへ
                startActivity(intent)



            }
        }




    }


}
