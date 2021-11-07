package com.example.inappcamerademo

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.inappcamerademo.databinding.CapturedImageViewBinding
import com.example.inappcamerademo.utils.ViewOnClick
import java.io.File

class ImageListRVAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TAG = "ImageListRVAdapter"
    }

    private val list: ArrayList<File> by lazy { ArrayList() }
    var clickListener: ViewOnClick? = null

    fun setImages(list: Array<File>?) {
        this.list.clear()
        if (list != null && list.isNotEmpty()) {
            this.list.addAll(list.asList())
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val context = parent.context
        val binding = CapturedImageViewBinding.inflate(LayoutInflater.from(context), parent, false)
        return ImageViewHolder(binding, clickListener)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        with(holder as ImageViewHolder) {
            bind(list[position])
        }
    }

    override fun getItemCount() = list.size

    private class ImageViewHolder(
        val binding: CapturedImageViewBinding,
        val clickListener: ViewOnClick?
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(file: File) {
            Glide.with(binding.root.context)
                .load(file)
                .into(binding.image)

            binding.image.setOnClickListener {
                clickListener?.onClick(file.absolutePath)
            }
        }

    }

}