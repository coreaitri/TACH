package com.tugas.batik.Customer.Cart

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tugas.batik.Constant
import com.tugas.batik.Customer.Model.Cart
import com.tugas.batik.R
import kotlinx.android.synthetic.main.activity_cart.*

class CartActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var cartList: MutableList<Cart>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        recyclerView = findViewById(R.id.recyclerview)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        cartList = mutableListOf()
        loadData()

        btn_checkout.setOnClickListener {
            val totalCart = total_price.text.toString()

            val intent = Intent(this, ConfirmOrderActivity::class.java)
            intent.putExtra(Constant.KEY_TOTAL_CART, totalCart)
            startActivity(intent)
        }
    }

    private fun loadData()
    {
        val akunUser = FirebaseAuth.getInstance().currentUser?.uid
        val dbRef = FirebaseDatabase.getInstance().getReference("Cart").orderByChild("user_id").equalTo(akunUser)

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError)
            {
                Toast.makeText(applicationContext,"Error . "+databaseError.message, Toast.LENGTH_LONG).show()/**/

            }

            override fun onDataChange(dataSnapshot: DataSnapshot)
            {
                var tt_quantity = 0
                var tt_price = 0
                if (dataSnapshot.exists())
                {//before fetch we have clear the list not to show duplicate value
                    cartList.clear()
                    // fetch data & add to list
                    for (data in dataSnapshot.children)
                    {
                        val std = data.getValue(Cart::class.java)
                        cartList.add(std!!)

                        val t_quantity = data.getValue(Cart::class.java)?.quantity?.toInt()
                        tt_quantity += t_quantity!!

                        val t_price = data.getValue(Cart::class.java)?.total?.toInt()
                        tt_price += t_price!!
                    }

                    val adapter = CartAdapter(cartList, this@CartActivity)
                    recyclerview.adapter = adapter
                    adapter.notifyDataSetChanged()

                    total_quantity.text = tt_quantity.toString()
                    total_price.text = tt_price.toString()

                }
                else
                {
                    Toast.makeText(applicationContext,"Data tidak ditemukan", Toast.LENGTH_LONG).show()
                }

            }

        })
    }
}
