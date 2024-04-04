import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*

class AgeApi {
    private val client = HttpClient(CIO) {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
        }
    }

    suspend fun getAge(name: String): Int? {
        val url = "https://api.agify.io/?name=$name"
        val response: AgeResponse = client.get(url)
        return response.age
    }
}

data class AgeResponse(val name: String, val age: Int, val count: Int)
