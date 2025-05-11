package com.wekesamabwi.theboat.ui.theme.screens.home

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.wekesamabwi.theboat.R // Replace with your actual resource package name
import com.wekesamabwi.theboat.navigation.ROUT_AIAssistantScreen
import com.wekesamabwi.theboat.navigation.ROUT_CONVERTER
import com.wekesamabwi.theboat.navigation.ROUT_CURRENCYCHART
import com.wekesamabwi.theboat.navigation.ROUT_HOME
import com.wekesamabwi.theboat.navigation.ROUT_MARKET
import com.wekesamabwi.theboat.navigation.ROUT_NOTIFICATION

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {

    var showMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("TheBoat", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0A1F44),
                    titleContentColor = Color.White
                ),
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
            BottomNavigationBar(navController)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Card items with respective images and descriptions
            CardItem("Market", "Explore various markets and trading opportunities",
                R.drawable.img
            ) {
                navController.navigate(ROUT_MARKET)
            }
            CardItem("Alerts", "Get notified of important market changes", R.drawable.img_2) {
                navController.navigate("alterscreen")
            }
            CardItem("Notifications", "Stay updated with real-time alerts", R.drawable.img_3) {
                navController.navigate("notificationscreen")
            }
            CardItem("AI Assistance", "Let AI guide you on your investment journey",
                R.drawable.img_4
            ) {
                navController.navigate("ai_assistance")
            }
            CardItem("Currency Converter", "Convert currencies instantly and contact support for trading assistance.",
                R.drawable.img_5
            ) {
                navController.navigate(ROUT_CONVERTER)
            }

            CardItem("Currency Trends", "Track 7-day exchange rate trends to make informed trading decisions.",
                R.drawable.img_6,
            ) {
                navController.navigate(ROUT_CURRENCYCHART)

            }
        }
    }
}
@Composable
fun CardItem(
    title: String,
    description: String,
    imageRes: Int,
    onClick: () -> Unit
) {
    var isHovered by remember { mutableStateOf(false) }

    val elevation by animateDpAsState(targetValue = if (isHovered) 8.dp else 4.dp)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clickable { onClick() }
            .padding(8.dp)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isHovered = true // Set hovered when pressed
                        try {
                            // Wait until the press is released
                            awaitRelease()
                        } finally {
                            // Ensure hover is always reset
                            isHovered = false
                        }
                    }
                )
            },

        colors = CardDefaults.cardColors(containerColor = Color.LightGray),
        elevation = CardDefaults.cardElevation(elevation)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .height(100.dp)
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                modifier = Modifier.size(50.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(text = title, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = description, fontSize = 14.sp, color = Color.Gray)
            }
        }
    }
}


@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar(containerColor = (Color(0xFF0A1F44))) {
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate(ROUT_HOME) },
            icon = { Icon(Icons.Default.Home, contentDescription = "Home", tint = Color.White) },
            label = { Text("Home", color = Color.White ) }
        )
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate(ROUT_MARKET) },
            icon = { Icon(Icons.Default.Info, contentDescription = "Market", tint = Color.White) },
            label = { Text("Market", color = Color.White) }
        )
        NavigationBarItem(
            selected = false,
            onClick = { navController.navigate(ROUT_AIAssistantScreen) },
            icon = { Icon(Icons.Default.CheckCircle, contentDescription = "AI", tint = Color.White) },
            label = { Text("AI", color = Color.White) }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(navController = rememberNavController())
}
