package com.tugas.batik

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.tugas.batik.Customer.DashboardCustomerActivity
import kotlinx.android.synthetic.main.activity_register.*
import java.util.HashMap

class RegisterActivity : AppCompatActivity() {

    internal lateinit var auth: FirebaseAuth
    internal lateinit var reference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btnRegister.setOnClickListener {
            doRegister()
        }
    }
    private fun doRegister(){
        val username = etr_name.text.toString()
        val email = etr_email.text.toString()
        val password = etr_password.text.toString()

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
        }else if (password.length < 6){
            Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show()
        }else{
            signUp(username, email, password)
        }
    }

    private fun signUp(username: String, email: String, password: String) {
        auth = FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful){
                    val firebaseUser = auth.currentUser
                    val userid = firebaseUser?.uid

                    reference = FirebaseDatabase.getInstance().getReference("Users").child(userid!!)

                    val hashMap = HashMap<String, String>()
                    hashMap.put("id", userid)
                    hashMap.put("username", username)
                    hashMap.put("imageUrl", "default")

                    reference.setValue(hashMap).addOnCompleteListener {
                        if (it.isSuccessful){
                           startActivity(Intent(this, DashboardCustomerActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
                        }
                    }
                }else{
                    Toast.makeText(
                        this,
                        "email is already exist",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}
