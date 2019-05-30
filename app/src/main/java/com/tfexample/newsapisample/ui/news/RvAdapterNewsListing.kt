package com.tfexample.newsapisample.ui.news

import android.support.v4.util.ArrayMap
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tfexample.newsapisample.databinding.LayoutNewsItemBinding
import com.tfexample.newsapisample.databinding.LayoutSourceItemBinding
import com.tfexample.newsapisample.networking.models.DBArticle
import com.tfexample.newsapisample.networking.models.NewsAdapterItem
import com.tfexample.newsapisample.networking.models.Source

class RvAdapterNewsListing(
    private val activityNewsListing: AdapterViewListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  private var dataSet = mutableListOf<NewsAdapterItem>()
  val HEADER_SOURCE = 1
  val ITEM_NEWS = 2
  override fun getItemViewType(position: Int): Int {
    return when {
      viewTypeIsSource(position) -> HEADER_SOURCE
      else -> ITEM_NEWS
    }
  }

  private fun viewTypeIsSource(position: Int): Boolean = dataSet[position] is Source

  override fun onCreateViewHolder(parent: ViewGroup, itemType: Int): RecyclerView.ViewHolder {
    return when (itemType) {
      HEADER_SOURCE -> {
        val binding = LayoutSourceItemBinding.inflate(LayoutInflater.from(parent.context), parent,
            false)
        SourceItemViewHolder(binding)
      }
      else -> {
        val binding = LayoutNewsItemBinding.inflate(LayoutInflater.from(parent.context), parent,
            false)
        NewsItemViewHolder(binding)
      }
    }
  }

  override fun getItemCount(): Int = dataSet.size

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, p1: Int) {
    when (holder) {
      is NewsItemViewHolder -> {
        holder.bindUI()
      }
      is SourceItemViewHolder -> {
        holder.bindUI()
      }
    }
  }

  fun updateArticles(
      articles: ArrayMap<Source, List<DBArticle>>) {
    articles.forEach {
      dataSet.add(it.key)
      dataSet.addAll(it.value)
    }
    notifyDataSetChanged()
  }

  inner class NewsItemViewHolder(
      val binding: LayoutNewsItemBinding
  ) : RecyclerView.ViewHolder(
      binding.root
  ) {
    init {
      binding.root.setOnClickListener {
        activityNewsListing.navigateTo((dataSet[adapterPosition] as DBArticle).url)
      }
      binding.favIcon.setOnClickListener {
        val dbArticle = (dataSet[adapterPosition] as DBArticle)
        activityNewsListing.switchFav(dbArticle)
        dbArticle.isFav = !dbArticle.isFav
        binding.notifyChange()
        binding.invalidateAll()
      }
    }

    fun bindUI() {
      binding.item = (dataSet[adapterPosition] as DBArticle)
    }
  }

  inner class SourceItemViewHolder(
      val binding: LayoutSourceItemBinding
  ) : RecyclerView.ViewHolder(
      binding.root
  ) {

    fun bindUI() {
      binding.tvSourceName.text = (dataSet[adapterPosition] as Source).name
    }
  }

}
