package com.example.practical11_19012021012

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView

class SplashActivity : AppCompatActivity(), Animation.AnimationListener{

    lateinit var logoAnim : AnimationDrawable
    lateinit var rotateAnim : Animation
    lateinit var ivGuniLogo : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)



        ivGuniLogo = findViewById<ImageView>(R.id.iv_guni_splash)

        ivGuniLogo.setBackgroundResource(R.drawable.logo_animation_list)
        logoAnim = ivGuniLogo.background as AnimationDrawable

        rotateAnim= AnimationUtils.loadAnimation(this,R.anim.rotation)
        rotateAnim.setAnimationListener(this)

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus)
        {
            logoAnim.start()
            ivGuniLogo.startAnimation(rotateAnim)
        }
        else
        {
            logoAnim.stop()
        }

    }
    override fun onAnimationStart(animation: Animation?) {


    }

    override fun onAnimationEnd(animation: Animation?) {
        Intent(this,DashboardActivity::class.java).apply {
            startActivity(this)
        }
    }

    override fun onAnimationRepeat(animation: Animation?) {
        TODO("Not yet implemented")
    }
}