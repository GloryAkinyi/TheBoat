package com.wekesamabwi.theboat.ui.theme.screens.marketscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.wekesamabwi.theboat.navigation.ROUT_AIAssistantScreen
import com.wekesamabwi.theboat.navigation.ROUT_CONVERTER
import com.wekesamabwi.theboat.navigation.ROUT_CURRENCYCHART
import com.wekesamabwi.theboat.navigation.ROUT_MARKET
import com.wekesamabwi.theboat.navigation.ROUT_NOTIFICATION
import com.wekesamabwi.theboat.navigation.ROUT_TRADE

data class MarketPlatform(
    val name: String,
    val description: String,
    val price: String,
    val growth: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketScreen(navController: NavHostController) {
    var searchQuery by remember { mutableStateOf("") }
    var showMenu by remember { mutableStateOf(false) }




    val allPlatforms = listOf(
        MarketPlatform("Binance", "Top crypto exchange", "Ksh. 120,000", "+4.2%"),
        MarketPlatform("Coinbase", "Secure trading globally", "Ksh. 115,000", "+2.1%"),
        MarketPlatform("Kraken", "Deep liquidity exchange", "Ksh. 118,000", "-1.4%"),
        MarketPlatform("Bitstamp", "Europe-based platform", "Ksh. 110,000", "+3.8%"),
        MarketPlatform("eToro", "Multi-asset trading", "Ksh. 112,500", "+1.7%")
    )

    val filteredPlatforms = allPlatforms.filter {
        it.name.contains(searchQuery, ignoreCase = true) || it.description.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Markets") },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0A1F44), titleContentColor = Color.White),
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.Default.Menu, contentDescription = "", tint = Color.White)
                    }
                },
                actions = {

                    IconButton(onClick = { showMenu = true }) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Menu", tint = Color.White)
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Currency Converter") },
                            onClick = {
                                navController.navigate(ROUT_CONVERTER)
                                showMenu = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Currency Chart") },
                            onClick = {
                                navController.navigate(ROUT_CURRENCYCHART)
                                showMenu = false
                            }

                        )

                        DropdownMenuItem(
                            text = { Text("Market") },
                            onClick = {
                                navController.navigate(ROUT_MARKET)
                                showMenu = false
                            }

                        )

                        DropdownMenuItem(
                            text = { Text("Notifications") },
                            onClick = {
                                navController.navigate(ROUT_NOTIFICATION)
                                showMenu = false
                            }

                        )
                        DropdownMenuItem(
                            text = { Text("AI Assistance") },
                            onClick = {
                                navController.navigate(ROUT_AIAssistantScreen)
                                showMenu = false
                            }

                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color(0xFF0A1F44)) {
                NavigationBarItem(
                    selected = false,
                    onClick = { /* Navigate to home */ },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    colors = NavigationBarItemDefaults.colors(unselectedIconColor = Color.White, unselectedTextColor = Color.White)
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { /* Stay on market */ },
                    icon = { Icon(Icons.Default.Info, contentDescription = "Markets") },
                    label = { Text("Markets") },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = Color.Green, selectedTextColor = Color.Green)
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { /* Navigate to profile */ },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    colors = NavigationBarItemDefaults.colors(unselectedIconColor = Color.White, unselectedTextColor = Color.White)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search market platforms...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredPlatforms) { platform ->
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE7F1FF)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate(ROUT_TRADE)
                            }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = platform.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = Color(0xFF0A1F44)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = platform.description,
                                fontSize = 14.sp,
                                color = Color.DarkGray,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                Text(text = platform.price, fontWeight = FontWeight.Medium)
                                Text(
                                    text = platform.growth,
                                    color = if (platform.growth.contains("-")) Color.Red else Color.Green,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MarketScreenPreview() {
    MarketScreen(navController = rememberNavController())
}
