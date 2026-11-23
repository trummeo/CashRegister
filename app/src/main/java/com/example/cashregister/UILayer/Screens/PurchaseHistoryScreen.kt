
package com.example.cashregister.UILayer.Screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cashregister.DataLayer.Purchase
import androidx.compose.material3.Button


@Composable
fun PurchaseHistoryScreen(
    purchases: List<Purchase>,
    onNavigateBack: () -> Unit,
    onSelectPurchase: (Int) -> Unit
) {

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        // back button
        Button(
            onClick = onNavigateBack
        ) {
            Text("Back")
        }

        //screen title
        Text(
            text = "Purchase History",
            fontSize = 22.sp
        )

        //show purchase history
        LazyColumn {

            items(purchases) { purchase ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {
                            onSelectPurchase(purchase.id)
                        }
                ) {

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {

                        //show product and quantity
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(text = purchase.productName)
                            Text(text = "Quantity: ${purchase.quantity}")
                        }

                        //show total price
                        Text(
                            text = "$${"%.2f".format(purchase.total)}"
                        )
                    }
                }
            }
        }
    }
}
