package com.wekesamabwi.theboat.ui.theme.screens.converter

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.wekesamabwi.theboat.navigation.ROUT_CURRENCYCHART
import com.wekesamabwi.theboat.navigation.ROUT_HOME

@Composable
fun ConverterScreen(navController: NavController) {
    CurrencyConverterApp(rememberNavController(), viewModel = CurrencyViewModel())
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyConverterApp(navController:NavController,viewModel: CurrencyViewModel = viewModel()) {

    val amount by viewModel.amount
    val selectedFromCurrency by viewModel.fromCurrency
    val selectedToCurrency by viewModel.toCurrency
    val convertedAmount by viewModel.convertedAmount
    val alertMessage by viewModel.alertMessage

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Currency Converter + AI", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White)
                },
               colors = TopAppBarDefaults.topAppBarColors(
                   containerColor = Color(0xFF0A1F44),
                   titleContentColor = Color.White
               ),
                actions = {
                    IconButton(onClick = {navController.navigate(ROUT_CURRENCYCHART)}) {
                        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "", tint = Color.White)
                    }
                }
            )
        },

    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedTextField(
                value = amount,
                onValueChange = { viewModel.updateAmount(it) },
                label = { Text("Enter Amount") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = MaterialTheme.shapes.medium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("From Currency", fontWeight = FontWeight.SemiBold)
            CurrencyRowSelector(
                options = viewModel.currencies,
                selected = selectedFromCurrency,
                onSelected = { viewModel.updateFromCurrency(it) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("To Currency", fontWeight = FontWeight.SemiBold)
            CurrencyRowSelector(
                options = viewModel.currencies,
                selected = selectedToCurrency,
                onSelected = { viewModel.updateToCurrency(it) }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.convertCurrency() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF0A1F44)),
                shape = RoundedCornerShape(10.dp)


                ) {
                Text("Convert")
            }




            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEEF7FF))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Converted Amount", fontWeight = FontWeight.Medium)
                    Text(
                        text = "$convertedAmount",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0A1F44)
                    )
                    if (alertMessage.isNotBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "AI Assistant: $alertMessage",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CurrencyRowSelector(
    options: List<String>,
    selected: String,
    onSelected: (String) -> Unit
) {
    LazyRow(modifier = Modifier.padding(8.dp)) {
        items(options.size) { index ->
            val option = options[index]
            Card(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clickable { onSelected(option) },
                colors = CardDefaults.cardColors(
                    containerColor = if (selected == option) Color(0xFF1A73E8) else Color(0xFFF1F3F4)
                ),
                shape = MaterialTheme.shapes.medium,
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = option,
                        color = if (selected == option) Color.White else Color.Black
                    )
                }
            }
        }
    }
}

// ---------------- VIEWMODEL ----------------

class CurrencyViewModel : ViewModel() {
    private val _amount = mutableStateOf("")
    val amount: State<String> = _amount

    private val _fromCurrency = mutableStateOf("USD")
    val fromCurrency: State<String> = _fromCurrency

    private val _toCurrency = mutableStateOf("EUR")
    val toCurrency: State<String> = _toCurrency

    private val _convertedAmount = mutableStateOf("0.00")
    val convertedAmount: State<String> = _convertedAmount

    private val _alertMessage = mutableStateOf("")
    val alertMessage: State<String> = _alertMessage

    val currencies = listOf("USD", "EUR", "KES", "GBP", "JPY", "NGN")

    fun updateAmount(newAmount: String) {
        _amount.value = newAmount
    }

    fun updateFromCurrency(currency: String) {
        _fromCurrency.value = currency
    }

    fun updateToCurrency(currency: String) {
        _toCurrency.value = currency
    }

    fun convertCurrency() {
        val rate = getExchangeRate(_fromCurrency.value, _toCurrency.value)
        val input = _amount.value.toDoubleOrNull() ?: 0.0
        val result = input * rate
        _convertedAmount.value = "%.2f".format(result)
        simulateAiAlert(rate, result)
    }

    private fun getExchangeRate(from: String, to: String): Double {
        val exchangeRates = mapOf(
            "USD_EUR" to 0.93,
            "USD_KES" to 130.0,
            "USD_GBP" to 0.80,
            "USD_JPY" to 150.0,
            "USD_NGN" to 1400.0,
            "EUR_USD" to 1.08,
            "KES_USD" to 0.0077,
            "GBP_USD" to 1.25,
            "JPY_USD" to 0.0067,
            "NGN_USD" to 0.00071
        )

        val key = "${from}_${to}"
        return if (from == to) 1.0 else exchangeRates[key] ?: 1.0
    }

    private fun simulateAiAlert(rate: Double, profit: Double) {
        _alertMessage.value = when {
            rate > 1.2 -> "High rate detected! Consider trading now!"
            profit > 1000 -> "You made a good profit: $profit"
            else -> "Exchange done successfully."
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConverterScreenPreview() {
    ConverterScreen(navController = rememberNavController())
}
