package com.cyb3rko.abouticons

import android.content.Context
import androidx.fragment.app.FragmentManager

internal class IconInfoBuilder(
    private val appContext: Context,
    private val fragmentManager: FragmentManager
) {

    private var author = ""
    private var drawableId = 0
    private var licenseName = ""
    private var link = ""
    private var modified = false
    private var website = ""

    fun setDrawable(drawableId: Int): IconInfoBuilder {
        this.drawableId = drawableId
        return this
    }

    fun setLink(link: String): IconInfoBuilder {
        this.link = link
        return this
    }

    fun setAuthor(author: String): IconInfoBuilder {
        this.author = author
        return this
    }

    fun setWebsite(website: String): IconInfoBuilder {
        this.website = website
        return this
    }

    fun setModifiedInfo(modified: Boolean): IconInfoBuilder {
        this.modified = modified
        return this
    }

    fun setLicense(licenseName: String): IconInfoBuilder {
        this.licenseName = licenseName
        return this
    }

    fun start() = IconInfoDialog(
        appContext,
        author,
        drawableId,
        licenseName,
        link,
        modified,
        website
    ).show(fragmentManager, IconInfoDialog.TAG)
}
