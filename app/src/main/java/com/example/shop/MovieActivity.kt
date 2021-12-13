package com.example.shop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.shop.databinding.ActivityMovieBinding
import com.example.shop.databinding.RowMovieBinding
import com.google.gson.Gson
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.info
import org.jetbrains.anko.uiThread
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import java.net.URL

class MovieActivity : AppCompatActivity(), AnkoLogger {
    private lateinit var binding: ActivityMovieBinding
    lateinit var movies: Movies
    val movie_url = "https://api.jsonserve.com/7fSLPm"

    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.jsonserve.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMovieBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        doAsync {
//            val url = URL(movie_url)
//            val json = url.readText()
//            movies = Gson().fromJson<Movies>(json, Movies::class.java)
//            info(json)
            val movieService = retrofit.create(MovieService::class.java)

            movies = movieService
                .listMovies()
                .execute()
                .body()!!

            uiThread {
                val recycler = binding.recycler
                recycler.layoutManager = LinearLayoutManager(this@MovieActivity)
                recycler.setHasFixedSize(true)
                recycler.adapter = MovieAdapter()
            }
        }
    }

    inner class MovieAdapter: RecyclerView.Adapter<MovieHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.row_movie, parent, false)

            val holder = MovieHolder(view)

            return holder
        }

        override fun onBindViewHolder(holder: MovieHolder, position: Int) {
            holder.movieTitle.text = movies[position].Title
            holder.movieImdb.text = movies[position].imdbRating
            holder.movieDirector.text = movies[position].Director

            Glide.with(this@MovieActivity)
                .load(movies[position].Images[0])
                .override(200)
                .into(holder.moviePoster)
        }

        override fun getItemCount(): Int {
            return movies.size
        }

    }
    class MovieHolder(view: View): RecyclerView.ViewHolder(view) {
        val binding = RowMovieBinding.bind(view)

        val movieTitle = binding.movieTitle
        val movieImdb = binding.movieImdb
        val movieDirector = binding.movieDirector
        val moviePoster = binding.moviePoster
    }
}



class Movies : ArrayList<Movie>()

data class Movie(
    val Actors: String,
    val Awards: String,
    val ComingSoon: Boolean,
    val Country: String,
    val Director: String,
    val Genre: String,
    val Images: List<String>,
    val Language: String,
    val Metascore: String,
    val Plot: String,
    val Poster: String,
    val Rated: String,
    val Released: String,
    val Response: String,
    val Runtime: String,
    val Title: String,
    val Type: String,
    val Writer: String,
    val Year: String,
    val imdbID: String,
    val imdbRating: String,
    val imdbVotes: String,
    val totalSeasons: String
)

interface MovieService {
    @GET("7fSLPm")
    fun listMovies(): Call<Movies>
}