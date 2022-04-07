package com.cyb3rko.abouticons

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.DialogFragment
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.bottomsheets.BottomSheet
import com.cyb3rko.androidlicenses.AndroidLicenses
import kotlinx.android.synthetic.main.dialog_icon_info.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class IconInfoDialog(
    private val appContext: Context,
    private val author: String,
    private val drawableId: Int,
    private val licenseName: String,
    private val link: String,
    private val modified: Boolean,
    private val website: String
) : DialogFragment() {
    companion object {
        const val TAG = "IconInfoDialog"
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return layoutInflater.inflate(R.layout.dialog_icon_info, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        val drawable = ResourcesCompat.getDrawable(appContext.resources, drawableId, appContext.theme)!!
        view.findViewById<ImageView>(R.id.icon_view).setImageDrawable(drawable)
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
        license_name.text = when (licenseName) {
            "" -> ""
            "apache_2.0" -> "Apache 2.0"
            "mit" -> "MIT License"
            "cc_by_3.0" -> "CC BY 3.0"
            "cc_by_sa_3.0" -> "CC BY-SA 3.0"
            "cc_by_4.0" -> "CC BY 4.0"
            "cc_by_sa_4.0" -> "CC BY-SA 4.0"
            "cc_by_nc_3.0" -> "CC BY-NC 3.0"
            "cc_by_nc_sa_3.0" -> "CC BY-NC-SA 3.0"
            else -> "Not found"
        }

        if (license_name.text == "") license_container.visibility = View.GONE

        setOnClickListeners(website)

        GlobalScope.launch {
            val backgroundColor = getAverageColor(drawable)
            (appContext as Activity).runOnUiThread {
                header.setBackgroundColor(backgroundColor)
            }
        }
    }

    private fun setOnClickListeners(website: String?) {
        AndroidLicenses.init(appContext)

        link_button.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://$website")))
        }

        if (license_name.text != "") {
            license_container.setOnClickListener {
                MaterialDialog(appContext, BottomSheet()).show {
                    message(text = AndroidLicenses.get(licenseName))
                }
            }
        }
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
