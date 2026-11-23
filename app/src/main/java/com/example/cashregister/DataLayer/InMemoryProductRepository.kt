package com.example.cashregister.DataLayer

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class InMemoryProductRepository : ProductRepository {

    private val products = mutableListOf(
        Product(1,"Pants",10,20.44),
        Product(2,"Shoes",100,10.44),
        Product(3,"Hats",30,5.90),
        Product(4,"Shirts",50,15.99),
        Product(5,"Jackets",5,99.99)
    )
    //store the product list and notifies the UI when it changes
    private val productFlow = MutableStateFlow<List<Product>>(products.toList ())

    //return the product list as a read-only flow
    override fun getProduct(): Flow<List<Product>> {
        return productFlow.asStateFlow()
    }

    //add new product to the list
    override suspend fun addProduct(product: Product) {
        val currentList = productFlow.value
        productFlow.value = currentList + product
    }

    //update existing product
    override suspend fun updateProduct(product: Product) {
        val currentList = productFlow.value

        productFlow.value = currentList.map { existing ->
            if (existing.id == product.id) product else existing
        }
    }

    //delele product using id
    override suspend fun deleteProduct(productId: Int) {
        val currentList = productFlow.value
        productFlow.value = currentList.filter {
            it.id != productId
        }
    }

    //find product using id
    override suspend fun getProduct(productId: Int): Product? {
        return productFlow.value.find {
            it.id == productId
        }
    }


}

