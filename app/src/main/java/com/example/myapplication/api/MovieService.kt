package com.example.myapplication.api

import com.example.myapplication.model.MovieMainDto
import com.example.myapplication.model.SearchMovieDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query


interface MovieService {
   @GET(value = "/v1/search/movie.json")
   fun getmoviesByName(
      @Query(value = "query") Keyword: String,
      @Header("X-Naver-Client-id")id:String,
      @Header("X-Naver-Client-Secret")pw:String
   ): Call<SearchMovieDto> //클래스반환



   @GET(value = "/v1/search/movie.json?query=main")
   fun mainmovie(
      @Header("X-Naver-Client-id")id:String,
      @Header("X-Naver-Client-Secret")pw:String
   ): Call<MovieMainDto> //클래스 반환
}