package com.cyb3rko.abouticons

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap

import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.cyb3rko.androidlicenses.AndroidLicenses

import kotlinx.android.synthetic.main.activity_icon_info.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

internal class IconInfoActivity: AppCompatActivity() {

    private lateinit var bundle: Bundle
    private lateinit var drawable: Drawable
    private lateinit var license: String

    override fun onCreate(savedInstanceState: Bundle?) {
        bundle = intent.extras!!
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_icon_info)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        AndroidLicenses.init(applicationContext)

        addContent()
    }

    @SuppressLint("SetTextI18n")
    private fun addContent() {
        val author = bundle.getString("author")
        val drawableId = bundle.getInt("drawableId")
        license = bundle.getString("licenseName")!!
        val link: String? = bundle.getString("link")
        val modified = bundle.getBoolean("modified")
        val website = bundle.getString("website")

        drawable = ResourcesCompat.getDrawable(applicationContext.resources, drawableId, applicationContext.theme)!!

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
        when (license) {
            "" -> license_container.visibility = View.GONE
            "apache_2.0" -> license_name.text = "Apache 2.0"
            "mit" -> license_name.text = "MIT License"
            "cc_by_3.0" -> license_name.text = "CC BY 3.0"
            "cc_by_sa_3.0" -> license_name.text = "CC BY-SA 3.0"
            "cc_by_4.0" -> license_name.text = "CC BY 4.0"
            "cc_by_sa_4.0" -> license_name.text = "CC BY-SA 4.0"
            else -> license_name.text = ""
        }

        setOnClickListeners(website)

        GlobalScope.launch {
            header.setBackgroundColor(getAverageColor(drawable))
        }
    }

    private fun setOnClickListeners(website: String?) {
        link_button.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://$website")))
        }

        val license = bundle.getString("licenseName")

        if (license_name.text != "") {
            license_container.setOnClickListener {
                MaterialDialog(this, BottomSheet()).show {
                    message(0, AndroidLicenses.get(license!!))
                }
            }
        }
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

        for (x in 0 until bitmapWidth step 10) {
            for (y in 0 until bitmapHeight step 10) {
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
