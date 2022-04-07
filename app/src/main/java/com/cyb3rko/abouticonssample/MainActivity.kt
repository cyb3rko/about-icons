package com.cyb3rko.abouticonssample

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.cyb3rko.abouticons.AboutIcons

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(AboutIcons(this, R.drawable::class.java, supportFragmentManager)
            .setTitle("My Used Icons").get())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            R.id.action_github -> startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/cyb3rko/about-icons")))
            R.id.action_about -> startActivity(Intent(applicationContext, About::class.java))
        }

        return super.onOptionsItemSelected(item)
    }
}
