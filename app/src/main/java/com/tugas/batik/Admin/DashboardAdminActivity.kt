package com.tugas.batik.Admin

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.tugas.batik.Admin.Models.Users
import com.tugas.batik.Admin.Order.ListOrderActivity
import com.tugas.batik.Admin.Product.ProductActivity
import com.tugas.batik.R
import com.tugas.batik.SplashLoginActivity
import kotlinx.android.synthetic.main.activity_admin_dashboard.*

class DashboardAdminActivity : AppCompatActivity() {
    internal var firebaseUser: FirebaseUser? = null
    internal lateinit var reference: DatabaseReference

    lateinit var animationDrawable: AnimationDrawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        animationDrawable = relLay.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(2000)
        animationDrawable.setExitFadeDuration(2000)
        animationDrawable.start()

        firebaseUser = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser!!.uid)

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue<Users>(Users::class.java)
                tv_username.text = user!!.username
                if (user.imageUrl == "default") {
                    img_profile.setImageResource(R.drawable.logo)
                } else {
                    Glide.with(applicationContext).load(user.imageUrl).into(img_profile)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                //
            }
        })

        btn_product.setOnClickListener {
            startActivity(Intent(this, ProductActivity::class.java))
        }

        btn_list_order.setOnClickListener {
            startActivity(Intent(this, ListOrderActivity::class.java))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_admin, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(
                    Intent(
                        this@DashboardAdminActivity,
                        SplashLoginActivity::class.java
                    ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                )
                return true
            }
        }

        return false
    }
}

