import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Call

interface AgeApiService {
    @GET("https://api.agify.io/")
    fun getAge(@Query("name") name: String): Call<AgeResponse>
}

data class AgeResponse(val name: String?, val age: Int?, val count: Int?)
