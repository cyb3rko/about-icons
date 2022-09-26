package com.cyb3rko.abouticonssample

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View

import androidx.appcompat.app.AppCompatActivity

import mehdi.sakout.aboutpage.AboutPage
import mehdi.sakout.aboutpage.Element

class About : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val aboutPage: View = AboutPage(this)
            .setImage(R.mipmap.ic_launcher_foreground)
            .setDescription("App for showcasing the Android library 'AboutIcons'")
            .addItem(Element().setTitle("Version ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})")
                .setIconDrawable(R.drawable.about_icon_github)
                .setIntent(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/cyb3rko/about-icons")))
            )
            .addGroup("Credits")
            .addItem(Element().setTitle("Used Libraries").setIconDrawable(R.drawable._icon_libraries).setOnClickListener(showLibraries()))
            .addGroup("Connect with me")
            .addItem(Element().setTitle("Visit and give feedback on GitHub").setIconDrawable(R.drawable.about_icon_github).setOnClickListener {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/cyb3rko/about-icons")))
                }
            )
            .addEmail("niko@cyb3rko.de", "Contact me")
            // GitHub item
            .addGitHub("cyb3rko", "Take a look at my other projects")
            .addItem(
                Element().setTitle("Follow me").setIconDrawable(R.drawable.about_icon_instagram)
                    .setIconTint(R.color.about_instagram_color).setOnClickListener {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://instagram.com/_u/cyb3rko")))
                    }
            )
            .create()

        setContentView(aboutPage)
    }

    private fun showLibraries() : View.OnClickListener {
        return View.OnClickListener {
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            android.R.id.home -> finish()
        }

        return super.onOptionsItemSelected(item)
    }
}