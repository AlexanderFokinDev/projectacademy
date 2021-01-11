package pt.amn.projectacademy.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Deferred
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import pt.amn.projectacademy.data.ActorsListResponse
import pt.amn.projectacademy.data.GenresListResponse
import pt.amn.projectacademy.data.MoviesListResponse
import pt.amn.projectacademy.models.Movie
import pt.amn.projectacademy.utilities.TMDB_API_KEY
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Used to connect to the themoviedb.org API to fetch movies
 */
interface TMDBService {

    @GET("movie/popular")
    fun getPopularMovies(@Query("page") page: Int) : Deferred<MoviesListResponse>

    @GET("movie/{id}")
    fun getMovieById(@Path("id") id: Int): Deferred<Movie>

    @GET("movie/{movie_id}/credits")
    fun getMovieActors(@Path("movie_id") id: Int) : Deferred<ActorsListResponse>

    @GET("genre/movie/list")
    fun getGenres() : Deferred<GenresListResponse>

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