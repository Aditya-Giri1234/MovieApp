package com.aditya.movieapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadStateAdapter
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aditya.movieapp.databinding.MovieListItemBinding
import com.aditya.movieapp.model.dataModel.Movie
import com.aditya.movieapp.util.Constant
import com.bumptech.glide.Glide

class MoviePagerAdapter(private val showDetail:(id:Int)->Unit): PagingDataAdapter<Movie, MoviePagerAdapter.MovieViewHolder>(MovieComparator) {

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        if(position!=-1){
            getItem(position)?.let {movie->
                holder.view.cvMovieTitle.text=movie.title
                holder.view.cvMovieReleaseDate.text=movie.releaseDate
                Glide.with(holder.itemView.context).load("${Constant.posterBaseUrl}${movie.posterPath}").into(holder.view.cvIvMoviePoster)
                holder.view.cardView.setOnClickListener {
                    showDetail.invoke(movie.id)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = MovieListItemBinding.inflate(inflater, parent, false)
        return MovieViewHolder(binding)
    }

    class MovieViewHolder(val view: MovieListItemBinding): RecyclerView.ViewHolder(view.root)
    object MovieComparator: DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            // Id is unique.
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(position==itemCount) 0  else 1
    }

    fun withHeaderAndFooter(
        header: LoadStateAdapter<*>,
        footer: LoadStateAdapter<*>
    ): ConcatAdapter {
        addLoadStateListener { loadStates ->
            header.loadState = loadStates.prepend
            footer.loadState = loadStates.append
        }
        return ConcatAdapter(ConcatAdapter.Config.Builder().setIsolateViewTypes(false).build(), header, this, footer)
    }
}