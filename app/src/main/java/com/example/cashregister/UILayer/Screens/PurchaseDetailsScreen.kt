
package com.example.cashregister.UILayer.Screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import com.example.cashregister.DataLayer.Purchase
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun PurchaseDetailsScreen(
    purchase: Purchase?,
    onNavigateBack: () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        if (purchase != null) {

            //show product name
            Text(
                text = "Product: ${purchase.productName}",
                fontSize = 20.sp
            )

            //show quantity
            Text(
                text = "Quantity: ${purchase.quantity}"
            )

            //show unit price
            Text(
                text = "Price: $${purchase.unitPrice}"
            )

            //show total price
            Text(
                text = "Total: $${"%.2f".format(purchase.total)}"
            )

            //format and show purchase date
            val dateFormat = SimpleDateFormat(
                "EEE MMM dd HH:mm:ss yyyy",
                Locale.getDefault()
            )

            Text(
                text = "Purchase Date: ${dateFormat.format(purchase.date)}"
            )

        } else {

            Text("Purchase not found")
        }

        //return to purchase history
        Button(
            onClick = onNavigateBack
        ) {
            Text("Back")
        }
    }
}
