package com.app.intership.fileexplorer.adapter

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.app.intership.fileexplorer.R
import com.app.intership.fileexplorer.modal.Item
import android.support.v4.content.ContextCompat
import android.widget.Toast
import com.app.intership.fileexplorer.ExplorerActivity
import java.util.ArrayList
import android.content.ActivityNotFoundException
import android.net.Uri
import android.support.v4.content.ContextCompat.startActivity
import android.webkit.MimeTypeMap




class ItemAdapter(context: Context, listFile: MutableList<Item>) : RecyclerView.Adapter<ItemViewHolder>()
{
    private var context: Context? = context
    private var fileList: MutableList<Item>? = listFile
    internal val REQUESTCODE = 10

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return fileList!!.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = fileList!!.get(position)

        holder.txtDateOpened.text = "28/8/2018"//item.date.toString()
        holder.txtName.text = item.name

        when (item.extention) {
            "jpg", "png" -> {
                val drawable = ContextCompat.getDrawable(this.context!!.applicationContext, R.drawable.picture)
                holder.iconExtention.setImageDrawable(drawable)
            }
            "txt" -> {
                val drawable = ContextCompat.getDrawable(this.context!!.applicationContext, R.drawable.txt)
                holder.iconExtention.setImageDrawable(drawable)
            }
            "mp3" -> {
                val drawable = ContextCompat.getDrawable(this.context!!.applicationContext, R.drawable.mp3)
                holder.iconExtention.setImageDrawable(drawable)
            }
            "apk" -> {
                val drawable = ContextCompat.getDrawable(this.context!!.applicationContext, R.drawable.apk)
                holder.iconExtention.setImageDrawable(drawable)
            }
            else -> {
                if(item.file.isDirectory)
                {
                    val drawable = ContextCompat.getDrawable(this.context!!.applicationContext, R.drawable.folder)
                    holder.iconExtention.setImageDrawable(drawable)
                }
                else{
                    val drawable = ContextCompat.getDrawable(this.context!!.applicationContext, R.drawable.file)
                    holder.iconExtention.setImageDrawable(drawable)
                }
            }
        }
        holder.itemView.setOnClickListener {
            if(item.file.isFile)
            {
                val newIntent = Intent(Intent.ACTION_VIEW)
                var mimeType = getMimeType(item.file.absolutePath)

                if(item.file.extension.equals("mp3"))
                    mimeType = "audio/*"
                newIntent.setDataAndType(Uri.fromFile(item.file), mimeType)
                newIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                try {
                    context!!.startActivity(newIntent)
                } catch (e: ActivityNotFoundException) {
                    Toast.makeText(context, "No handler for this type of file.", Toast.LENGTH_LONG).show()
                }

            }
            else if(item.file.isDirectory)
            {
                val intent = Intent(context, ExplorerActivity::class.java)
                var data = Bundle()

                data.putString("path", item.file.absolutePath.toString())
                intent.putExtra("data",data)

                context!!.startActivity(intent)
            }
        }
    }

    fun getMimeType(url: String): String? {
        var type: String? = null
        val extension = MimeTypeMap.getFileExtensionFromUrl(url)
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        }
        return type
    }
}
class ItemViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
    lateinit var checkItem:CheckBox
    lateinit var txtName:TextView
    lateinit var txtDateOpened:TextView
    lateinit var iconExtention:ImageView
    init {
        if (itemView != null) {
            checkItem = itemView.findViewById(R.id.checkItem)
            txtName = itemView.findViewById(R.id.txtName)
            txtDateOpened = itemView.findViewById(R.id.txtDateOpened)
            iconExtention = itemView.findViewById(R.id.iconExtention)
        }
    }
}