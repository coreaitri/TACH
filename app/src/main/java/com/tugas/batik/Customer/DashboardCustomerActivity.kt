package com.tugas.batik.Customer

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.tugas.batik.Admin.Models.Users
import com.tugas.batik.Customer.Cart.CartActivity
import com.tugas.batik.R
import com.tugas.batik.SplashLoginActivity
import kotlinx.android.synthetic.main.activity_customer_dashboard.*

class DashboardCustomerActivity : AppCompatActivity() {

    internal var firebaseUser: FirebaseUser? = null
    internal lateinit var reference: DatabaseReference

    lateinit var animationDrawable: AnimationDrawable
    lateinit var recyclerView: RecyclerView
    lateinit var dbRef: DatabaseReference
    lateinit var productList: MutableList<com.tugas.batik.Admin.Models.Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_dashboard)


        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        animationDrawable = relLay.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(2000)
        animationDrawable.setExitFadeDuration(2000)
        animationDrawable.start()

        recyclerView = findViewById(R.id.recyclerview)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        firebaseUser = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser!!.uid)

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue<Users>(Users::class.java!!)
                tv_username.text = user!!.username
                if (user.imageUrl == "default") {
                    img_profile.setImageResource(R.drawable.ic_account)
                } else {
                    //change this
                    Glide.with(applicationContext).load(user.imageUrl).into(img_profile)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

        dbRef = FirebaseDatabase.getInstance().getReference("Product")
        productList = mutableListOf()
        loadData()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(
                    Intent(
                        this@DashboardCustomerActivity,
                        SplashLoginActivity::class.java
                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )

                return true
            }

            R.id.cart -> {
                startActivity(Intent(this, CartActivity::class.java))
            }

        }

        return false
    }

    private fun loadData()
    {
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError) {
                Toast.makeText(applicationContext,"Error . "+databaseError.message, Toast.LENGTH_LONG).show()/**/
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    //before fetch we have clear the list not to show duplicate value
                    productList.clear()
                    // fetch data & add to list
                    for (data in dataSnapshot.children) {
                        val std = data.getValue(com.tugas.batik.Admin.Models.Product::class.java)
                        productList.add(std!!)
                    }

                    val adapter = ProductAdapterCustomer(productList, this@DashboardCustomerActivity)
                    recyclerview.adapter = adapter
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
