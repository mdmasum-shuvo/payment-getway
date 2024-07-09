package com.masum.payment_getway

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.masum.payment_getway.ui.theme.PaymentgetwayTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PaymentgetwayTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
   Column(modifier = Modifier
       .fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
       verticalArrangement = Arrangement.Center

    ) {
       val context = LocalContext.current
       Button(onClick = { context.startActivity(Intent(context, BkashPaymentActivity::class.java)) }) {
           Text(text = "Open Bkash")

       }
   }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PaymentgetwayTheme {
        Greeting("Android")
    }
}