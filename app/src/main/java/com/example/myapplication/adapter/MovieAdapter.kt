package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.databinding.ItemMovieBinding
import com.example.myapplication.model.movie

class MovieAdapter(private val itemClickedListener: (movie) -> Unit): ListAdapter<movie, MovieAdapter.movieItemViewHolder>(diffUtil) {

    inner class  movieItemViewHolder(private val binding: ItemMovieBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(movieModel:movie){
            binding.titleTextView.text=movieModel.title
            binding.pubdateview.text=movieModel.pubDate
            binding.userrate.text=movieModel.userRating
            binding.root.setOnClickListener {
                itemClickedListener(movieModel)
            }
            //이미지
           Glide
               .with(binding.coverImageView)
               .load(movieModel.image)
               .into(binding.coverImageView)


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): movieItemViewHolder {
    return  movieItemViewHolder(ItemMovieBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }//뷰홀더 없을 경우

    override fun onBindViewHolder(holder: movieItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }//

     //새로운 값을 할당 결정
    companion object{
        val diffUtil = object :DiffUtil.ItemCallback<movie>(){
            override fun areItemsTheSame(oldItem: movie, newItem: movie): Boolean {
                return oldItem==newItem

            }//안의 컨텐츠 같냐 다르냐 구별별

            override fun areContentsTheSame(oldItem: movie, newItem: movie): Boolean {
                return oldItem.title==newItem.title
            }


        }
    }


}