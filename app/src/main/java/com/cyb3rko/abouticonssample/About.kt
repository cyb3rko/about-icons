package com.cyb3rko.abouticonssample

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View

import androidx.appcompat.app.AppCompatActivity

import com.mikepenz.aboutlibraries.LibsBuilder

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
            .create()

        setContentView(aboutPage)
    }

    private fun showLibraries() : View.OnClickListener {
        return View.OnClickListener {
            LibsBuilder()
                .withShowLoadingProgress(true)
                .withAboutVersionShownCode(false)
                .withAboutVersionShownName(false)
                .withFields(R.string::class.java.fields)
                .withAutoDetect(true)
                .withAboutIconShown(false)
                .withAboutVersionShown(false)
                .withVersionShown(true)
                .withLicenseDialog(true)
                .withLicenseShown(true)
                .withCheckCachedDetection(true)
                .withSortEnabled(true)
                .start(application)
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