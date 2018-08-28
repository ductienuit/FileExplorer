package com.app.intership.fileexplorer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v7.widget.RecyclerView
import com.app.intership.fileexplorer.adapter.ItemAdapter
import com.app.intership.fileexplorer.modal.Item
import java.io.File
import android.support.v7.widget.LinearLayoutManager


class ExplorerActivity : AppCompatActivity() {

    private lateinit var fileList :MutableList<Item>
    private lateinit var itemAdapter:ItemAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var root:File

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
    }

    private fun listDir(f: File) {
        val files = f.listFiles()
        fileList.clear()

        for (file in files!!) {
            fileList.add(Item(file.name,file.extension,file.parent,file.path, file))
        }

        itemAdapter = ItemAdapter(this, fileList)
    }
}
