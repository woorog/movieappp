package com.example.myapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import android.view.MotionEvent
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.example.myapplication.adapter.MovieAdapter
import com.example.myapplication.adapter.HistoryAdapter
import com.example.myapplication.api.MovieService
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.model.MovieMainDto
import com.example.myapplication.model.History
import com.example.myapplication.model.SearchMovieDto
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {



    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MovieAdapter
    private lateinit var movieService: MovieService
    private lateinit var historyAdapter: HistoryAdapter

    private lateinit var db: AppDatabase

   private val TAG="MyActivity"


    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initmovieRecyclerView()  //영화 정보
        initSearchEditText()   //검색기능
        initHistoryRecyclerView() //검색기록


        //database 를 위한 room 사용
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            name = "movieSearchDB"
        ).build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://openapi.naver.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()


        movieService = retrofit.create<MovieService>()

        movieService.mainmovie( id = "1nV41uHeBS6rMtJWqY1L", pw = "rGYnT899RA")
            .enqueue(object: Callback<MovieMainDto> {
                override fun onResponse( //true.
                    call: Call<MovieMainDto>,
                    response: Response<MovieMainDto>
                ) {

                    if(response.isSuccessful.not()){
                        Log.e(TAG,"error")
                        return
                    }
                    response.body()?.let {
                        Log.d(TAG,it.toString())

                        it.movies.forEach{book ->
                            Log.d(TAG,book.toString())

                        }
                        adapter.submitList(it.movies)
                    }
                }

                override fun onFailure(call: Call<MovieMainDto>, t: Throwable) {
                    Log.e(TAG,t.toString())
                }


            })

    //검색기능


    }


    private fun search(keyword:String){
        movieService.getmoviesByName(keyword, id = "1nV41uHeBS6rMtJWqY1L", pw = "rGYnT899RA")
            .enqueue(object: Callback<SearchMovieDto> {
                override fun onResponse( //true.
                    call: Call<SearchMovieDto>,
                    response: Response<SearchMovieDto>
                ) {
                    hideHistoryView()
                    saveSearchKeyword(keyword)

                    if(response.isSuccessful.not()){
                        Log.e(TAG,"error")
                        return
                    }

                    adapter.submitList(response.body()?.movies.orEmpty())

                }

                override fun onFailure(call: Call<SearchMovieDto>, t: Throwable) {
                    hideHistoryView()
                    Log.e(TAG,t.toString())
                }


            })
    }

    //영화 리사이클러뷰
    private fun initmovieRecyclerView(){
        adapter = MovieAdapter(itemClickedListener = {
            var intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.link))
            Log.d("link",it.link)
            startActivity(intent)
        })
        binding.movieRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.movieRecyclerView.adapter = adapter

    }

    //검색기록 리사이클러뷰
    private fun initHistoryRecyclerView() {
        historyAdapter = HistoryAdapter(
            historyDeleteClickedListener = {
                deleteSearchKeyword(it)
            }, historyKeywordClicktedListener = {
                search(it)
            })
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.historyRecyclerView.adapter = historyAdapter
        initSearchEditText()
    }


    private fun initSearchEditText() {
        binding.searchEditText.setOnTouchListener { v, event ->
            if(event.action == MotionEvent.ACTION_DOWN) {
                showHistoryView()
            }
            return@setOnTouchListener false
        }

        binding.searchEditText.setOnKeyListener { v, keyCode, event ->
            if(keyCode == KeyEvent.KEYCODE_ENTER && event.action == MotionEvent.ACTION_DOWN){
                search(binding.searchEditText.text.toString())
                return@setOnKeyListener true
            }
            return@setOnKeyListener false

        }

        }


    private fun showHistoryView() {
        Thread {
            val keywords = db.historyDao().getRecent(10).reversed()
            runOnUiThread{
                binding.historyRecyclerView.isVisible=true
                historyAdapter.submitList(keywords.orEmpty())
            }

        }.start()
        binding.historyRecyclerView.isVisible=true
    }

    private fun hideHistoryView() {
        binding.historyRecyclerView.isVisible = false
    }

    private fun saveSearchKeyword(keyword: String){
        Thread{
            db.historyDao().insertHistory(History(null,keyword))
        }.start()
    }

    private fun deleteSearchKeyword(keyword: String) {
        Thread {
            db.historyDao()?.delete(keyword)
            showHistoryView()
        }.start()
    }









}