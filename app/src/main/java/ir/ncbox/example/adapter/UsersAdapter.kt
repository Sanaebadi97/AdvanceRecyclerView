package ir.ncbox.example.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ir.ncbox.example.R
import ir.ncbox.example.cell.User
import ir.ncbox.libarary.adapter.AdvanceRecyclerViewAdapter
import kotlinx.android.synthetic.main.user_item_view.view.*

class UsersAdapter(context: Context) : AdvanceRecyclerViewAdapter<User, UsersAdapter.UserHolder>(context) {


    override fun onCreateDataItemViewHolder(parent: ViewGroup?, viewType: Int): UserHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.user_item_view, parent, false)
        return UserHolder(view)
    }

    override fun onBindDataItemViewHolder(holder: UserHolder?, position: Int) {
        holder!!.bindItem(data[position])
    }

    override fun footerOnVisibleItem() {

    }


    class UserHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItem(user: User) {
            itemView.tv_user_item_username.text = user.name
            itemView.tv_user_item_job.text = user.job
        }

    }

}