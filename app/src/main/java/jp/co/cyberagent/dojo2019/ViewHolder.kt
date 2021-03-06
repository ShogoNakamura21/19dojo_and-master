package jp.co.cyberagent.dojo2019

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.profile_cell.view.*

class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val nameView: TextView = itemView.findViewById(R.id.textViewName)
    val gitView: TextView = itemView.findViewById(R.id.textViewGit)
    val twiView: TextView = itemView.findViewById(R.id.textViewTwi)


    val twiButton: ImageView = itemView.findViewById(R.id.imageViewTwi)
    val gitButton: ImageView = itemView.findViewById(R.id.imageViewGit)
    val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)
}
