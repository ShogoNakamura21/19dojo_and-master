package jp.co.cyberagent.dojo2019

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView


class ViewAdapter(private val list: MutableList<RowModel>, private val listener: ListListener,val context: Context) ://contextが使えないから持ってきた（profileactivityから）
    RecyclerView.Adapter<ViewHolder>() {//generics

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

        holder.twiButton.setOnClickListener {//アイコンがタッチされたときの処理
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