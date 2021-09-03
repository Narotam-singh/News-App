package com.example.newsapp

import android.annotation.SuppressLint
import android.content.Intent

import android.net.Uri

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.OrientationHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.google.firebase.auth.FirebaseAuth
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), NewsItemClicked, CategoryItemClicked {
    private lateinit var mAdapter:NewsListAdapter
    private var newsArray=ArrayList<News>()

    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val hRecyclerView:RecyclerView=findViewById(R.id.hRecyclerView)
        hRecyclerView.layoutManager=LinearLayoutManager(this,OrientationHelper.HORIZONTAL,false)
        val items=fetchList()
        val adapter= CategoryListAdapter(items,this)
        hRecyclerView.adapter=adapter


        val recyclerView:RecyclerView=findViewById(R.id.recyclerView)

        recyclerView.layoutManager=LinearLayoutManager(this)
        Toast.makeText(this,"Showing News Related To General",Toast.LENGTH_SHORT).show()
        fetchData("https://saurav.tech/NewsAPI/top-headlines/category/general/in.json")
        mAdapter= NewsListAdapter(this,this)
        recyclerView.adapter=mAdapter

        val swipeToDelete= object : SwipeToDelete(){

            override fun onSwiped(viewHolder: ViewHolder, direction: Int) {

                mAdapter.swiped(viewHolder,mAdapter)
            }
        }

        val itemTouchHelper=ItemTouchHelper(swipeToDelete)
        itemTouchHelper.attachToRecyclerView(recyclerView)

    }


    private fun fetchList(): ArrayList<Category> {

        val list=ArrayList<Category>()
        var lists=Category("Business","https://images.unsplash.com/photo-1444653614773-995cb1ef9efa?ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mzd8fGJ1c2luZXNzfGVufDB8fDB8fA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60")
        list.add(lists)
        lists=Category("Entertainment","https://images.unsplash.com/photo-1603190287605-e6ade32fa852?ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mnx8ZW50ZXJ0YWlubWVudHxlbnwwfHwwfHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60")
        list.add(lists)
        lists=Category("General","https://images.unsplash.com/photo-1586880234202-32a56790c681?ixid=MnwxMjA3fDB8MHxzZWFyY2h8NTZ8fG5ld3N8ZW58MHx8MHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60")
        list.add(lists)
        lists=Category("Health","https://images.unsplash.com/photo-1532938911079-1b06ac7ceec7?ixid=MnwxMjA3fDB8MHxzZWFyY2h8NXx8aGVhbHRofGVufDB8fDB8fA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60")
        list.add(lists)
        lists=Category("Science","https://images.unsplash.com/photo-1530973428-5bf2db2e4d71?ixid=MnwxMjA3fDB8MHxzZWFyY2h8N3x8c2NpZW5jZXxlbnwwfHwwfHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60")
        list.add(lists)
        lists=Category("Sports","https://images.unsplash.com/photo-1587280501635-68a0e82cd5ff?ixid=MnwxMjA3fDB8MHxzZWFyY2h8OXx8c3BvcnRzfGVufDB8fDB8fA%3D%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60")
        list.add(lists)
        lists=Category("Technology","https://images.unsplash.com/photo-1488590528505-98d2b5aba04b?ixid=MnwxMjA3fDB8MHxzZWFyY2h8Mnx8dGVjaG5vbG9neXxlbnwwfHwwfHw%3D&ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60")
        list.add(lists)
        return list
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu,menu)
        val menuItem=menu!!.findItem(R.id.nav_search)
        val searchView= menuItem.actionView as androidx.appcompat.widget.SearchView
        searchView.maxWidth=Int.MAX_VALUE
        searchView.setOnQueryTextListener(object:androidx.appcompat.widget.SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                mAdapter.filter.filter(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                mAdapter.filter.filter(newText)
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.nav_logout){
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this@MainActivity,LoginActivity::class.java))
            finish()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun fetchData(url:String){

        //val url="https://saurav.tech/NewsAPI/top-headlines/category/general/in.json"
        val jsonObjectRequest=JsonObjectRequest(
            Request.Method.GET,url,null,
            Response.Listener {
                val newsJsonArray=it.getJSONArray("articles")
                val newsArray=ArrayList<News>()

                for(i in 0 until newsJsonArray.length()){
                    val newsJsonObject=newsJsonArray.getJSONObject(i)
                    val news=News(
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("author"),
                        newsJsonObject.getString("url"),
                        newsJsonObject.getString("urlToImage")
                    )
                    newsArray.add(news)
                }
                mAdapter.updateNews(newsArray)
            },
            Response.ErrorListener {
                Toast.makeText(this,"Error",Toast.LENGTH_LONG).show()
            }
        )
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
        //pbLoad.visibility=View.GONE
    }


    override fun onItemClicked(item: News) {

        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))
    }


    override fun onCategoryClicked(id:Int){
        if(id==1) {
            newsArray.clear()
            Toast.makeText(applicationContext,"Showing News Related to Business",Toast.LENGTH_SHORT).show()
            fetchData("https://saurav.tech/NewsAPI/top-headlines/category/business/in.json")
        }
        else if(id==2){
            newsArray.clear()
            Toast.makeText(applicationContext,"Showing News Related to Entertainment",Toast.LENGTH_SHORT).show()
            fetchData("https://saurav.tech/NewsAPI/top-headlines/category/entertainment/in.json")
        }

        else if(id==3){
            newsArray.clear()
            Toast.makeText(applicationContext,"Showing News Related to General",Toast.LENGTH_SHORT).show()
            fetchData("https://saurav.tech/NewsAPI/top-headlines/category/general/in.json")
        }

        else if(id==4){
            newsArray.clear()
            Toast.makeText(applicationContext,"Showing News Related to Health",Toast.LENGTH_SHORT).show()
            fetchData("https://saurav.tech/NewsAPI/top-headlines/category/health/in.json")
        }

        else if(id==5){
            newsArray.clear()
            Toast.makeText(applicationContext,"Showing News Related to Science",Toast.LENGTH_SHORT).show()
            fetchData("https://saurav.tech/NewsAPI/top-headlines/category/science/in.json")
        }

        else if(id==6){
            newsArray.clear()
            Toast.makeText(applicationContext,"Showing News Related to Sports",Toast.LENGTH_SHORT).show()
            fetchData("https://saurav.tech/NewsAPI/top-headlines/category/sports/in.json")
        }

        else{
            newsArray.clear()
            Toast.makeText(applicationContext,"Showing News Related to Technology",Toast.LENGTH_SHORT).show()
            fetchData("https://saurav.tech/NewsAPI/top-headlines/category/technology/in.json")
        }


    }




}


