package com.tugas.batik.Customer.Model


class Cart(var user_id:String, var productid: String, var name:String, var quantity:String, var total: String)
{
    //empty constrcutor
    constructor() : this("","","","",""){

    }

}