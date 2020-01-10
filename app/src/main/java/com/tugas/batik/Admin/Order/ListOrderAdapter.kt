package com.tugas.batik.Admin.Order

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tugas.batik.Constant
import com.tugas.batik.Customer.Model.Order
import com.tugas.batik.R

class ListOrderAdapter(val orderList: List<Order>, val context: Context) : RecyclerView.Adapter<ListOrderAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_order, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int
    {
        return orderList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int)
    {
        holder.orderId.text = orderList[position].orderid
        holder.nameCustomer.text = orderList[position].name
        holder.contact.text = orderList[position].no_telp
        holder.address.text = orderList[position].alamat
        holder.total.text = orderList[position].total

        holder.viewOrder.setOnClickListener {
            val user_id = orderList[position].user_id

            val intent = Intent(context, DetailOrderActivity::class.java)
            intent.putExtra(Constant.KEY_ID_USER, user_id)
            context.startActivity(intent)
        }
    }


    // holder class
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val orderId = itemView.findViewById(R.id.orderId) as TextView
        val nameCustomer = itemView.findViewById(R.id.nameCustomer) as TextView
        val contact = itemView.findViewById(R.id.contact) as TextView
        val address = itemView.findViewById(R.id.address) as TextView
        val total = itemView.findViewById(R.id.total) as TextView

        val viewOrder = itemView.findViewById(R.id.viewOrder) as Button
    }
}