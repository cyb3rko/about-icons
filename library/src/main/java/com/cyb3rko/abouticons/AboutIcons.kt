package com.cyb3rko.abouticons

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*
import java.util.*

class AboutIcons(
    private val appContext: Context,
    private val drawableClass: Class<*>,
    private val fragmentManager: FragmentManager
) {

    private var allowModificationAnnotation: Boolean = true
    private lateinit var mAdapter: RecyclerViewAdapter
    private val modelList = ArrayList<IconModel>()
    private val modifiedContainer: LinearLayout
    private var progressBar: ProgressBar
    private var recyclerView: RecyclerView
    private var titleView: TextView
    private var view: View

    init {
        @SuppressLint("InflateParams")
        view = LayoutInflater.from(appContext).inflate(R.layout.activity_icon_view, null)
        recyclerView = view.findViewById(R.id.recycler_view)
        modifiedContainer = view.findViewById(R.id.modified_container)
        progressBar = view.findViewById(R.id.progress_bar)
        titleView = view.findViewById(R.id.title_view)
    }

    private fun setAdapter() {
        val layoutManager = GridLayoutManager(appContext, 2)
        recyclerView.layoutManager = layoutManager

        CoroutineScope(Dispatchers.Main).launch {
            mAdapter = RecyclerViewAdapter(appContext, modelList, drawableClass, allowModificationAnnotation)
            for (i in 0 until mAdapter.getIconListSize()) {
                val task = async { fetchAttributes(i) }
                modelList.add(task.await())
            }

            recyclerView.adapter = mAdapter
            onItemClick()
            progressBar.visibility = View.GONE
        }
    }

    private fun fetchAttributes(index: Int): IconModel {
        val missingHint = "[${appContext.getString(R.string.missing)}]"
        var iconAuthor = missingHint
        var iconWebsite = missingHint
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

        return IconModel(iconAuthor, iconWebsite, iconLink, modified, iconLicense)
    }

    private fun onItemClick() {
        mAdapter.setOnItemClickListener(object: RecyclerViewAdapter.OnItemClickListener {
            override fun onItemClick(view: View?, position: Int, model: IconModel?) {
                if (model != null) {
                    IconInfoBuilder(appContext, fragmentManager)
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
        } else {
            view.findViewById<ImageView>(R.id.color_sample).colorFilter = PorterDuffColorFilter(ContextCompat.getColor(appContext,
                R.color.colorModified), PorterDuff.Mode.SRC_ATOP)
        }
        return view
    }

    @Deprecated(message = "Instead use setTitle(\"\")", replaceWith = ReplaceWith("this.setTitle(\"\")"))
    fun hideTitle(): AboutIcons {
        val titleView: TextView = view.findViewById(R.id.title_view)
        titleView.visibility = View.GONE
        return this
    }
}
