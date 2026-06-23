package com.truckhisaab.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.truckhisaab.ui.components.THPrimaryButton
import com.truckhisaab.ui.theme.DangerRed
import com.truckhisaab.ui.theme.TextSecondary
import com.truckhisaab.ui.theme.TruckRed
import kotlinx.coroutines.delay

@Composable
fun OtpScreen(phone: String, onVerified: () -> Unit, onBack: () -> Unit) {
    var otp by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }
    var timer by remember { mutableIntStateOf(30) }
    var canResend by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (timer > 0) { delay(1000); timer-- }
        canResend = true
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White).padding(24.dp)
    ) {
        Spacer(Modifier.height(48.dp))

        Text("OTP daalein", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text("+91 $phone pe OTP bheja gaya hai", fontSize = 14.sp, color = TextSecondary)

        Spacer(Modifier.height(40.dp))

        OtpInputField(
            otp = otp,
            onOtpChange = { if (it.length <= 6) { otp = it; showError = false } },
            isError = showError
        )

        if (showError) {
            Spacer(Modifier.height(8.dp))
            Text("Galat OTP. Dobara koshish karein.", color = DangerRed, fontSize = 13.sp, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        }

        Spacer(Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            if (canResend) {
                TextButton(onClick = { timer = 30; canResend = false }) {
                    Text("OTP dobara bhejo", color = TruckRed)
                }
            } else {
                Text("Dobara bhejo: ${timer}s", color = TextSecondary, fontSize = 14.sp)
            }
        }

        Spacer(Modifier.height(24.dp))

        THPrimaryButton(
            text = "Verify",
            onClick = {
                if (otp.length == 6) onVerified()
                else showError = true
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = otp.length == 6
        )

        Spacer(Modifier.weight(1f))

        TextButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text("← Number badlein", color = TextSecondary)
        }
    }
}

@Composable
private fun OtpInputField(otp: String, onOtpChange: (String) -> Unit, isError: Boolean) {
    BasicTextField(
        value = otp,
        onValueChange = onOtpChange,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        decorationBox = {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                repeat(6) { index ->
                    val char = otp.getOrNull(index)?.toString() ?: ""
                    val borderColor = when {
                        isError -> DangerRed
                        char.isNotEmpty() -> TruckRed
                        else -> Color.LightGray
                    }
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .border(2.dp, borderColor, RoundedCornerShape(10.dp))
                            .background(if (char.isNotEmpty()) TruckRed.copy(alpha = 0.05f) else Color.White, RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(char, fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TruckRed)
                    }
                    if (index < 5) Spacer(Modifier.width(8.dp))
                }
            }
        }
    )
}
