package com.tugas.batik

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.tugas.batik.Admin.DashboardAdminActivity
import com.tugas.batik.Customer.DashboardCustomerActivity
import kotlinx.android.synthetic.main.activity_splash_login.*

class SplashLoginActivity : AppCompatActivity() {

    lateinit var anim: Animation
    private lateinit var auth: FirebaseAuth

    internal var firebaseUser: FirebaseUser? = null

    override fun onStart() {
        super.onStart()

        firebaseUser = FirebaseAuth.getInstance().currentUser

        if (firebaseUser != null){
            if (firebaseUser!!.email == "admin@gmail.com" ){
                startActivity(Intent(this, DashboardAdminActivity::class.java))
                finish()
            }else{
                startActivity(Intent(this, DashboardCustomerActivity::class.java))
                finish()
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_login)

        anim = AnimationUtils.loadAnimation(this, R.anim.frombottom)
        bg_app.animate().translationY(-1900f).setDuration(2600).startDelay=600
        logo.animate().alpha(0f).setDuration(2600).startDelay=1200
        textSplash.animate().translationY(140f).alpha(0f).setDuration(2600).startDelay=600

        textQuh.startAnimation(anim)
        menus.startAnimation(anim)

        auth = FirebaseAuth.getInstance()


        tv_register.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java ))
        }

        btnLogin.setOnClickListener {
            doLogin()
        }
    }

    private fun doLogin(){
        val email = etl_email.text.toString()
        val password = etl_password.text.toString()

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(this, "the fields can not empty", Toast.LENGTH_SHORT).show()
        }else if (password.length < 6){
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
        }else{
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {
                    if (it.isSuccessful){
                        if (email == "admin@gmail.com" ){
                            startActivity(Intent(this, DashboardAdminActivity::class.java)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
                        }else{
                            startActivity(Intent(this, DashboardCustomerActivity::class.java)
                                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
                        }
                    }else{
                        Toast.makeText(
                            this,
                            "Authentication Failed!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }
    }

}
