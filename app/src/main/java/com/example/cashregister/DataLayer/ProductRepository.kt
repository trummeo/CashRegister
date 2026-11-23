package com.example.cashregister.DataLayer

import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getProduct(): Flow<List<Product>>                   //get the list of product
    //suspend ~ allow func to work that takes time
    suspend fun addProduct(product: Product)                //add a new product
    suspend fun updateProduct(product: Product)             //update an existing product
    suspend fun deleteProduct(productId: Int)            //delete a product using its id
    suspend fun getProduct (productId: Int): Product?    //get one product using its id

}