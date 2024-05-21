package com.example.currencyconverter

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CurrencyConverterApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyConverterApp() {
    var inputValue by remember { mutableStateOf("") }
    var inputCurrency by remember { mutableStateOf("FC") }
    var outputCurrency by remember { mutableStateOf("USD") }
    val currencies = listOf("FC", "USD", "EUR", "ZMW")
    var result by remember { mutableStateOf("") }

    fun convert(value: Double, from: String, to: String): Double {
        val exchangeRates = mapOf(
            "FC" to 1.0,
            "USD" to 2000.0,
            "EUR" to 2400.0,
            "ZMW" to 150.0
        )
        val fromRate = exchangeRates[from] ?: 1.0
        val toRate = exchangeRates[to] ?: 1.0
        return value / fromRate * toRate
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = "Convertisseur de devises", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = inputValue,
            onValueChange = { inputValue = it },
            label = { Text("Montant à convertir") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CurrencyDropdownMenu(
                currencies = currencies,
                selectedCurrency = inputCurrency,
                onCurrencySelected = { inputCurrency = it }
            )

            CurrencyDropdownMenu(
                currencies = currencies,
                selectedCurrency = outputCurrency,
                onCurrencySelected = { outputCurrency = it }
            )
        }

        Button(onClick = {
            val value = inputValue.toDoubleOrNull()
            if (value != null) {
                result = convert(value, inputCurrency, outputCurrency).toString()
            } else {
                result = "Entrée invalide"
            }
        }) {
            Text("Convertir")
        }

        Text(text = "Résultat: $result", fontSize = 20.sp)
    }
}

@Composable
fun CurrencyDropdownMenu(currencies: List<String>, selectedCurrency: String, onCurrencySelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.wrapContentSize()) {
        TextButton(onClick = { expanded = !expanded }) {
            Text(text = selectedCurrency)
            Icon(imageVector = if (expanded) Icons.Filled.ArrowDropDown else Icons.Filled.ArrowDropDown, contentDescription = null)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            currencies.forEach { currency ->
                DropdownMenuItem(onClick = {
                    onCurrencySelected(currency)
                    expanded = false
                }) {
                    //Text(text = currency)
                }
            }
        }
    }
}

fun DropdownMenuItem(onClick: () -> Unit, interactionSource: () -> Unit) {

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CurrencyConverterApp()
}
