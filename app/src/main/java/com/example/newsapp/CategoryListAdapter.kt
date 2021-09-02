package com.example.newsapp



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class CategoryListAdapter(private val itemslist:ArrayList<Category>,private val listener: MainActivity) :RecyclerView.Adapter<CategoryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view=LayoutInflater.from(parent.context).inflate(R.layout.categories_item,parent,false)
        val viewHolder=CategoryViewHolder(view)
        view.setOnClickListener{
            listener.onCategoryClicked(viewHolder.adapterPosition+1)
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val currentItem=itemslist[position]
        holder.tvNewsCategory.text=currentItem.item
        Glide.with(holder.itemView.context).load(currentItem.imageUrls).into(holder.ivCategory)
    }

    override fun getItemCount(): Int {
        return itemslist.size
    }
}

class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val tvNewsCategory:TextView=itemView.findViewById(R.id.tvNewsCategory)
    val ivCategory:ImageView=itemView.findViewById(R.id.ivCategory)
}

interface CategoryItemClicked{
    fun onCategoryClicked(id:Int)
}