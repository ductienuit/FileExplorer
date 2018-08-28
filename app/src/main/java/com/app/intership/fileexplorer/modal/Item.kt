package com.app.intership.fileexplorer.modal

import java.io.File
import java.util.*

class Item {
     var name:String
     var extention:String
     var parentDir:String
     var childDir:String
        var file:File
     //var date: Date

    constructor(name: String, extention: String, parentDir: String, childDir: String, file:File) {
        this.name = name
        this.extention = extention
        this.parentDir = parentDir
        this.childDir = childDir
        this.file = file
        //this.date = date
    }

    override fun toString(): String {
        return "Item(name='$name', extention='$extention', parentDir='$parentDir', childDir='$childDir')"
    }
}