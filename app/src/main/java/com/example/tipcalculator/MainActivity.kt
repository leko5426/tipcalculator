/**
 * Application : tipCalculator
 * Ce programme permet de :
 *  - Saisir un montant de facture
 *  - Indiquer un pourcentage de pourboire
 *  - Choisir d’arrondir ou non le montant
 *  - Activer ou désactiver un mode sombre via un bouton bascule (Switch)
 */

package com.example.tipcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.text.NumberFormat
import kotlin.math.ceil

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {

            var darkModeEnabled by remember { mutableStateOf(false) }
            val colorScheme = if (darkModeEnabled) darkColorScheme() else lightColorScheme()

            MaterialTheme(colorScheme = colorScheme) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(20.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // 🔘 Switch pour activer/désactiver le mode sombre
                        DarkModeSwitch(
                            darkModeEnabled = darkModeEnabled,
                            onToggle = { darkModeEnabled = it }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        TipTimeLayout()
                    }
                }
            }
        }
    }
}

@Composable
fun TipTimeLayout() {
    var amountInput by remember { mutableStateOf("") }
    val amount = amountInput.toDoubleOrNull() ?: 0.0

    var tipInput by remember { mutableStateOf("") }
    val tipPercent = tipInput.toDoubleOrNull() ?: 0.0

    var roundUp by remember { mutableStateOf(false) }

    val tip = calculateTip(amount, tipPercent, roundUp)

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Calcul du pourboire",
            modifier = Modifier.padding(bottom = 16.dp),
            style = MaterialTheme.typography.headlineSmall
        )

        EditNumberField(
            label = "Montant de la facture",
            iconRes = android.R.drawable.ic_menu_info_details,
            value = amountInput,
            onValueChanged = { amountInput = it },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        EditNumberField(
            label = "Pourcentage du pourboire",
            iconRes = android.R.drawable.ic_menu_sort_by_size,
            value = tipInput,
            onValueChanged = { tipInput = it },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        )

        RoundTheTipRow(
            roundUp = roundUp,
            onRoundUpChanged = { roundUp = it },
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Text(
            text = "Pourboire : $tip",
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

@Composable
fun EditNumberField(
    label: String,
    @DrawableRes iconRes: Int? = null,
    value: String,
    onValueChanged: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChanged,
        label = { Text(label) },
        singleLine = true,
        keyboardOptions = keyboardOptions,
        leadingIcon = iconRes?.let {
            { Icon(painter = painterResource(id = it), contentDescription = null) }
        },
        modifier = modifier
    )
}
@Composable
fun RoundTheTipRow(
    roundUp: Boolean,
    onRoundUpChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Arrondir le pourboire ?")
        Switch(
            checked = roundUp,
            onCheckedChange = onRoundUpChanged,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End)
        )
    }
}

@Composable
fun DarkModeSwitch(
    darkModeEnabled: Boolean,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Mode sombre")
        Switch(
            checked = darkModeEnabled,
            onCheckedChange = onToggle,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End)
        )
    }
}

private fun calculateTip(amount: Double, tipPercent: Double = 15.0, roundUp: Boolean): String {
    var tip = tipPercent / 100 * amount
    if (roundUp) tip = ceil(tip)
    return NumberFormat.getCurrencyInstance().format(tip)
}

@Preview(showBackground = true)
@Composable
fun TipTimeLayoutPreview() {
    MaterialTheme {
        TipTimeLayout()
    }
}
