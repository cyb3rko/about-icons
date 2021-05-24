package com.cyb3rko.abouticons

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewAdapter(
    val appContext: Context,
    var modelList: ArrayList<IconModel>,
    drawableClass: Class<*>,
    val allowModificationAnnotation: Boolean
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
        val genericViewHolder = holder as ViewHolder

        if (allowModificationAnnotation && model!!.modified) {
            genericViewHolder.itemLinLayout.setBackgroundColor(ContextCompat.getColor(appContext, R.color.colorModified))
        } else {
            genericViewHolder.itemLinLayout.setBackgroundColor(ContextCompat.getColor(appContext, R.color.colorNotModified))
        }
        genericViewHolder.imgUser.setImageDrawable(usedDrawables[position])
        if (model!!.iconLicense.isNotEmpty()) {
            genericViewHolder.itemLicense.visibility = View.VISIBLE
            genericViewHolder.itemLicense.text = when (model.iconLicense) {
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
            genericViewHolder.itemLicense.visibility = View.GONE
        }
        val author = model.author
        genericViewHolder.itemTxtTitle.text = (if (author != "[Missing]") "by " else "") + author
        genericViewHolder.itemTxtMessage.text = model.website
    }

    override fun getItemCount(): Int = modelList.size

    private fun getUsedDrawables(appContext: Context, drawableClass: Class<*>) {
        val resources = appContext.resources
        val allDrawables = drawableClass.fields
        usedDrawables = ArrayList()
        drawableIds = ArrayList()
        for (field in allDrawables) {
            try {
                if (resources.getResourceEntryName(field.getInt(null)).startsWith("_")) {
                    usedDrawables.add(ResourcesCompat.getDrawable(resources, field.getInt(null), appContext.theme)!!)
                    drawableNames.add(resources.getResourceEntryName(field.getInt(null)))
                    drawableIds.add(field.getInt(null))
                }
            } catch (e: IllegalAccessException) {
                Log.e("About Icons", "IllegalAccessException while reading used drawables.")
            }
        }
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        this.itemClickListener = onItemClickListener!!
    }

    private fun getItem(position: Int): IconModel? {
        return modelList[position]
    }

    fun getDrawableInt(model: IconModel?): Int  = drawableIds[modelList.indexOf(model)]

    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int, model: IconModel?)
    }

    fun getIconListSize(): Int = usedDrawables.size

    fun getDrawableName(index: Int): String? = drawableNames[(index)].substring(1)

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemLinLayout: LinearLayout = itemView.findViewById(R.id.linearLayout)
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