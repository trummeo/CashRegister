
package com.example.cashregister.UILayer.Screens

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cashregister.DataLayer.Product
import com.example.cashregister.ViewModel.ProductViewModel
import androidx.compose.foundation.background
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import com.example.cashregister.ui.theme.Cream
import com.example.cashregister.ui.theme.SoftPink
import com.example.cashregister.ui.theme.Lavender
import com.example.cashregister.ui.theme.MintGreen
import com.example.cashregister.ui.theme.Peach
import com.example.cashregister.ui.theme.DarkBrown
import androidx.compose.material3.AlertDialog

@Composable
fun CashRegisterScreen(
    viewModel: ProductViewModel,
    onNavigateManager: () -> Unit
) {
    // get the current screen orientation
    val configuration = LocalConfiguration.current

    // check if the screen is in landscape mode
    val isLandscape =
        configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    // get the product list from the viewmodel
    val products by viewModel.products.collectAsState()

    // get the selected product id
    val selectedProductId by viewModel.selectedProductId.collectAsState()

    // get the entered quantity
    val quantityText by viewModel.quantityText.collectAsState()

    // get the message from the viewmodel
    val message by viewModel.message.collectAsState()
    // get the purchase summary
    val purchaseSummary by viewModel.purchaseSummary.collectAsState()

    // find the selected product
    val selectedProduct = products.find {
        it.id == selectedProductId
    }

    // get the current context for toast
    val context = LocalContext.current

    // show a message when it changes
    LaunchedEffect(message) {
        if (message != null) {
            Toast.makeText(
                context,
                message,
                Toast.LENGTH_SHORT
            ).show()

            viewModel.messageShown()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Cream)
            .padding(16.dp)
    ) {
        // top row with selected product and manager button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Product: ${selectedProduct?.name ?: "None"}",
                fontSize = 18.sp
            )

            Button(
                onClick = onNavigateManager,
                colors = ButtonDefaults.buttonColors(
                    containerColor = SoftPink,
                    contentColor = DarkBrown
                )
            ) {
                Text("Manager")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // show purchase summary popup
        if (purchaseSummary != null) {
            AlertDialog(
                onDismissRequest = {
                    viewModel.closePurchaseSummary()
                },
                title = {
                    Text("Purchase Successful!")
                },
                text = {
                    Text(
                        "${purchaseSummary!!.productName} x ${purchaseSummary!!.quantity} = $${"%.2f".format(purchaseSummary!!.total)}"
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.closePurchaseSummary()
                        }
                    ) {
                        Text("OK")
                    }
                }
            )
        }

        // change the layout based on screen orientation
        if (isLandscape) {

            // landscape layout
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                // controls on the left
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    RegisterControls(
                        viewModel = viewModel,
                        quantityText = quantityText
                    )
                }

                Spacer(modifier = Modifier.padding(8.dp))

                // product list on the right
                ProductList(
                    products = products,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    onProductClick = { product ->
                        viewModel.selectProduct(product)
                    }
                )
            }

        } else {

            // portrait layout
            RegisterControls(
                viewModel = viewModel,
                quantityText = quantityText
            )

            // product list below the controls
            ProductList(
                products = products,
                modifier = Modifier.weight(1f),
                onProductClick = { product ->
                    viewModel.selectProduct(product)
                }
            )
        }
    }
}

// show total, number pad, buy button and quantity
@Composable
fun RegisterControls(
    viewModel: ProductViewModel,
    quantityText: String
) {
    Column {
        // show the total amount
        Text(
            text = "Total: $${"%.2f".format(viewModel.getTotal())}",
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(8.dp))

        // number pad and buy button
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            // number pad
            Column(
                modifier = Modifier.weight(3f)
            ) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    NumberButton("1", Modifier.weight(1f)) {
                        viewModel.onDigit("1")
                    }
                    NumberButton("2", Modifier.weight(1f)) {
                        viewModel.onDigit("2")
                    }
                    NumberButton("3", Modifier.weight(1f)) {
                        viewModel.onDigit("3")
                    }
                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    NumberButton("4", Modifier.weight(1f)) {
                        viewModel.onDigit("4")
                    }
                    NumberButton("5", Modifier.weight(1f)) {
                        viewModel.onDigit("5")
                    }
                    NumberButton("6", Modifier.weight(1f)) {
                        viewModel.onDigit("6")
                    }
                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    NumberButton("7", Modifier.weight(1f)) {
                        viewModel.onDigit("7")
                    }
                    NumberButton("8", Modifier.weight(1f)) {
                        viewModel.onDigit("8")
                    }
                    NumberButton("9", Modifier.weight(1f)) {
                        viewModel.onDigit("9")
                    }
                }

                Row(modifier = Modifier.fillMaxWidth()) {
                    NumberButton("C", Modifier.weight(1f)) {
                        viewModel.clearQuantity()
                    }
                    NumberButton("0", Modifier.weight(1f)) {
                        viewModel.onDigit("0")
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
            }

            // buy button
            Button(
                modifier = Modifier
                    .weight(1f)
                    .height(180.dp)
                    .padding(start = 6.dp),
                onClick = {
                    viewModel.buy()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = MintGreen,
                    contentColor = DarkBrown
                )
            ) {
                Text("BUY")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // show the entered quantity
        Text(
            text = "Quantity: $quantityText",
            fontSize = 18.sp
        )
    }
}

// show all products
@Composable
fun ProductList(
    products: List<Product>,
    modifier: Modifier = Modifier,
    onProductClick: (Product) -> Unit
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = "Products",
            fontSize = 18.sp
        )

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(products) { product ->
                ProductItem(
                    product = product,
                    onClick = {
                        onProductClick(product)
                    }
                )
            }
        }
    }
}

// reusable button for the number pad
@Composable
fun NumberButton(
    number: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier.padding(2.dp),
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Lavender,
            contentColor = DarkBrown
        )
    ) {
        Text(number)
    }
}

// show one product in the list
@Composable
fun ProductItem(
    product: Product,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
            .clickable {
                onClick()
            }
    ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Text(text = product.name)
            Text(text = "Price: $${product.price}")
            Text(text = "Stock: ${product.quantity}")
        }
    }
}
