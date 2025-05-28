package com.example.zadanie2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import com.example.zadanie2.Presentation.Navigation.NavGraph
import com.example.zadanie2.di.provideKoinModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        startKoin {
            androidContext(this@MainActivity)
            modules(provideKoinModules(this@MainActivity))
        }

        setContent {
            MaterialTheme {
                NavGraph()
            }
        }
    }
}