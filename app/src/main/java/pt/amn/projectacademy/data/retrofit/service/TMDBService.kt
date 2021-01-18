package pt.amn.projectacademy.data.retrofit.service

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pt.amn.projectacademy.data.retrofit.response.ActorsListResponse
import pt.amn.projectacademy.data.retrofit.response.GenresListResponse
import pt.amn.projectacademy.data.retrofit.response.MoviesListResponse
import pt.amn.projectacademy.utils.TMDB_API_KEY
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Used to connect to the themoviedb.org API to fetch movies
 */
interface TMDBService {

    @GET("movie/popular")
    suspend fun getPopularMoviesAsync(@Query("page") page: Int) : MoviesListResponse

    /*@GET("movie/{id}")
    fun getMovieByIdAsync(@Path("id") id: Int): Deferred<Movie>*/

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieActorsAsync(@Path("movie_id") id: Int) : ActorsListResponse

    @GET("genre/movie/list")
    suspend fun getGenresAsync() : GenresListResponse

    companion object {

        private const val BASE_URL = "https://api.themoviedb.org/3/"

        fun create() : TMDBService {
            val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }

            //  Создаем Network Interceptor, чтобы добавить api_key во все запросы в качестве authInterceptor
            val authInterceptor = Interceptor { chain ->
                val newUrl = chain.request().url
                    .newBuilder()
                    .addQueryParameter("api_key", TMDB_API_KEY)
                    .build()

                val newRequest = chain.request()
                    .newBuilder()
                    .url(newUrl)
                    .build()

                chain.proceed(newRequest)
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .addInterceptor(logger)
                .build()

            val json = Json {
                prettyPrint = true
                ignoreUnknownKeys = true
            }

            val contentType = "application/json".toMediaType()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(json.asConverterFactory(contentType))
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
                .create(TMDBService::class.java)
        }

    }
}