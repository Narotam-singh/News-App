package com.example.newsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock.sleep
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.auth.FirebaseAuth

class SplashScreen : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        supportActionBar?.hide()
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        val animation:LottieAnimationView=findViewById(R.id.animation)
        animation.playAnimation()


        Thread {
            try {
                sleep(4000)
            } catch (ex: Exception) {
                ex.printStackTrace()
            } finally {


                val currentUser= FirebaseAuth.getInstance().currentUser
                var currentUserId= ""
                if(currentUser!=null){
                    currentUserId=currentUser.uid
                }
                if(currentUserId.isNotEmpty()){
                    startActivity(Intent(this,MainActivity::class.java))
                }
                else{
                    startActivity(Intent(this,LoginActivity::class.java))
                }

                finish()
            }
        }.start()




    }
}