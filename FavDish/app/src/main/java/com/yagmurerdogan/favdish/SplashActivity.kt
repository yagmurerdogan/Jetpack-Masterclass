package com.yagmurerdogan.favdish

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yagmurerdogan.favdish.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val splashBinding: ActivitySplashBinding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(splashBinding.root)

    }
}