
package com.example.cashregister.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.cashregister.DataLayer.Product
import com.example.cashregister.DataLayer.ProductRepository
import com.example.cashregister.DataLayer.Purchase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProductViewModel(
    private val repository: ProductRepository
) : ViewModel() {

    //see the product list from the repository
    val products: StateFlow<List<Product>> = repository.getProduct().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )
    //store the selected product for restocking
    private val _selectedRestockProductId = MutableStateFlow<Int?>(null)
    val selectedRestockProductId: StateFlow<Int?> =
        _selectedRestockProductId.asStateFlow()

    //store the entered restock quantity
    private val _restockQuantity = MutableStateFlow("")
    val restockQuantity: StateFlow<String> = _restockQuantity.asStateFlow()

    //store the selected product id
    private val _selectedProductId = MutableStateFlow<Int?>(null)
    val selectedProductId: StateFlow<Int?> = _selectedProductId.asStateFlow()

    //store the entered quantity
    private val _quantityText = MutableStateFlow("")
    val quantityText: StateFlow<String> = _quantityText.asStateFlow()

    //store purchase history
    private val _purchases = MutableStateFlow<List<Purchase>>(emptyList())
    val purchases: StateFlow<List<Purchase>> = _purchases.asStateFlow()

    //store messages for the user
    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    // store the purchase summary
    private val _purchaseSummary = MutableStateFlow<Purchase?>(null)
    val purchaseSummary: StateFlow<Purchase?> = _purchaseSummary.asStateFlow()

    fun closePurchaseSummary() {
        _purchaseSummary.value = null
    }

    //keep track of the next purchase id
    private var nextPurchaseId = 1

    //find the selected product
    fun getSelectedProduct(): Product? {
        return products.value.find {
            it.id == _selectedProductId.value
        }
    }

    //select a product
    fun selectProduct(product: Product) {
        _selectedProductId.value = product.id
        _quantityText.value = ""
        _message.value = null
    }

    //enter a number from the number pad
    fun onDigit(digit: String) {
        if (digit.length != 1 || digit !in "0123456789") return

        val newText = _quantityText.value + digit

        if (newText.length <= 3) {
            _quantityText.value = newText
            _message.value = null
        }
    }

    //clear the entered quantity
    fun clearQuantity() {
        _quantityText.value = ""
        _message.value = null
    }

    //calculate the purchase total
    fun getTotal(): Double {
        val quantity = _quantityText.value.toIntOrNull() ?: 0
        val product = getSelectedProduct()

        return quantity * (product?.price ?: 0.0)
    }

    //complete a purchase
    fun buy() {

        val selectedProduct = getSelectedProduct()
        val quantity = _quantityText.value.toIntOrNull()

        //check whether a product is selected
        if (selectedProduct == null) {
            _message.value = "Please select a product"
            return
        }

        //check whether a valid quantity is entered
        if (quantity == null || quantity <= 0) {
            _message.value = "Please enter a valid quantity"
            return
        }

        //check whether enough stock is available
        if (quantity > selectedProduct.quantity) {
            _message.value = "Not enough stock available"
            return
        }

        viewModelScope.launch {

            //calculate the total price
            val total = quantity * selectedProduct.price

            //create a purchase record
            val purchase = Purchase(
                id = nextPurchaseId,
                productName = selectedProduct.name,
                quantity = quantity,
                unitPrice = selectedProduct.price,
                total = total
            )

            //update the product quantity
            val updatedProduct = selectedProduct.copy(
                quantity = selectedProduct.quantity - quantity
            )

            repository.updateProduct(updatedProduct)

            //add the purchase to history
            _purchases.value = listOf(purchase) + _purchases.value

            //find a purchase using its id
            fun getPurchase(purchaseId: Int): Purchase? {
                return purchases.value.find {
                    it.id == purchaseId
                }
            }

            //reset the cash register screen
            _selectedProductId.value = null
            _quantityText.value = ""
            _purchaseSummary.value = purchase

            nextPurchaseId++
        }
    }

    //reset the message after it is displayed
    fun messageShown() {
        _message.value = null
    }

    //select a product for restocking
    fun selectRestockProduct(product: Product) {
        _selectedRestockProductId.value = product.id
    }

    //update the entered restock quantity
    fun onRestockQuantityChange(quantity: String) {
        if (quantity.all { it.isDigit() }) {
            _restockQuantity.value = quantity
        }
    }

    //complete restocking
    fun restock() {

        val productId = _selectedRestockProductId.value
        val quantity = _restockQuantity.value.toIntOrNull()

        //check whether a product is selected
        if (productId == null) {
            _message.value = "Please select a product"
            return
        }

        //check whether a valid quantity is entered
        if (quantity == null || quantity <= 0) {
            _message.value = "Please enter a valid quantity"
            return
        }

        viewModelScope.launch {

            //find the selected product
            val selectedProduct = repository.getProduct(productId)

            if (selectedProduct != null) {

                //add the new quantity to existing stock
                val updatedProduct = selectedProduct.copy(
                    quantity = selectedProduct.quantity + quantity
                )

                //update the repository
                repository.updateProduct(updatedProduct)

                //reset the restock screen
                _selectedRestockProductId.value = null
                _restockQuantity.value = ""

                _message.value = "Restock successful!"
            }
        }
    }

    //cancel restocking
    fun cancelRestock() {
        _selectedRestockProductId.value = null
        _restockQuantity.value = ""
        _message.value = null
    }
    //find a purchase using its id
    fun getPurchase(purchaseId: Int): Purchase? {
        return purchases.value.find {
            it.id == purchaseId
        }
    }

    companion object {
        fun provideFactory(repository: ProductRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {

                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(ProductViewModel::class.java)) {
                        return ProductViewModel(repository) as T
                    }

                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
    }
}
