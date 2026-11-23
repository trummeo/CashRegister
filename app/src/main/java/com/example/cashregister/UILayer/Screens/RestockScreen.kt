
package com.example.cashregister.UILayer.Screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cashregister.ViewModel.ProductViewModel

@Composable
fun RestockScreen(
    viewModel: ProductViewModel,
    onNavigateBack: () -> Unit
) {

    //get the product list
    val products by viewModel.products.collectAsState()

    //get selected product id
    val selectedProductId by
    viewModel.selectedRestockProductId.collectAsState()

    //get entered quantity
    val quantity by viewModel.restockQuantity.collectAsState()

    //get message from the viewmodel
    val message by viewModel.message.collectAsState()

    //get context for toast
    val context = LocalContext.current

    //show message when it changes
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
            .padding(16.dp)
    ) {

        //screen title
        Text(
            text = "Restock",
            fontSize = 22.sp
        )

        Spacer(modifier = Modifier.padding(8.dp))

        //enter new quantity
        OutlinedTextField(
            value = quantity,
            onValueChange = {
                viewModel.onRestockQuantityChange(it)
            },
            label = {
                Text("Enter New Quantity")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.padding(8.dp))

        //OK and Cancel buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {

            Button(
                onClick = {
                    viewModel.restock()
                }
            ) {
                Text("OK")
            }

            Button(
                onClick = {
                    viewModel.cancelRestock()
                    onNavigateBack()
                },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("CANCEL")
            }
        }

        Spacer(modifier = Modifier.padding(8.dp))

        //show product list
        LazyColumn {

            items(products) { product ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)
                        .clickable {
                            viewModel.selectRestockProduct(product)
                        }
                ) {

                    Column(
                        modifier = Modifier.padding(10.dp)
                    ) {

                        Text(text = product.name)

                        Text(text = "Price: $${product.price}")

                        Text(text = "Stock: ${product.quantity}")

                        //show selected product
                        if (selectedProductId == product.id) {
                            Text(text = "Selected")
                        }
                    }
                }
            }
        }
    }
}
