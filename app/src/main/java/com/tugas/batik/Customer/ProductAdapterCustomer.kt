package com.tugas.batik.Customer

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tugas.batik.Admin.Models.Product
import com.tugas.batik.Constant
import com.tugas.batik.Customer.Cart.QuantityActivity
import com.tugas.batik.R

class ProductAdapterCustomer(val productList: MutableList<Product>, val context: Context) : RecyclerView.Adapter<ProductAdapterCustomer.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_layout_customer, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int
    {
        return productList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int)
    {
        holder.name.text = productList[position].name
        holder.price.text = productList[position].price
        Glide.with(context).load(productList[position].image).into(holder.image)

        // if user click on update icon for  update operation
        holder.itemView.setOnClickListener()
        {
            val productid = productList[position].productid
            val nameProduct = productList[position].name
            val priceProduct = productList[position].price
            val imageProduct = productList[position].image

            val intent = Intent(context, QuantityActivity::class.java)
            intent.putExtra(Constant.KEY_ID_PRODUCT, productid)
            intent.putExtra(Constant.KEY_NAME, nameProduct)
            intent.putExtra(Constant.KEY_PRICE, priceProduct)
            intent.putExtra(Constant.KEY_IMAGE, imageProduct)
            context.startActivity(intent)
        }

    }


    // holder class
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name = itemView.findViewById(R.id.textView1) as TextView
        val price = itemView.findViewById(R.id.textView2) as TextView
        val image = itemView.findViewById(R.id.img_product) as ImageView

        // action operation widget
        val pesan = itemView.findViewById(R.id.tv_pesan) as TextView

    }
}