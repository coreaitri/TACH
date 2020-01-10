package com.tugas.batik.Admin.Order

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.tugas.batik.Customer.Model.Order
import com.tugas.batik.R
import kotlinx.android.synthetic.main.activity_list_order.*

class ListOrderActivity : AppCompatActivity() {

    lateinit var rv: RecyclerView
    lateinit var dbRef: DatabaseReference
    lateinit var orderList: MutableList<Order>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_order)

        rv = findViewById(R.id.recyclerView)
        rv.setHasFixedSize(true)
        rv.layoutManager = LinearLayoutManager(this)

        dbRef = FirebaseDatabase.getInstance().getReference("Order")
        orderList = mutableListOf()
        loadData()
    }

    private fun loadData(){
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError)
            {
                Toast.makeText(applicationContext,"Error. "+databaseError.message, Toast.LENGTH_LONG).show()/**/

            }

            override fun onDataChange(dataSnapshot: DataSnapshot)
            {
                if (dataSnapshot.exists()){
                    //before fetch we have clear the list not to show duplicate value
                    orderList.clear()
                    // fetch data & add to list
                    for (data in dataSnapshot.children)
                    {
                        val std = data.getValue(Order::class.java)
                        orderList.add(std!!)
                    }

                    val adapter = ListOrderAdapter(orderList, this@ListOrderActivity)
                    recyclerView.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
                else
                {
                    // if no data found or you can check specefici child value exist or not here
                    Toast.makeText(applicationContext,"No data Found", Toast.LENGTH_LONG).show()
                }

            }

        })
    }
}
