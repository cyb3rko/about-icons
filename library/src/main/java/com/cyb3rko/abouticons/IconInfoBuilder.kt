package com.cyb3rko.abouticons

import android.content.Context
import android.content.Intent

import java.io.Serializable

class IconInfoBuilder(val appContext: Context) : Serializable {

    private var author = ""
    private var drawableId = 0
    private var licenseName = ""
    private var modified = false
    private var website = ""

    fun setDrawable(drawableId: Int): IconInfoBuilder {
        this.drawableId = drawableId
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

    fun start() {
        val intent = Intent(appContext, IconInfoActivity::class.java)
        intent.putExtra("author", author)
        intent.putExtra("drawableId", drawableId)
        intent.putExtra("licenseName", licenseName)
        intent.putExtra("modified", modified)
        intent.putExtra("website", website)
        appContext.startActivity(intent)
    }
}
