package com.tugas.batik.Admin.Product

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.tugas.batik.Admin.Models.Product
import com.tugas.batik.R
import kotlinx.android.synthetic.main.activity_product.*
import java.util.*

class ProductActivity : AppCompatActivity() {

    private lateinit var productName: EditText
    private lateinit var productPrice: EditText
    private lateinit var productImage: ImageView
    private lateinit var saveData: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var dbRef: DatabaseReference
    private lateinit var productList: MutableList<Product>
    private lateinit var recyclerView: RecyclerView

    private var imgPath: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)

        recyclerView = findViewById(R.id.rv_product)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        productName = findViewById(R.id.et_nameProduct);
        productPrice = findViewById(R.id.et_priceProduct);
        productImage = findViewById(R.id.img_product)
        saveData = findViewById(R.id.btn_saveData);
        progressBar = findViewById(R.id.progressbar)
        dbRef = FirebaseDatabase.getInstance().getReference("Product")

        productImage.setOnClickListener {
            val mImg = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(mImg,0)
        }

        // initialize mutable list
        productList = mutableListOf()

        // save data to database
        saveData.setOnClickListener()
        {
            saveDataToServer()
            progressBar.visibility = View.VISIBLE
        }

        // call load data method in main thread
        loadData()
    }

    private fun saveDataToServer(){
        // get value from edit text & spinner
        val name: String = productName.text.toString().trim()
        val price: String = productPrice.text.toString().trim()

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(price))
        {
            val filename = UUID.randomUUID().toString()
            val storageReference = FirebaseStorage.getInstance().getReference("image_product/$filename")
            val databaseReference = FirebaseDatabase.getInstance().getReference("Product").push()
            val productid = databaseReference.key

            storageReference.putFile(imgPath!!).addOnSuccessListener {
                storageReference.downloadUrl.addOnSuccessListener {
                    databaseReference.child("image").setValue(it.toString())
                    databaseReference.child("productid").setValue(productid.toString())
                    databaseReference.child("name").setValue(name)
                    databaseReference.child("price").setValue(price)

                    Toast.makeText(this, "Sukses menambahkan produk", Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                    productImage.visibility = View.GONE
                    productName.text = null
                    productPrice.text = null
                }
            }
                .addOnFailureListener {
                    progressBar.visibility = View.GONE
                    println("Info File : ${it.message}")
                }

        }
        else
        {
            progressBar.visibility = View.GONE
            Toast.makeText(this,"Silahkan isi data barang", Toast.LENGTH_LONG).show()
        }
    }

    private fun loadData()
    {
        progressBar.visibility = View.VISIBLE

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError)
            {
                Toast.makeText(applicationContext,"Error. "+databaseError.message, Toast.LENGTH_LONG).show()/**/
            }

            override fun onDataChange(dataSnapshot: DataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    //before fetch we have clear the list not to show duplicate value
                    productList.clear()
                    // fetch data & add to list
                    for (data in dataSnapshot.children)
                    {
                        val std = data.getValue(Product::class.java)
                        productList.add(std!!)
                    }

                    // bind data to adapter
                    val adapter =
                        ProductAdapter(productList, this@ProductActivity)
                    rv_product.adapter = adapter
                    progressBar.visibility = View.GONE
                    adapter.notifyDataSetChanged()

                }
                else
                {
                    // if no data found or you can check specify child value exist or not here
                    Toast.makeText(applicationContext,"Data Tidak Ditemukan", Toast.LENGTH_LONG).show()
                    progressBar.visibility = View.GONE
                }

            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            imgPath = data?.data

            try {
                //getting image from gallery
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imgPath)

                //Setting image to ImageView
                productImage.setImageBitmap(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

}

