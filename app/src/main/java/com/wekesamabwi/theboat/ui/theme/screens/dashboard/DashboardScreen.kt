package com.wekesamabwi.theboat.ui.theme.screens.dashboard

import android.app.Application
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.room.*
import kotlinx.coroutines.launch

// ---------------- ROOM SETUP -------------------

@Entity(tableName = "balance_table")
data class BalanceEntity(
    @PrimaryKey val id: Int = 0,
    val amount: Double
)

@Dao
interface BalanceDao {
    @Query("SELECT * FROM balance_table WHERE id = 0")
    suspend fun getBalance(): BalanceEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBalance(balance: BalanceEntity)
}

@Database(entities = [BalanceEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun balanceDao(): BalanceDao
}

class BalanceViewModel(application: Application) : AndroidViewModel(application) {
    private val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        "app_db"
    ).build()

    private val _balance = mutableStateOf(0.0)
    val balance: State<Double> = _balance

    init {
        viewModelScope.launch {
            val savedBalance = db.balanceDao().getBalance()
            _balance.value = savedBalance?.amount ?: 0.0
        }
    }

    fun updateBalance(newAmount: Double) {
        viewModelScope.launch {
            db.balanceDao().insertBalance(BalanceEntity(amount = newAmount))
            _balance.value = newAmount
        }
    }
}

class BalanceViewModelFactory(private val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return BalanceViewModel(app) as T
    }
}

// ---------------- UI SECTION --------------------

object NavigationRoutes {
    const val PROFILE = "profile"
    const val ALERTS = "alerts"
    const val CONVERTER = "converter"
    const val PLATFORMS_LIST = "platforms"
    const val ALERT_DETAILS = "alert_details/{id}"

    fun getAlertDetailsRoute(id: String) = "alert_details/$id"
}

data class Conversion(
    val fromAmount: Double,
    val fromCurrency: String,
    val toAmount: Double,
    val toCurrency: String,
    val date: String
)

data class Alert(
    val id: String,
    val title: String,
    val description: String,
    val icon: ImageVector
)

val sampleConversions = listOf(
    Conversion(100.0, "USD", 91.23, "EUR", "2025-04-15"),
    Conversion(5000.0, "EUR", 5432.10, "USD", "2025-04-14"),
    Conversion(250.0, "GBP", 322.45, "USD", "2025-04-13")
)

val sampleAlerts = listOf(
    Alert("1", "Profitable conversion opportunity", "USD to EUR rate has improved by 2.3%", Icons.Default.Warning),
    Alert("2", "Rate alert", "JPY dropping against major currencies", Icons.Default.Warning)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(navController: NavController) {
    val context = LocalContext.current
    val viewModel: BalanceViewModel = viewModel(
        factory = BalanceViewModelFactory(context.applicationContext as Application)
    )

    var showMenu by remember { mutableStateOf(false) }
    var newAmount by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Trader Dashboard", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0A1F44), titleContentColor = Color.White),
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Default.Menu, contentDescription = "", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = Color.White)
                    }
                    DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                        DropdownMenuItem(text = { Text("Currency Converter") }, onClick = {
                            navController.navigate(NavigationRoutes.CONVERTER); showMenu = false
                        })
                        DropdownMenuItem(text = { Text("Currency Chart") }, onClick = {
                            navController.navigate(NavigationRoutes.CONVERTER); showMenu = false
                        })
                        DropdownMenuItem(text = { Text("Market") }, onClick = {
                            navController.navigate(NavigationRoutes.PLATFORMS_LIST); showMenu = false
                        })
                        DropdownMenuItem(text = { Text("AI Assistance") }, onClick = {
                            navController.navigate("ai_assistant"); showMenu = false
                        })
                        DropdownMenuItem(text = { Text("Profile") }, onClick = {
                            navController.navigate(NavigationRoutes.PROFILE); showMenu = false
                        })
                        DropdownMenuItem(text = { Text("Alerts") }, onClick = {
                            navController.navigate(NavigationRoutes.ALERTS); showMenu = false
                        })
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar(containerColor = Color(0xFF0A1F44)) {
                NavigationBarItem(icon = { Icon(Icons.Default.Home, contentDescription = "", tint = Color.White) },
                    label = { Text("Home", color = Color.White) }, selected = true, onClick = {})
                NavigationBarItem(icon = { Icon(Icons.Default.Settings, contentDescription = "", tint = Color.White) },
                    label = { Text("Convert", color = Color.White) }, selected = false,
                    onClick = { navController.navigate(NavigationRoutes.CONVERTER) })
                NavigationBarItem(icon = { Icon(Icons.Default.DateRange, contentDescription = "", tint = Color.White) },
                    label = { Text("Trade", color = Color.White) }, selected = false,
                    onClick = { navController.navigate(NavigationRoutes.PLATFORMS_LIST) })
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Current Balance", style = MaterialTheme.typography.titleMedium)
                        Text("Ksh.${String.format("%.2f", viewModel.balance.value)}", style = MaterialTheme.typography.headlineMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = newAmount,
                            onValueChange = { newAmount = it },
                            label = { Text("Enter new trading amount") },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF0A1F44),
                                unfocusedBorderColor = Color(0xFF0A1F44)
                            )

                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = {
                            newAmount.toDoubleOrNull()?.let {
                                viewModel.updateBalance(it)
                                newAmount = ""
                            }
                        },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF0A1F44),
                                contentColor = Color.White
                            )) {
                            Text("Save")
                        }
                    }
                }
            }

            item { Text("Recent Conversions", style = MaterialTheme.typography.titleLarge) }

            items(sampleConversions) { conversion ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = "${conversion.fromAmount} ${conversion.fromCurrency} to ${conversion.toCurrency}",
                                style = MaterialTheme.typography.titleMedium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = conversion.date,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                        }
                        Text(
                            text = "${conversion.toAmount}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }


            item { Text("AI Assistant Alerts", style = MaterialTheme.typography.titleLarge) }

            items(sampleAlerts) { alert ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { navController.navigate(NavigationRoutes.getAlertDetailsRoute(alert.id)) }
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(alert.icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(alert.title, style = MaterialTheme.typography.titleMedium)
                            Text(alert.description, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    DashboardScreen(navController = rememberNavController())
}
