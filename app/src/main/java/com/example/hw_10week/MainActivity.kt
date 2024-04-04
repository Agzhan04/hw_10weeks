package com.example.hw_10week

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


interface AgeApiService {
    @GET(".")
    suspend fun getAge(@Query("name") name: String): AgeResponse
}


data class AgeResponse(val name: String?, val age: Int?, val count: Int?)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Инициализация Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.agify.io/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val ageApiService = retrofit.create(AgeApiService::class.java)

        setContent {
            AgeApp(ageApiService)
        }
    }
}

@Composable
fun AgeApp(ageApiService: AgeApiService) {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Enter your name") }
        )
        Button(onClick = {
            coroutineScope.launch {
                isLoading = true
                try {
                    // Выполнение сетевого запроса
                    val response = ageApiService.getAge(name)
                    age = "Estimated age: ${response.age}"
                } catch (e: Exception) {
                    Log.e("AgeApi", "Error fetching age", e)
                    age = "Failed to fetch age"
                } finally {
                    isLoading = false
                }
            }
        }) {
            Text(if (isLoading) "Loading..." else "Find Out the Age")
        }

        age?.let {
            Text(it)
        }

        if (isLoading) {
            CircularProgressIndicator()
        }
    }
}
