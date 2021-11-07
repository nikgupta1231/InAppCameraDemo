package com.example.inappcamerademo.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.GridView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.example.inappcamerademo.ImageViewBaseAdapter
import com.example.inappcamerademo.MainDbHelper
import com.example.inappcamerademo.R
import dagger.hilt.android.AndroidEntryPoint

class ImagesGridViewActivity : AppCompatActivity(), OnItemClickListener {
    private var viewBaseAdapter: ImageViewBaseAdapter? = null
    private var imageFileNameList: MutableList<String>? = null
    private var mainDbHelper: MainDbHelper? = null
    override fun onResume() {
        super.onResume()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_images_view)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = "Images"
        val gridView = findViewById<GridView>(R.id.images_grid_view)
        mainDbHelper = MainDbHelper.getInstance(this.applicationContext)
        imageFileNameList?.clear()
        mainDbHelper?.allImageNames?.let { imageFileNameList?.addAll(it) }
        viewBaseAdapter = imageFileNameList?.let {
            ImageViewBaseAdapter(
                this.applicationContext,
                this@ImagesGridViewActivity as Activity,
                it
            )
        }
        gridView.adapter = viewBaseAdapter
        gridView.onItemClickListener = this
    }

    override fun onItemClick(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
        val intent = Intent(this, ImageDetailedViewActivity::class.java)
        intent.putExtra("name", imageFileNameList!![i])
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuInflater = menuInflater
        menuInflater.inflate(R.menu.menu_images_view, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_sort_by_name -> {
                Log.v(TAG, "Sorted by name")
                imageFileNameList!!.clear()
                imageFileNameList!!.addAll(mainDbHelper!!.allImageNamesSortedByName)
                viewBaseAdapter!!.notifyDataSetChanged()
            }
            R.id.action_sort_by_time -> {
                Log.v(TAG, "Sorted by time")
                imageFileNameList!!.clear()
                imageFileNameList!!.addAll(mainDbHelper!!.allImageNamesSortedByTime)
                viewBaseAdapter!!.notifyDataSetChanged()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val TAG = "ImagesGridViewActivity"
    }
}