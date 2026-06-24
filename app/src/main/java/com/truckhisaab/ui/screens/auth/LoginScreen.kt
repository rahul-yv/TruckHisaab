package com.truckhisaab.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.truckhisaab.ui.components.THPrimaryButton
import com.truckhisaab.ui.theme.*
import com.truckhisaab.util.Validators

@Composable
fun LoginScreen(onOtpSent: (String) -> Unit, onSkip: () -> Unit) {
    var phone by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }
    var termsAccepted by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.Center) {
        Text("Namaste!", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = TruckRed)
        Spacer(Modifier.height(8.dp))
        Text("Apna phone number dalein", fontSize = 16.sp, color = TextSecondary)
        Spacer(Modifier.height(32.dp))

        OutlinedTextField(
            value = phone,
            onValueChange = { if (it.length <= 10 && it.all { c -> c.isDigit() }) { phone = it; error = null } },
            label = { Text("Phone Number") },
            prefix = { Text("+91 ", fontWeight = FontWeight.Bold) },
            leadingIcon = { Icon(Icons.Default.Phone, null) },
            isError = error != null,
            supportingText = error?.let { { Text(it, color = DangerRed) } },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = termsAccepted, onCheckedChange = { termsAccepted = it })
            Text("Main Terms & Privacy Policy se sahmat hoon", fontSize = 12.sp, color = TextSecondary)
        }

        Spacer(Modifier.height(24.dp))
        THPrimaryButton(
            text = "OTP Bhejein",
            onClick = {
                if (!Validators.isValidPhone(phone)) { error = "Sahi phone number dalein" }
                else if (!termsAccepted) { error = "Terms accept karein" }
                else { loading = true; onOtpSent(phone) }
            },
            loading = loading,
            enabled = phone.length == 10 && termsAccepted,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))
        TextButton(onClick = onSkip, Modifier.align(Alignment.CenterHorizontally)) {
            Text("Skip - Baad mein", color = TextSecondary)
        }
    }
}
