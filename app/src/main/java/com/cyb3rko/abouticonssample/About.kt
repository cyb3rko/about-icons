package com.cyb3rko.abouticonssample

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mikepenz.aboutlibraries.LibsBuilder
import mehdi.sakout.aboutpage.AboutPage
import mehdi.sakout.aboutpage.Element

class About : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val githubIcon = mehdi.sakout.aboutpage.R.drawable.about_icon_github
        val emailIcon = mehdi.sakout.aboutpage.R.drawable.about_icon_email
        val instagramIcon = mehdi.sakout.aboutpage.R.drawable.about_icon_instagram
        val instagramColor = mehdi.sakout.aboutpage.R.color.about_instagram_color

        val aboutPage: View = AboutPage(this, R.style.Theme_AboutPage)
            .setImage(R.mipmap.ic_launcher_foreground)
            .setDescription("App for showcasing the Android library 'AboutIcons'")
            .addItem(
                Element(
                    "Version ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})",
                    githubIcon
                ).setOnClickListener { openUrl("https://github.com/cyb3rko/about-icons/releases") }
            )
            .addGroup("Credits")
            .addItem(
                Element(
                    "Used Libraries",
                    R.drawable._icon_libraries
                ).setOnClickListener { showLibraries() }
            )
            .addGroup("Connect with me")
            .addItem(
                Element(
                    "Visit and give feedback on GitHub",
                    githubIcon
                ).setOnClickListener { openUrl("https://github.com/cyb3rko/about-icons") }
            )
            .addItem(
                Element(
                    "Contact me",
                    emailIcon
                ).setOnClickListener { writeEmail() }
            )
            .addItem(
                Element(
                    "Take a look at my other projects",
                    githubIcon
                ).setOnClickListener { openUrl("https://github.com/cyb3rko/") }
            )
            .addItem(
                Element(
                    "Follow me",
                    instagramIcon
                )
                    .setIconTint(instagramColor)
                    .setOnClickListener { openUrl("https://instagram.com/_u/cyb3rko") }
            )
            .create()

        setContentView(aboutPage)
    }

    private fun showLibraries() {
        LibsBuilder()
            .withLicenseShown(true)
            .withAboutIconShown(false)
            .withAboutVersionShown(false)
            .withActivityTitle("Used Libraries")
            .withSearchEnabled(true)
            .start(applicationContext)
    }

    private fun writeEmail() {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            setDataAndType(Uri.parse("mailto:"), "text/plain")
            putExtra(Intent.EXTRA_EMAIL, arrayOf("niko@cyb3rko.de"))
        }
        startActivity(intent)
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

    private fun openUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            storeToClipboard(url)
            showToast("Opening URL failed, copied URL instead")
        }
    }

    private fun storeToClipboard(url: String) {
        val clip = ClipData.newPlainText("URL", url)
        (getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager)
            .setPrimaryClip(clip)
    }

    private fun showToast(message: String) {
        Toast.makeText(
            applicationContext,
            message,
            Toast.LENGTH_LONG
        ).show()
    }
}
