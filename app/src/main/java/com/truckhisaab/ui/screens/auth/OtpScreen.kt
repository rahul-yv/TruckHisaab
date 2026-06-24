package com.truckhisaab.ui.screens.auth

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.truckhisaab.ui.components.THPrimaryButton
import com.truckhisaab.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun OtpScreen(phone: String, onVerified: () -> Unit, onBack: () -> Unit) {
    var otp by remember { mutableStateOf("") }
    var timer by remember { mutableIntStateOf(45) }
    var loading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { while (timer > 0) { delay(1000); timer-- } }
    LaunchedEffect(otp) { if (otp.length == 6) { loading = true; delay(1000); onVerified() } }

    Column(Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.Center) {
        Text("OTP Verification", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text("+91 $phone pe OTP bheja gaya", fontSize = 14.sp, color = TextSecondary)
        Spacer(Modifier.height(32.dp))

        BasicTextField(
            value = otp,
            onValueChange = { if (it.length <= 6 && it.all { c -> c.isDigit() }) otp = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            decorationBox = {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    repeat(6) { i ->
                        Box(
                            Modifier.size(48.dp).border(2.dp, if (i == otp.length) TruckRed else Divider, RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) { Text(otp.getOrNull(i)?.toString() ?: "", fontSize = 20.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center) }
                    }
                }
            }
        )

        Spacer(Modifier.height(24.dp))
        if (timer > 0) {
            Text("Resend in ${timer}s", color = TextSecondary, fontSize = 14.sp)
        } else {
            TextButton(onClick = { timer = 45 }) { Text("OTP Dobara Bhejein", color = TruckRed) }
        }

        Spacer(Modifier.height(24.dp))
        THPrimaryButton("Verify", onClick = { loading = true; onVerified() }, loading = loading, enabled = otp.length == 6, modifier = Modifier.fillMaxWidth())
        Spacer(Modifier.height(16.dp))
        TextButton(onClick = onBack, Modifier.align(Alignment.CenterHorizontally)) { Text("Number badlein", color = TextSecondary) }
    }
}
