package com.tugas.batik.Customer.Cart

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tugas.batik.Customer.Model.Cart
import com.tugas.batik.R

class CartAdapter(val cartList: List<Cart>, val context: Context) : RecyclerView.Adapter<CartAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int
    {
        return cartList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int)
    {
        holder.name.text = cartList[position].name
        holder.quantity.text = cartList[position].quantity
        holder.price.text = cartList[position].total

    }


    // holder class
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name = itemView.findViewById(R.id.tvNameCart) as TextView
        val quantity = itemView.findViewById(R.id.tvQuantityCart) as TextView
        val price = itemView.findViewById(R.id.tvPriceCart) as TextView
    }
}