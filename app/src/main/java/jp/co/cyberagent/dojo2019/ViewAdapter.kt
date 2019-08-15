package jp.co.cyberagent.dojo2019

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import kotlin.concurrent.thread


class ViewAdapter(private val list: MutableList<RowModel>, private val listener: ListListener,val context: Context) ://contextが使えないから持ってきた（profileactivityから）
    RecyclerView.Adapter<ViewHolder>() {//generics

    var db:AppDatabase? = null
    var user = User()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.d("Adapter", "onCreateViewHolder")
        val rowView: View = LayoutInflater.from(parent.context).inflate(R.layout.profile_cell, parent, false)
        return ViewHolder(rowView)//activity_profileに紐づけ
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("Adapter", "onBindViewHolder")
        holder.nameView.text = list[position].nameId
        holder.gitView.text = list[position].gitId
        holder.twiView.text = list[position].twiId
        db = AppDatabase.get(context)//databaseに入っている全てのデータを呼び出し this→context

        //twitterのアイコンを押したときの処理
        holder.twiButton.setOnClickListener {//アイコンがタッチされたときの処理
            Toast.makeText(context, "twitterに移動します", Toast.LENGTH_LONG).show()//ちゃんと押されているか確認

            val twiUrl = "https://twitter.com/"+list[position].twiId

            //webViewActivityに遷移
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra("Url",twiUrl)
            context.startActivity(intent)//contextの型にそろえる
        }

        //twitterのテキストを押したときの処理
        holder.twiView.setOnClickListener {//アイコンがタッチされたときの処理
            Toast.makeText(context, "twitterに移動します", Toast.LENGTH_LONG).show()//ちゃんと押されているか確認

            val twiUrl = "https://twitter.com/"+list[position].twiId

            //webViewActivityに遷移
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra("Url",twiUrl)
            context.startActivity(intent)//contextの型にそろえる
        }

        //githubのアイコンを押したときの処理
        holder.gitButton.setOnClickListener {
            Toast.makeText(context, "gitHubに移動します" , Toast.LENGTH_LONG).show()
            val gitUrl = "https://github.com/"+list[position].gitId//list[position].gitId...読み込んだデータのこと
            //https://github.com
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra("Url",gitUrl)//gitのurlをWebViewActivityに引き渡す
            context.startActivity(intent)
        }

        holder.gitView.setOnClickListener {
            Toast.makeText(context, "gitHubに移動します" , Toast.LENGTH_LONG).show()
            val gitUrl = "https://github.com/"+list[position].gitId//list[position].gitId...読み込んだデータのこと
            //https://github.com
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra("Url",gitUrl)//gitのurlをWebViewActivityに引き渡す
            context.startActivity(intent)
        }

        //削除機能
        holder.deleteButton.setOnClickListener{
            val username = list[position].nameId
            AlertDialog.Builder(context).apply {
                setTitle("$username さん")
                setMessage("を本当に削除しますか？")
                setPositiveButton("OK", DialogInterface.OnClickListener{ _, _ ->
                    //deleteの記述
                    thread{
                        db?.userDao()?.deleteSelect(list[position].uid!!)//データベースから選択したuidの部分を削除する
                        list.removeAt(position)//listから選択した部分の位置を削除する
                        Handler(Looper.getMainLooper()).post{//データが来たことをアダプターに教える...そして更新する
                            notifyDataSetChanged()
                        }
                    }

                    Toast.makeText(context, "delete", Toast.LENGTH_SHORT).show()
                })
                setNegativeButton("Cancel",null)
                show()
            }
        }


//        holder.itemView.setOnClickListener {
//            listener.onClickRow(it, list[position])
//        }
    }

    override fun getItemCount(): Int {
        Log.d("Adapter", "getItemCount")
        return list.size//listのサイズを送る
    }

    interface ListListener {
        fun onClickRow(tappedView: View, rowModel: RowModel)
    }

    fun addUser(userList: MutableList<RowModel>) {
        list.addAll(userList)
    }
}