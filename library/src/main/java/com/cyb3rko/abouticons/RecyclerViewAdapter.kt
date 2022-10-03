package com.cyb3rko.abouticons

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

class RecyclerViewAdapter(
    private val appContext: Context,
    private var modelList: ArrayList<IconModel>,
    drawableClass: Class<*>,
    private val allowModificationAnnotation: Boolean
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var drawableIds: ArrayList<Int>
    private val drawableNames = ArrayList<String>()
    private lateinit var itemClickListener: OnItemClickListener
    private lateinit var usedDrawables: ArrayList<Drawable>

    init {
        getUsedDrawables(appContext, drawableClass)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_list, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = getItem(position)
        val viewholder = holder as ViewHolder

        val cardBackground = if (allowModificationAnnotation && model.modified) {
            R.drawable.cardview_modified_background
        } else {
            R.drawable.cardview_background
        }
        viewholder.container.background = ResourcesCompat.getDrawable(
            appContext.resources,
            cardBackground,
            appContext.theme
        )
        viewholder.imgUser.setImageDrawable(usedDrawables[position])
        if (model.iconLicense.isNotEmpty()) {
            viewholder.itemLicense.visibility = View.VISIBLE
            viewholder.itemLicense.text = when (model.iconLicense) {
                "apache_2.0" -> "Apache 2.0"
                "mit" -> "MIT License"
                "cc_by_sa_3.0" -> "CC BY-SA 3.0"
                "cc_by_3.0" -> "CC BY 3.0"
                "cc_by_4.0" -> "CC BY 4.0"
                "cc_by_sa_4.0" -> "CC BY-SA 4.0"
                "cc_by_nc_3.0" -> "CC BY-NC 3.0"
                "cc_by_nc_sa_3.0" -> "CC BY-NC-SA 3.0"
                else -> "..."
            }
        } else {
            viewholder.itemLicense.visibility = View.GONE
        }
        val author = model.author
        viewholder.itemTxtTitle.text = (
                if (author != "[${appContext.getString(R.string.missing)}]") {
                    "by "
                } else {
                    ""
                }) + author
        viewholder.itemTxtMessage.text = model.website
    }

    override fun getItemCount(): Int = modelList.size

    private fun getUsedDrawables(appContext: Context, drawableClass: Class<*>) {
        val resources = appContext.resources
        val allDrawables = drawableClass.fields
        usedDrawables = ArrayList()
        drawableIds = ArrayList()
        for (field in allDrawables) {
            try {
                val fieldId = field.getInt(null)
                if (resources.getResourceEntryName(fieldId).startsWith("_")) {
                    usedDrawables.add(ResourcesCompat.getDrawable(resources, fieldId, appContext.theme)!!)
                    drawableNames.add(resources.getResourceEntryName(fieldId))
                    drawableIds.add(fieldId)
                }
            } catch (e: IllegalAccessException) {
                Log.e("About Icons", "IllegalAccessException while reading used drawables.")
            }
        }
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        this.itemClickListener = onItemClickListener!!
    }

    private fun getItem(position: Int): IconModel {
        return modelList[position]
    }

    fun getDrawableInt(model: IconModel?): Int = drawableIds[modelList.indexOf(model)]

    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int, model: IconModel?)
    }

    fun getIconListSize(): Int = usedDrawables.size

    fun getDrawableName(index: Int): String = drawableNames[index].substring(1)

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var container: MaterialCardView = itemView.findViewById(R.id.container)
        var imgUser: ImageView = itemView.findViewById(R.id.img_user)
        var itemLicense: TextView = itemView.findViewById(R.id.item_license)
        var itemTxtTitle: TextView = itemView.findViewById(R.id.item_txt_title)
        var itemTxtMessage: TextView = itemView.findViewById(R.id.item_txt_message)

        init {
            itemView.setOnClickListener {
                itemClickListener.onItemClick(itemView, adapterPosition, modelList[adapterPosition])
            }
        }
    }
}