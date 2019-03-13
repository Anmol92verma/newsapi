package com.tfexample.newsapisample.lrucache

import android.support.v4.util.ArrayMap
import java.io.File


// Least Recently used cache
class LRUCache(val maxCacheSize: Int) {
    val arrayMap = ArrayMap<Int, Entry>()

    var start: Entry? = null
    var end: Entry? = null

    fun getEntry(key: Int): File? {
        arrayMap.containsKey(key = key).let {
            if (it) {
                val entry = arrayMap[key]
                removeNode(entry)
                addAtTop(entry)
                return entry?.value
            }
        }
        return null
    }

    fun putEntry(key: Int, value: File) {
        if (arrayMap.containsKey(key)) {
            // Key Already Exist, just update the value and move it to top
            val entry = arrayMap.get(key)
            entry?.value = value
            removeNode(entry)
            addAtTop(entry)
        } else {
            val newnode = Entry()
            newnode.left = null
            newnode.right = null
            newnode.value = value
            newnode.key = key
            if (arrayMap.size > maxCacheSize)
            // We have reached maxium size so need to make room for new element.
            {
                arrayMap.remove(end?.key)
                removeNode(end)
                addAtTop(newnode)

            } else {
                addAtTop(newnode)
            }

            arrayMap[key] = newnode
        }
    }

    private fun addAtTop(node: Entry?) {
        node?.right = start
        node?.left = null
        start?.let {
            it.left = node
        } ?: kotlin.run {
            start = node
        }
        if (end == null)
            end = start
    }

    private fun removeNode(node: Entry?) {
        node?.left?.let {
            it.left?.right = node.right
        } ?: kotlin.run {
            start = node?.right
        }

        node?.right?.let {
            it.right?.left = node.left
        } ?: kotlin.run {
            end = node?.left
        }
    }

    inner class Entry {
        var value: File? = null
        var key: Int = 0
        var left: Entry? = null
        var right: Entry? = null
    }
}