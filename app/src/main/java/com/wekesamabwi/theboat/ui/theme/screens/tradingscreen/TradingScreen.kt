import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun TradingScreen(navController: NavController) {
    var isBuyMode by remember { mutableStateOf(true) }
    var amount by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // Top bar
        Text(
            text = "Trading",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Balance card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E88E5)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Available Balance", color = Color.White, fontSize = 14.sp)
                Text("$12,540.25", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Buy/Sell toggle
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            Button(
                onClick = { isBuyMode = true },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isBuyMode) Color.Green else Color.LightGray
                )
            ) {
                Text("Buy")
            }
            Button(
                onClick = { isBuyMode = false },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (!isBuyMode) Color.Red else Color.LightGray
                )
            ) {
                Text("Sell")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Market chart placeholder
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
        ) {
            Text(
                "Chart Placeholder",
                modifier = Modifier.align(Alignment.Center),
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Amount input
        OutlinedTextField(
            value = amount,
            onValueChange = { amount = it },
            label = { Text("Enter Amount") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Buy/Sell button
        Button(
            onClick = {
                // Handle trade action
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isBuyMode) Color.Green else Color.Red
            )
        ) {
            Text(text = if (isBuyMode) "Buy Now" else "Sell Now", fontSize = 16.sp)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TradingScreenPreview() {
    TradingScreen(rememberNavController())
}
