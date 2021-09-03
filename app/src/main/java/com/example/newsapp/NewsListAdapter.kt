package com.example.newsapp


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.NewsListAdapter.NewsViewHolder
import kotlin.collections.ArrayList

class NewsListAdapter(val context:Context,private val listener:NewsItemClicked):RecyclerView.Adapter<NewsViewHolder>(),Filterable {
    private var items:ArrayList<News> = ArrayList()
    private var itemsFilter:ArrayList<News> = ArrayList()



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
        itemsFilter.clear()
        itemsFilter.addAll(updatedNews)
        notifyDataSetChanged()
    }

    fun swiped(viewHolder: RecyclerView.ViewHolder, adapter: NewsListAdapter){
        val pos=viewHolder.adapterPosition
        items.removeAt(pos)
        adapter.notifyItemRemoved(pos)

    }

    override fun getFilter(): Filter {
        return object:Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResult=FilterResults()
                if(constraint==null||constraint.length<0){
                    filterResult.count = itemsFilter.size
                    filterResult.values=itemsFilter
                }else{
                    val searchChar= constraint.toString().lowercase()
                    val itemModal=ArrayList<News>()
                    for(item in itemsFilter){
                        if(item.title.contains(searchChar)||item.author.contains(searchChar)){
                            itemModal.add(item)
                        }
                    }
                    filterResult.count=itemModal.size
                    filterResult.values=itemModal
                }
                return filterResult
            }

            override fun publishResults(constraint: CharSequence?, filterResults: FilterResults?) {
                items=filterResults!!.values as ArrayList<News>
                notifyDataSetChanged()
            }
        }
    }


}



interface NewsItemClicked{
    fun onItemClicked(item: News)
}