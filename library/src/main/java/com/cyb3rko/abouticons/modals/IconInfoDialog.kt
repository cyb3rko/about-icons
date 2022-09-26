package com.cyb3rko.abouticons.modals

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
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.DialogFragment
import com.cyb3rko.abouticons.R
import com.cyb3rko.androidlicenses.AndroidLicenses
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
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

        // Views
        val header = view.findViewById<LinearLayout>(R.id.header)
        val iconView = view.findViewById<ImageView>(R.id.icon_view)
        val visitButton = view.findViewById<MaterialButton>(R.id.visit_button)

        val authorView = view.findViewById<MaterialTextView>(R.id.author_view)

        val websiteView = view.findViewById<MaterialTextView>(R.id.website_view)
        val linkButton = view.findViewById<ImageButton>(R.id.link_button)

        val modifiedContainer = view.findViewById<CardView>(R.id.modified_container)
        val modifiedView = view.findViewById<MaterialTextView>(R.id.modified_view)

        val licenseContainer = view.findViewById<LinearLayout>(R.id.license_container)
        val licenseNameView = view.findViewById<MaterialTextView>(R.id.license_name)

        iconView.setImageDrawable(drawable)
        if (link != "") {
            visitButton.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(link)))
            }
        } else {
            visitButton.visibility = View.GONE
        }
        authorView.text = author
        websiteView.text = website
        if (website == "[Missing]") {
            linkButton.visibility = View.GONE
        }
        if (modified) {
            modifiedView.text = "Modified"
        } else {
            modifiedContainer.visibility = View.GONE
        }
        licenseNameView.text = when (licenseName) {
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

        if (licenseNameView.text == "") licenseContainer.visibility = View.GONE

        setOnClickListeners(
            linkButton,
            licenseContainer,
            licenseNameView.text.isEmpty(),
            website
        )

        GlobalScope.launch {
            val backgroundColor = getAverageColor(drawable)
            (appContext as Activity).runOnUiThread {
                header.setBackgroundColor(backgroundColor)
            }
        }
    }

    private fun setOnClickListeners(
        linkButton: ImageButton,
        licenseContainer: LinearLayout,
        licenseEmpty: Boolean,
        website: String?
    ) {
        AndroidLicenses.init(appContext)

        linkButton.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://$website")))
        }

        if (!licenseEmpty) {
            licenseContainer.setOnClickListener {
                IconLicenseBottomSheet(licenseName).show(
                    parentFragmentManager,
                    IconLicenseBottomSheet.TAG
                )
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
