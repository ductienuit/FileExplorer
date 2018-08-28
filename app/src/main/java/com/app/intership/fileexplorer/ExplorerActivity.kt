package com.app.intership.fileexplorer

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v7.widget.RecyclerView
import com.app.intership.fileexplorer.adapter.ItemAdapter
import com.app.intership.fileexplorer.modal.Item
import java.io.File
import android.support.v7.widget.LinearLayoutManager
import android.widget.TextView
import android.widget.Toast
import uk.co.markormesher.android_fab.FloatingActionButton
import uk.co.markormesher.android_fab.SpeedDialMenuAdapter
import uk.co.markormesher.android_fab.SpeedDialMenuItem
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*


class ExplorerActivity : AppCompatActivity() {

    private lateinit var fileList :MutableList<Item>
    private lateinit var itemAdapter:ItemAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var fab:FloatingActionButton
    lateinit var root:File
    var speedDialSize = 4


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_explorer)
        fileList= mutableListOf()

        getDataIntent()

        addControl()

        listDir(root)

        //setting for recycler view
        recyclerView.setHasFixedSize(true)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = itemAdapter
    }

    private fun getDataIntent() {
        val intent = intent

        val bundle = intent.getBundleExtra("data")

        var path:String=""
        if(bundle!=null)
            path = bundle.getString("path")

        root = if(!path.isEmpty())
        {
            File(path)
        }
        else{
            File(Environment.getExternalStorageDirectory().absolutePath)
        }
    }

    private fun addControl() {
        recyclerView = findViewById(R.id.recyclerView)
        fab = findViewById(R.id.fab)

        fab.speedDialMenuAdapter = speedDialMenuAdapter
        fab.contentCoverEnabled = true
    }

    private fun listDir(f: File) {
        val files = f.listFiles()
        fileList.clear()

        for (file in files!!) {
            fileList.add(Item(file.name,file.extension,file.parent,file.path, file))
        }

        itemAdapter = ItemAdapter(this, fileList)
    }

    private val speedDialMenuAdapter = object: SpeedDialMenuAdapter() {
        val DELETE_FOLDER = 0
        val ADD_FOLDER =1
        val DELETE_FILES=2
        val ADD_FILE=3
        override fun getCount(): Int = speedDialSize

        override fun getMenuItem(context: Context, position: Int): SpeedDialMenuItem = when (position) {
            DELETE_FOLDER -> SpeedDialMenuItem(context, R.drawable.trash, "Delete folders selected")
            ADD_FOLDER -> SpeedDialMenuItem(context, R.drawable.ic_add, "Add folders selected")
            DELETE_FILES -> SpeedDialMenuItem(context, R.drawable.trash, "Delete files")
            ADD_FILE -> SpeedDialMenuItem(context, R.drawable.ic_add, "Add file")
            else -> throw IllegalArgumentException("No menu item: $position")
        }

        override fun onMenuItemClick(position: Int): Boolean {
            when (position) {
                DELETE_FOLDER -> {
                    deleteFilesSelected()
                }
                ADD_FOLDER -> {
                    val rand = Random()
                    rand.nextInt()
                    createFolder("New Folder"+rand.nextInt())
                }
                DELETE_FILES -> {
                    deleteFilesSelected()
                }
                ADD_FILE ->
                {
                    val rand = Random()
                    rand.nextInt()
                    createFile("hola"+rand.nextInt()+".txt")

                    refeshData()
                }
                else -> throw IllegalArgumentException("No menu item: $position")
            }
            return true
        }

        override fun onPrepareItemLabel(context: Context, position: Int, label: TextView) {
            // make the first item bold if there are multiple items
            // (this isn't a design pattern, it's just to demo the functionality)
            if (position == 0 && speedDialSize > 1) {
                label.setTypeface(label.typeface, Typeface.BOLD)
            }
        }
    }

    private fun createFolder(folder: String) {
        val folder = File(root.absolutePath +
                File.separator + folder)
        var success = true
        if (!folder.exists()) {
            success = folder.mkdirs()
        }
        if (success) {
            refeshData()
        } else {

        }
    }

    private fun deleteFilesSelected() {
        try{
            val iter = fileList.iterator()

            while (iter.hasNext()) {
                val i = iter.next()

                if (i.check)
                {
                    val file = i.file
                    val deleted = file.delete()
                    iter.remove()
                }
            }

        }
        catch (e:Exception)
        {
            e.printStackTrace()
        }
        finally {
            itemAdapter.notifyDataSetChanged()
        }
    }

    private fun refeshData() {
        val files = root.listFiles()
        fileList.clear()

        for (file in files!!) {
            fileList.add(Item(file.name,file.extension,file.parent,file.path, file))
        }
        itemAdapter.notifyDataSetChanged()
    }

    private fun createFile(name:String ) {
        try {
            if (!root.exists()) {
                root.mkdirs()
            }
            val f = File(root.absolutePath +"/"+ name)
            if (f.exists()) {
                f.delete()
            }
            f.createNewFile()

            val out = FileOutputStream(f)

            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
