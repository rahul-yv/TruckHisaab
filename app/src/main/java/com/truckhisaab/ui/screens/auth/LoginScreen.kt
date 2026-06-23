package com.truckhisaab.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.truckhisaab.ui.components.THPrimaryButton
import com.truckhisaab.ui.theme.DangerRed
import com.truckhisaab.ui.theme.TextSecondary
import com.truckhisaab.ui.theme.WhatsAppGreen

@Composable
fun LoginScreen(onOtpSent: (String) -> Unit, onSkip: () -> Unit) {
    var phone by remember { mutableStateOf("") }
    var termsAccepted by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }

    val isValidPhone = phone.length == 10 && phone.all { it.isDigit() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
    ) {
        Spacer(Modifier.height(48.dp))

        Text("Apna number daalein", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text("OTP se verify karenge", fontSize = 14.sp, color = TextSecondary)

        Spacer(Modifier.height(32.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("+91", fontSize = 18.sp, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(end = 8.dp))
            OutlinedTextField(
                value = phone,
                onValueChange = { if (it.length <= 10) { phone = it; showError = false } },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("98765 43210") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(12.dp),
                singleLine = true,
                isError = showError
            )
        }

        if (showError) {
            Spacer(Modifier.height(4.dp))
            Text("Sahi number daalein (10 digit)", color = DangerRed, fontSize = 12.sp)
        }

        Spacer(Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = termsAccepted, onCheckedChange = { termsAccepted = it })
            Spacer(Modifier.width(4.dp))
            Text("Terms & Conditions maanta/maanti hoon", fontSize = 13.sp, color = TextSecondary)
        }

        Spacer(Modifier.height(24.dp))

        THPrimaryButton(
            text = "OTP Bhejo",
            onClick = {
                if (isValidPhone && termsAccepted) onOtpSent(phone)
                else showError = true
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = isValidPhone && termsAccepted
        )

        Spacer(Modifier.height(16.dp))

        THPrimaryButton(
            text = "WhatsApp se Login",
            onClick = { onSkip() },
            modifier = Modifier.fillMaxWidth(),
            color = WhatsAppGreen
        )

        Spacer(Modifier.weight(1f))

        androidx.compose.material3.TextButton(
            onClick = onSkip,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Skip karein →", color = TextSecondary, fontSize = 14.sp)
        }
    }
}
