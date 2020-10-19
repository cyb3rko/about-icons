package com.cyb3rko.abouticons

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.dmoral.toasty.Toasty
import java.util.*

class AboutIcons(private val appContext: Context, private val drawableClass: Class<*>) {

    private var allowModificationAnnotation: Boolean = true
    private lateinit var mAdapter: RecyclerViewAdapter
    private val modelList = ArrayList<IconModel>()
    private val modifiedContainer: LinearLayout
    private var recyclerView: RecyclerView
    private var titleView: TextView
    private var view: View

    init {
        Toasty.Config.getInstance().allowQueue(false).apply()

        @SuppressLint("InflateParams")
        view = LayoutInflater.from(appContext).inflate(R.layout.activity_icon_view, null)
        recyclerView = view.findViewById(R.id.recycler_view)
        modifiedContainer = view.findViewById(R.id.modified_container)
        titleView = view.findViewById(R.id.title_view)
    }

    private fun setAdapter() {
        mAdapter = RecyclerViewAdapter(appContext, modelList, drawableClass, allowModificationAnnotation)

        for (i in 0 until mAdapter.getIconListSize()) {
            addAttributes(i)
        }

        val layoutManager = GridLayoutManager(appContext, 2)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = mAdapter

        onItemClick()
    }

    private fun addAttributes(index: Int) {
        var iconAuthor = "[Missing]"
        var iconWebsite = "[Missing]"
        var iconLink = ""
        var modified = false
        var iconLicense = ""

        try {
            val arrayId = appContext.resources.getIdentifier(mAdapter.getDrawableName(index), "array", appContext.packageName)
            val iconInformation = appContext.resources.getStringArray(arrayId)
            iconAuthor = iconInformation[0]
            iconWebsite = iconInformation[1]
            iconLink = iconInformation[2]
            modified = iconInformation[3]!!.toBoolean()
            iconLicense = iconInformation[4]
        } catch (ignored: Exception) {
        }

        modelList.add(IconModel(iconAuthor, iconWebsite, iconLink, modified, iconLicense))
    }

    private fun onItemClick() {
        mAdapter.setOnItemClickListener(object: RecyclerViewAdapter.OnItemClickListener {
            override fun onItemClick(view: View?, position: Int, model: IconModel?) {
                if (model != null) {
                    IconInfoBuilder(appContext)
                        .setDrawable(mAdapter.getDrawableInt(model))
                        .setLink(model.iconLink)
                        .setAuthor(model.author)
                        .setWebsite(model.website)
                        .setModifiedInfo(model.modified)
                        .setLicense(model.iconLicense)
                        .start()
                }
            }
        })
    }

    fun setTitle(customTitle: String): AboutIcons {
        if (customTitle.isNotBlank()) {
            titleView.text = customTitle
        } else {
            titleView.visibility = View.GONE
        }
        return this
    }

    fun setTitleSize(customSize: Float): AboutIcons {
        titleView.textSize = customSize
        return this
    }

    fun setModificationTitle(customTitle: String): AboutIcons {
        view.findViewById<TextView>(R.id.modified_text).text = customTitle
        return this
    }

    fun setModificationTitleSize(customSize: Float): AboutIcons {
        view.findViewById<TextView>(R.id.modified_text).textSize = customSize
        return this
    }

    fun hideModificationAnnotation(): AboutIcons {
        modifiedContainer.visibility = View.GONE
        allowModificationAnnotation = false
        return this
    }

    fun get(): View {
        setAdapter()
        if (titleView.visibility == View.GONE && modifiedContainer.visibility == View.GONE) {
            view.findViewById<CardView>(R.id.header).visibility = View.GONE
        }
        return view
    }

    @Deprecated(message = "Instead use setTitle(\"\")",replaceWith = ReplaceWith("this.setTitle(\"\")"))
    fun hideTitle(): AboutIcons {
        val titleView: TextView = view.findViewById(R.id.title_view)
        titleView.visibility = View.GONE
        return this
    }
}
