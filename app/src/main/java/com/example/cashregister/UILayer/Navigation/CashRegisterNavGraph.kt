
package com.example.cashregister.UILayer.Navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cashregister.ViewModel.ProductViewModel
import com.example.cashregister.UILayer.Screens.CashRegisterScreen
import com.example.cashregister.UILayer.Screens.ManagerScreen
import com.example.cashregister.UILayer.Screens.PurchaseHistoryScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.cashregister.UILayer.Screens.PurchaseDetailsScreen
import com.example.cashregister.UILayer.Screens.RestockScreen
@Composable
fun CashRegisterNavGraph(
    viewModel: ProductViewModel,
    innerPadding: PaddingValues
) {

    //create the navigation controller
    val navController = rememberNavController()

    //get purchase history from the viewmodel
    val purchases by viewModel.purchases.collectAsState()

    //define all app screens
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = Modifier.padding(innerPadding)
    ) {

        //cash register screen
        composable("home") {
            CashRegisterScreen(
                viewModel = viewModel,
                onNavigateManager = {
                    navController.navigate("manager")
                }
            )
        }

        //manager screen
        composable("manager") {
            ManagerScreen(
                onNavigateHistory = {
                    navController.navigate("history")
                },
                onNavigateRestock = {
                    navController.navigate("restock")
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        //purchase history screen
        composable("history") {
            PurchaseHistoryScreen(
                purchases = purchases,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onSelectPurchase = { purchaseId ->
                    navController.navigate("history_details/$purchaseId")
                }
            )
        }

        //purchase details screen
        composable(
            route = "history_details/{purchaseId}",
            arguments = listOf(
                navArgument("purchaseId") {
                    type = NavType.IntType
                }
            )
        ) { backStackEntry ->

            //get the purchase id from navigation
            val purchaseId: Int =
                backStackEntry.arguments?.getInt("purchaseId") ?: -1

            //find the purchase using its id
            val purchase = viewModel.getPurchase(purchaseId)

            PurchaseDetailsScreen(
                purchase = purchase,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        //restock screen
        composable("restock") {
            RestockScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
