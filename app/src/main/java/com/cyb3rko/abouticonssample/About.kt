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
            .setDescription(getString(R.string.about_description))
            .addItem(
                Element(
                    "Version ${BuildConfig.VERSION_NAME} (${BuildConfig.VERSION_CODE})",
                    githubIcon
                ).setOnClickListener { openUrl("https://github.com/cyb3rko/about-icons/releases") }
            )
            .addGroup(getString(R.string.about_credits))
            .addItem(
                Element(
                    getString(R.string.activity_libraries),
                    R.drawable._icon_libraries
                ).setOnClickListener { showLibraries() }
            )
            .addGroup(getString(R.string.about_connect))
            .addItem(
                Element(
                    getString(R.string.about_github),
                    githubIcon
                ).setOnClickListener { openUrl("https://github.com/cyb3rko/about-icons") }
            )
            .addItem(
                Element(
                    getString(R.string.about_contact),
                    emailIcon
                ).setOnClickListener { writeEmail() }
            )
            .addItem(
                Element(
                    getString(R.string.about_github_other),
                    githubIcon
                ).setOnClickListener { openUrl("https://github.com/cyb3rko/") }
            )
            .addItem(
                Element(
                    getString(R.string.about_instagram),
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
            .withActivityTitle(getString(R.string.activity_libraries))
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
            showToast(getString(R.string.toast_url_failed))
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
