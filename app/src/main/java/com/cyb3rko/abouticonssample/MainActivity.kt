package com.cyb3rko.abouticonssample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.cyb3rko.abouticons.AboutIcons

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(AboutIcons(this, R.drawable::class.java).setTitle("My Used Icons").get())
    }
}
