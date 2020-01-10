package com.tugas.batik.Admin.Order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tugas.batik.Constant
import com.tugas.batik.Customer.Cart.CartAdapter
import com.tugas.batik.Customer.Model.Cart
import com.tugas.batik.R
import kotlinx.android.synthetic.main.activity_detail_order.*

class DetailOrderActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var cartList: MutableList<Cart>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_order)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this) as RecyclerView.LayoutManager?

        cartList = mutableListOf()
        loadData()
    }

    private fun loadData(){
        val user_id = intent.getStringExtra(Constant.KEY_ID_USER)
        val dbRef = FirebaseDatabase.getInstance().getReference("CartAdmin").orderByChild("user_id").equalTo(user_id)

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(applicationContext,"Error. "+databaseError.message, Toast.LENGTH_LONG).show()/**/
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var tt_quantity = 0
                var tt_price = 0
                if (dataSnapshot.exists()) {
                    //before fetch we have clear the list not to show duplicate value
                    cartList.clear()
                    // fetch data & add to list
                    for (data in dataSnapshot.children) {
                        val std = data.getValue(Cart::class.java)
                        cartList.add(std!!)

                        val t_quantity = data.getValue(Cart::class.java)?.quantity?.toInt()
                        tt_quantity += t_quantity!!

                        val t_price = data.getValue(Cart::class.java)?.total?.toInt()
                        tt_price += t_price!!
                    }

                    val adapter = CartAdapter(cartList, this@DetailOrderActivity)
                    recyclerView.adapter = adapter
                    adapter.notifyDataSetChanged()

                    total_quantity.text = tt_quantity.toString()
                    total_price.text = tt_price.toString()

                }
                else{
                    // if no data found or you can check specify child value exist or not here
                    Toast.makeText(applicationContext,"Data tidak ditemukan", Toast.LENGTH_LONG).show()
                }

            }

        })
    }
}
