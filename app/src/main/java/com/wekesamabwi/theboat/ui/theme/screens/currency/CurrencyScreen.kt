package com.wekesamabwi.theboat.ui.theme.screens.currency

import androidx.navigation.compose.rememberNavController
import android.graphics.Color as AndroidColor
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController

// MPAndroidChart imports
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.wekesamabwi.theboat.navigation.ROUT_AIAssistantScreen
import com.wekesamabwi.theboat.navigation.ROUT_CONVERTER
import com.wekesamabwi.theboat.navigation.ROUT_CURRENCYCHART
import com.wekesamabwi.theboat.navigation.ROUT_HOME
import com.wekesamabwi.theboat.navigation.ROUT_MARKET
import com.wekesamabwi.theboat.navigation.ROUT_NOTIFICATION


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyChartScreen(navController: NavController) {
    var showMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Currency Trends", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
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
                .padding(16.dp)
        ) {
            Text(
                "7-Day Exchange Rate: USD → NGN",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(16.dp))
            CurrencyLineChart()
        }
    }
}
@Composable
fun CurrencyLineChart() {
    val context = LocalContext.current

    val mockRates = listOf(1350f, 1380f, 1375f, 1400f, 1390f, 1420f, 1410f)

    AndroidView(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        factory = {
            val chart = LineChart(context).apply {
                setTouchEnabled(true)
                setPinchZoom(true)
                xAxis.position = XAxis.XAxisPosition.BOTTOM
                xAxis.granularity = 1f
                axisRight.isEnabled = false
                description = Description().apply {
                    text = "Day"
                    textColor = AndroidColor.GRAY
                }

                val entries = mockRates.mapIndexed { index, rate ->
                    Entry(index.toFloat(), rate)
                }

                val dataSet = LineDataSet(entries, "USD to NGN").apply {
                    color = ContextCompat.getColor(context, android.R.color.holo_blue_dark)
                    setCircleColor(AndroidColor.BLACK)
                    lineWidth = 2f
                    circleRadius = 4f
                    setDrawFilled(true)
                    fillColor = ContextCompat.getColor(context, android.R.color.holo_blue_light)
                    valueTextSize = 10f
                }

                data = LineData(listOf(dataSet as ILineDataSet))
            }
            chart.invalidate()
            chart
        }
    )
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
fun CurrencyChartScreenPreview() {
    CurrencyChartScreen(rememberNavController())
}
