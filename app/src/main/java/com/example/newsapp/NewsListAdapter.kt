package com.example.newsapp


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.NewsListAdapter.NewsViewHolder
import kotlin.collections.ArrayList

class NewsListAdapter(val context:Context,private val listener:NewsItemClicked):RecyclerView.Adapter<NewsViewHolder>() {
    private val items:ArrayList<News> = ArrayList()



    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleView:TextView?=itemView.findViewById(R.id.tvTitle)
        val image: ImageView ?=itemView.findViewById(R.id.ivImage)
        val author: TextView ?=itemView.findViewById(R.id.tvAuthor)



    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.item_news,parent,false)
        val viewHolder= NewsViewHolder(view)

        view.setOnClickListener{
            listener.onItemClicked(items[viewHolder.adapterPosition])
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val currentItem=items[position]
        holder.titleView!!.text=currentItem.title
        holder.author!!.text=currentItem.author
        Glide.with(holder.itemView.context).load(currentItem.imageUrl).into(holder.image!!)

    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun updateNews(updatedNews:ArrayList<News>){
        items.clear()
        items.addAll(updatedNews)

        notifyDataSetChanged()
    }

    fun swiped(viewHolder: RecyclerView.ViewHolder, adapter: NewsListAdapter){
        val pos=viewHolder.adapterPosition
        items.removeAt(pos)
        adapter.notifyItemRemoved(pos)

    }


}



interface NewsItemClicked{
    fun onItemClicked(item: News)
}