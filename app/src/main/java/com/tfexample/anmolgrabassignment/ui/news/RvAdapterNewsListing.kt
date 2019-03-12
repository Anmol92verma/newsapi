package com.tfexample.anmolgrabassignment.ui.news

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tfexample.anmolgrabassignment.databinding.LayoutNewsItemBinding

class RvAdapterNewsListing : RecyclerView.Adapter<RvAdapterNewsListing.NewsItemViewHolder>() {
  private var dataSet: MutableList<Article>? = null

  override fun onCreateViewHolder(parent: ViewGroup, itemType: Int): NewsItemViewHolder {
    val binding = LayoutNewsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    return NewsItemViewHolder(binding)
  }

  override fun getItemCount(): Int = dataSet?.size ?: 0

  override fun onBindViewHolder(holder: NewsItemViewHolder, p1: Int) {
    holder.bindUI()
  }

  fun updateArticles(
      articles: List<Article>) {
    dataSet?.let {
      val positionStart = it.size.plus(1)
      it.addAll(articles)
      notifyItemRangeInserted(positionStart, it.size)
    } ?: kotlin.run {
      this.dataSet = mutableListOf()
      this.dataSet?.addAll(articles)
      notifyDataSetChanged()
    }
  }

  inner class NewsItemViewHolder(
      private val binding: LayoutNewsItemBinding) : RecyclerView.ViewHolder(
      binding.root) {
    fun bindUI() {
      binding.item = dataSet?.get(adapterPosition)
    }
  }

}
