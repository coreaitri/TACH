package com.tugas.batik.Admin.Product

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.FirebaseDatabase
import com.tugas.batik.Admin.Models.Product
import com.tugas.batik.R

class ProductAdapter(val productList: List<Product>, val context: Context) : RecyclerView.Adapter<ProductAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.custom_layout, parent, false)
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
        holder.edit.setOnClickListener()
        {
            val perItemPosition = productList[position]
            updateDialog(perItemPosition)
        }

        // if user click on delete icon for delete operation
        holder.delete.setOnClickListener()
        {
            val perItemPosition = productList[position]
            deleteData(perItemPosition.productid)
        }
    }


    // holder class
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name = itemView.findViewById(R.id.tv_nameProduct) as TextView
        val price = itemView.findViewById(R.id.tv_priceProduct) as TextView
        val image = itemView.findViewById(R.id.iv_imageProduct) as ImageView

        // action operation widget
        val edit = itemView.findViewById(R.id.fabEdit) as FloatingActionButton
        val delete = itemView.findViewById(R.id.fabDelete) as FloatingActionButton

    }

    // update dialog show method
    private fun updateDialog(perItemPosition: Product) {

        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.update_dialog, null)
        builder.setCancelable(false)

        val etuName = view.findViewById<EditText>(R.id.etu_nameProduct)
        val etuPrice = view.findViewById<EditText>(R.id.etu_priceProduct)

        // set exist data from recycler to dialog field
        etuName.setText(perItemPosition.name)
        etuPrice.setText(perItemPosition.price)

        // now set view to builder
        builder.setView(view)
        // now set positive negative button in alertdialog
        builder.setPositiveButton("Update", object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {

                // update operation below
                val dbRef = FirebaseDatabase.getInstance().getReference("Product")

                val name = etuName.text.toString()
                val price = etuPrice.text.toString()
                val image = perItemPosition.image

                if (name.isEmpty() && price.isEmpty())
                {
                    etuName.error = "please Fill up data"
                    etuPrice.requestFocus()
                    return
                }
                else
                {
                    // update data
                    val stdData = Product(
                        perItemPosition.productid,
                        name,
                        price,
                        image
                    )
                    dbRef.child(perItemPosition.productid).setValue(stdData)
                    Toast.makeText(context, "Update Data", Toast.LENGTH_SHORT).show()

                }


            }
        })

        builder.setNegativeButton("Batal", object : DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {


            }
        })
        // show dialog now
        val alert = builder.create()
        alert.show()
    }

    // delete operation
    private fun deleteData(productid: String)
    {
        val dbRef = FirebaseDatabase.getInstance().getReference("Product").child(productid)
        dbRef.removeValue().addOnCompleteListener()
        {
            Toast.makeText(context, "Sukses menghapus data", Toast.LENGTH_SHORT).show()
        }

    }


}