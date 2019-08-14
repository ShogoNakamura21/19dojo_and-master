package jp.co.cyberagent.dojo2019


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.concurrent.thread


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 *
 */
class ProfileFragment : Fragment() {


    var db:AppDatabase? = null
    var userData = mutableListOf<User>()
    lateinit var adapter: ViewAdapter//今後入ることを前提に定義することができる

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val userList :MutableList<RowModel> = userData.map { user ->
            RowModel(user.nameId, user.twiId, user.gitId)
        }.toMutableList()//Userをrowmodelに　中身なし

        adapter = ViewAdapter(userList, object : ViewAdapter.ListListener{
            override fun onClickRow(tappedView: View, rowModel: RowModel) {
                //アダプターがタップされたときの処理

                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        },view.context)

        //this→view.context

        db = AppDatabase.get(view.context)//databaseに入っている全てのデータを呼び出し
        thread {
            userData = db?.userDao()?.getAll()!!.toMutableList()//中が(User)で既にデータは入っているこれを(Rowmodelにする)...!!はnull許容型をそうじゃなくするもの
            val userList :MutableList<RowModel> = userData.map { user ->
                RowModel(user.nameId, user.twiId, user.gitId)
            }.toMutableList()
            adapter.addUser(userList)//adapterにデータが入っている<rowmodel>の配列を渡す
            Handler(Looper.getMainLooper()).post{
                adapter.notifyDataSetChanged()//データが来たことをアダプターに教える
            }

        }

        val mRecyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        //レイアウトマネージャーを設定（ここで縦方向の標準リストであることを指定）
        mRecyclerView.setLayoutManager( LinearLayoutManager(view.context))
        mRecyclerView.setAdapter(adapter)

    }


}
