package com.cyb3rko.abouticons

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView

import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap

import kotlinx.android.synthetic.main.activity_icon_info.*

internal class IconInfoActivity: AppCompatActivity() {

    private lateinit var bundle: Bundle
    private lateinit var drawable: Drawable

    override fun onCreate(savedInstanceState: Bundle?) {
        bundle = intent.extras!!
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_icon_info)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        addContent()
    }

    @SuppressLint("SetTextI18n")
    private fun addContent() {
        drawable = ResourcesCompat.getDrawable(applicationContext.resources, bundle.getInt("drawableId"), applicationContext.theme)!!

        header.setBackgroundColor(getAverageColor(drawable))
        findViewById<ImageView>(R.id.iconView).setImageDrawable(drawable)
        author.text = bundle.getString("author")
        website.text = bundle.getString("website")
        if (bundle.getBoolean("modified")) {
            modified_info.text = "Modified"
        } else {
            modified_layout.visibility = View.GONE
            modified_divider.visibility = View.GONE
        }
        license_name.text = bundle.getString("licenseName")
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
