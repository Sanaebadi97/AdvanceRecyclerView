package ir.ncbox.example

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Toast
import ir.ncbox.example.adapter.UsersAdapter
import ir.ncbox.example.cell.User
import ir.ncbox.example.util.Logger
import ir.ncbox.libarary.liseners.OnRefreshListener
import ir.ncbox.libarary.liseners.OnScrollEndListener
import ir.ncbox.libarary.util.SwipeToDismiss
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.network_error_message.view.*
import java.util.*


class MainActivity : Activity(), OnRefreshListener, OnScrollEndListener, SwipeToDismiss.SwipetoDismissCallBack {


    val usersCollection = ArrayList<HashMap<String, String>>()
    val layoutManager = LinearLayoutManager(this)
    val adapter = UsersAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        fillCollection()

        listModeSetup()


    }


    private fun listModeSetup() {


        rv_advance_list.setLayoutManager(layoutManager)

        rv_advance_list.setAdapter(adapter)

        rv_advance_list.onRefreshListener = this

        rv_advance_list.scrollEndListener = this

        rv_advance_list.setFooterLoadingView(R.layout.footer_loading)

        rv_advance_list.setToRightSwipeItem(R.drawable.ic_delete_grey_600_36dp,
                ContextCompat.getColor(this, R.color.red_light), this)


        rv_advance_list.showContentLoading()


        Handler().postDelayed({

            // showNetworkErrorMessage()

            rv_advance_list.showContentMessage(resources.getString(R.string.network_error), R.drawable.ic_wifi_grey_600_48dp)

        }, 1500)


    }

    private fun showNetworkErrorMessage() {

        rv_advance_list.showContentMessage(R.layout.network_error_message)
        rv_advance_list.getContentMessageView()!!.ll_try_again.setOnClickListener {

            rv_advance_list.showContentLoading()

            Handler().postDelayed({

                adapter.data = getTestUsers(5)
                rv_advance_list.showList()

            }, 1500)

        }

    }

    private fun getTestUsers(count: Int): ArrayList<User> {

        val users = ArrayList<User>()
        for (x in 0..count) {
            val random = Random().nextInt(usersCollection.size)
            val randomData = usersCollection[random]
            val user = User(randomData["name"], randomData["job"])
            users.add(user)
        }

        return users

    }

    private fun fillCollection() {

        val user1 = HashMap<String, String>()
        user1["name"] = "Bill Gates"
        user1["job"] = "Microsoft Leader"


        val user2 = HashMap<String, String>()
        user2["name"] = "Steve Jobs"
        user2["job"] = "Apple Leader"


        val user3 = HashMap<String, String>()
        user3["name"] = "Reza Masoudi"
        user3["job"] = "NCBOX Leader"

        usersCollection.add(user1)
        usersCollection.add(user2)
        usersCollection.add(user3)


    }

    // on page refresh
    override fun onRefresh() {

        Handler().postDelayed({

            if (adapter.data != null) {
                adapter.data.clear()
            }
            adapter.data = getTestUsers(Random().nextInt(13))
            adapter.notifyDataSetChanged()
            rv_advance_list.cancelRefreshing()
            rv_advance_list.showList()

        }, 1000)

    }

    // on page scroll end
    override fun onEndPage() {

        rv_advance_list.pauseEndlessListener()

        Logger.i("On Page End...")

        rv_advance_list.showFooterLoading()

        Handler().postDelayed({
            adapter.data.addAll(getTestUsers(Random().nextInt(13)))
            adapter.notifyDataSetChanged()


            rv_advance_list.cancelFooterLoading()

            rv_advance_list.startEndlessListener()

        }, 2000)
    }

    override fun onSwipedToRight(viewHolder: RecyclerView.ViewHolder) {
        val position = viewHolder.adapterPosition
        adapter.data!!.removeAt(position)
        adapter.notifyItemRemoved(position)
        adapter.notifyItemRangeChanged(position, adapter.data!!.size)

    }

    override fun onSwipedToLeft(viewHolder: RecyclerView.ViewHolder) {
    }

}
