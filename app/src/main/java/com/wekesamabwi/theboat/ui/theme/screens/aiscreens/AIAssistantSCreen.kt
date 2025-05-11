package com.wekesamabwi.theboat.ui.theme.screens.aiscreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.tooling.preview.Preview
import com.wekesamabwi.theboat.navigation.ROUT_AIAssistantScreen
import com.wekesamabwi.theboat.navigation.ROUT_CONVERTER
import com.wekesamabwi.theboat.navigation.ROUT_CURRENCYCHART
import com.wekesamabwi.theboat.navigation.ROUT_HOME
import com.wekesamabwi.theboat.navigation.ROUT_MARKET
import com.wekesamabwi.theboat.navigation.ROUT_NOTIFICATION
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIScreen(navController: NavController) {
    var userInput by remember { mutableStateOf(TextFieldValue("")) }
    val messages = remember { mutableStateListOf<Pair<String, String>>() } // Pair<"user"/"ai", message>
    var showMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Assistant", fontSize = 20.sp) },
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
                    }
                }


            )
        },

        bottomBar = {
            com.wekesamabwi.theboat.ui.theme.screens.home.BottomNavigationBar(navController)
        }

    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF1F1F1))
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                messages.forEach { (sender, message) ->
                    if (sender == "user") {
                        UserMessage(message)
                    } else {
                        AIMessage(message)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                BasicTextField(
                    value = userInput,
                    onValueChange = { userInput = it },
                    modifier = Modifier
                        .weight(1f)
                        .background(Color.White, shape = RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    singleLine = true
                )
                IconButton(onClick = {
                    if (userInput.text.isNotBlank()) {
                        messages.add("user" to userInput.text)
                        messages.add("ai" to getAIResponse(userInput.text))
                        userInput = TextFieldValue("")
                    }
                }) {
                    Icon(Icons.Default.Send, contentDescription = "Send")
                }
            }
        }
    }
}

@Composable
fun UserMessage(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 24.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Text(
            text = text,
            color = Color.White,
            modifier = Modifier
                .background(Color(0xFF0A1F44), RoundedCornerShape(10.dp))
                .padding(10.dp)
        )
    }
}

@Composable
fun AIMessage(text: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Text(
            text = text,
            color = Color.Black,
            modifier = Modifier
                .background(Color.LightGray, RoundedCornerShape(10.dp))
                .padding(10.dp)
        )
    }
}

// Simulated AI response logic
fun getAIResponse(userInput: String): String {
    val responses = listOf(
        "Consider diversifying your portfolio for better risk management.",
        "The EUR/USD rate is expected to remain stable this week.",
        "Set a stop-loss to avoid heavy losses in volatile markets.",
        "Use technical indicators like RSI to make smarter trades.",
        "Now might be a good time to monitor the crypto market closely."
    )
    return responses[Random.nextInt(responses.size)]
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
fun AIScreenPreview() {
    AIScreen(navController = rememberNavController())
}
