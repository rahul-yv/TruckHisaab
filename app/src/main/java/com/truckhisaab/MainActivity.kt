package com.truckhisaab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.truckhisaab.ui.navigation.TruckHisaabNavGraph
import com.truckhisaab.ui.theme.TruckHisaabTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TruckHisaabTheme {
                TruckHisaabNavGraph()
            }
        }
    }
}
