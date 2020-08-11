package com.cyb3rko.abouticons

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

import es.dmoral.toasty.Toasty

import java.util.*

@SuppressLint("InflateParams")
class AboutIcons(private val appContext: Context, private val drawableClass: Class<*>) {

    private var allowModificationAnnotation: Boolean = true
    private lateinit var mAdapter: RecyclerViewAdapter
    private val modelList = ArrayList<IconModel>()
    private var recyclerView: RecyclerView
    private var view: View

    init {
        Toasty.Config.getInstance().allowQueue(false).apply()

        view = LayoutInflater.from(appContext).inflate(R.layout.activity_icon_view, null)
        recyclerView = view.findViewById(R.id.recycler_view)
    }

    private fun setAdapter() {
        mAdapter = RecyclerViewAdapter(appContext, modelList, drawableClass, allowModificationAnnotation)

        for (i in 0 until mAdapter.iconListSize) {
            addAttributes(i)
        }

        val layoutManager = GridLayoutManager(appContext, 2)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = mAdapter

        onItemClick()
    }

    private fun addAttributes(index: Int) {
        var modified = false
        var iconAuthor = "[Missing]"
        var iconWebsite = "[Missing]"
        var iconLink = ""

        try {
            val arrayId = appContext.resources.getIdentifier(mAdapter.getDrawableName(index), "array", appContext.packageName)
            val iconInformation = appContext.resources.getStringArray(arrayId)
            modified = iconInformation[3]!!.toBoolean()
            iconAuthor = "by ${iconInformation[0]}"
            iconWebsite = iconInformation[1]
            iconLink = iconInformation[2]
        } catch (ignored: Exception) {
        }

        modelList.add(IconModel(modified, iconAuthor, iconWebsite, iconLink))
    }

    private fun onItemClick() {
        mAdapter.setOnItemClickListener { _, _, model ->
            if (model.iconLink != "") {
                appContext.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(model.iconLink)))
            } else {
                Toasty.warning(appContext, "Link not set!", Toasty.LENGTH_SHORT).show()
            }
        }
    }

    fun hideTitle(): AboutIcons {
        val titleView: TextView = view.findViewById(R.id.title_view)
        titleView.visibility = View.GONE
        return this
    }

    fun setTitle(customTitle: String): AboutIcons {
        val titleView: TextView = view.findViewById(R.id.title_view)
        titleView.text = customTitle
        return this
    }

    fun hideModificationAnnotation(): AboutIcons {
        view.findViewById<TextView>(R.id.modified_info).visibility = View.GONE
        allowModificationAnnotation = false
        return this
    }

    fun get(): View {
        setAdapter()
        return view
    }
}