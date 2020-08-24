package com.cyb3rko.abouticons

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.text.Spanned
import android.view.MenuItem
import android.view.View
import android.widget.ImageView

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap

import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet

import kotlinx.android.synthetic.main.activity_icon_info.*

internal class IconInfoActivity: AppCompatActivity() {

    private lateinit var bundle: Bundle
    private lateinit var drawable: Drawable
    private var span: Spanned? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        bundle = intent.extras!!
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_icon_info)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        addContent()
    }

    @SuppressLint("SetTextI18n")
    private fun addContent() {
        val author = bundle.getString("author")
        val drawableId = bundle.getInt("drawableId")
        val licenseName: String? = bundle.getString("licenseName")
        val link: String? = bundle.getString("link")
        val modified = bundle.getBoolean("modified")
        val website = bundle.getString("website")

        drawable = ResourcesCompat.getDrawable(applicationContext.resources, drawableId, applicationContext.theme)!!

        header.setBackgroundColor(getAverageColor(drawable))
        findViewById<ImageView>(R.id.iconView).setImageDrawable(drawable)
        if (link != "") {
            visit_button.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
            }
        } else {
            visit_button.visibility = View.GONE
        }
        author_view.text = author
        website_view.text = website
        if (website == "[Missing]") {
            link_button.visibility = View.GONE
        }
        if (modified) {
            modified_info.text = "Modified"
        } else {
            modified_container.visibility = View.GONE
        }
        when (licenseName) {
            "" -> license_container.visibility = View.GONE
            "apache_2.0" -> license_name.text = "Apache 2.0"
            "mit" -> license_name.text = "MIT License"
            "cc_by_3.0" -> license_name.text = "CC BY 3.0"
            "cc_by_sa_3.0" -> license_name.text = "CC BY-SA 3.0"
            "cc_by_4.0" -> license_name.text = "CC BY 4.0"
            "cc_by_sa_4.0" -> license_name.text = "CC BY-SA 4.0"
            else -> license_name.text = "License not found"
        }

        setOnClickListeners(website)
    }

    private fun setOnClickListeners(website: String?) {
        link_button.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://$website")))
        }

        if (license_name.text != "License not found") {
            license_name.setOnClickListener {
                MaterialDialog(this, BottomSheet()).show {
                    message(0, if (span != null) span else readLicense())
                }
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun readLicense(): Spanned? {
        span = Html.fromHtml(assets.open("${bundle.getString("licenseName")}.html").bufferedReader().use { it.readText() })
        return span
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }

    private fun getAverageColor(drawable: Drawable): Int {
        val bitmap = drawable.toBitmap()
        val bitmapWidth = bitmap.width
        val bitmapHeight = bitmap.height
        var pixelCount = 0
        var pixelSumRed = 0
        var pixelSumBlue = 0
        var pixelSumGreen = 0
        var color: Int

        for (x in 0 until bitmapWidth step 20) {
            for (y in 0 until bitmapHeight step 20) {
                color = bitmap.getPixel(x, y)
                pixelCount++
                pixelSumRed += Color.red(color)
                pixelSumGreen += Color.green(color)
                pixelSumBlue += Color.blue(color)
            }
        }

        return Color.rgb(pixelSumRed / pixelCount, pixelSumGreen / pixelCount, pixelSumBlue / pixelCount)
    }
}
