package com.tugas.batik.Customer.Model

class Order(var user_id:String, var orderid: String, var name:String, var no_telp:String, var alamat: String, var total:String)
{
    //empty constrcutor
    constructor() : this("","","","","",""){

    }

}