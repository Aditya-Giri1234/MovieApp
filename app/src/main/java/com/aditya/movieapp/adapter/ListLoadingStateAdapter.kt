package com.aditya.movieapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aditya.movieapp.databinding.ItemNetworkStateBinding
import com.aditya.movieapp.util.Helper


class ListLoadingStateAdapter(
    private val retry: () -> Unit
) : LoadStateAdapter<ListLoadingStateAdapter.NetworkStateItemViewHolder>() {

    val TAG ="ListLoadingStateAdapter "
    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): NetworkStateItemViewHolder {

        val view =
            ItemNetworkStateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NetworkStateItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: NetworkStateItemViewHolder, loadState: LoadState) =
        holder.bind(loadState)

    inner class NetworkStateItemViewHolder(
        private val binding: ItemNetworkStateBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retryButton.setOnClickListener {
                retry.invoke()
            }
        }

        fun bind(loadState: LoadState) {


                binding.apply {
                    progressBar.isVisible = loadState is LoadState.Loading
                    progressBar.isVisible = loadState !is LoadState.Error

                    retryButton.isVisible = loadState !is LoadState.Loading
                    retryButton.isVisible = loadState is LoadState.Error

                    errorMsg.isVisible = loadState !is LoadState.Loading
                    errorMsg.isVisible = loadState is LoadState.Error

            }
            if(Helper.isEndOfListDown && Helper.isEndOfListFromUp){
                Helper.customLog("MovieList","$TAG - bind - page end")
                binding.errorMsg.text="You reached end of List ."
                binding.errorMsg.isVisible=true
                binding.retryButton.isGone=true
            }else{
                Helper.customLog("MovieList","$TAG - bind - page not end")
                binding.errorMsg.text="No Internet Connection ."
            }
        }
    }
}
