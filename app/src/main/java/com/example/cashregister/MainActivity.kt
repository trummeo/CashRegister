
package com.example.cashregister

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.cashregister.DataLayer.InMemoryProductRepository
import com.example.cashregister.UILayer.Navigation.CashRegisterNavGraph
import com.example.cashregister.ViewModel.ProductViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        //create the repository
        val productRepository = InMemoryProductRepository()

        //create the viewmodel using its factory
        val productViewModel: ProductViewModel by viewModels {
            ProductViewModel.provideFactory(productRepository)
        }

        setContent {
            Scaffold(
                modifier = Modifier.fillMaxSize()
            ) { innerPadding ->

                //start the navigation graph
                CashRegisterNavGraph(
                    viewModel = productViewModel,
                    innerPadding = innerPadding
                )
            }
        }
    }
}
