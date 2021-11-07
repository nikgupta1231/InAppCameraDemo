package com.example.inappcamerademo.ui.camera

import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.inappcamerademo.ImageListRVAdapter
import com.example.inappcamerademo.R
import com.example.inappcamerademo.databinding.ImagesListScreenFragmentBinding
import com.example.inappcamerademo.utils.ViewOnClick
import kotlinx.coroutines.launch
import java.io.File

class ImageListingFragment : Fragment() {

    companion object {
        const val TAG = "ImagesListFragment"
    }

    private lateinit var binding: ImagesListScreenFragmentBinding
    private lateinit var adapter: ImageListRVAdapter

    private val clickListener = View.OnClickListener {
        when (it.id) {
            binding.btnBack.id -> {
                findNavController().navigateUp()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.images_list_screen_fragment,
            container,
            false
        )
        binding.clickListener = clickListener
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        initData()

    }


    private fun initData() {
        loadImages()
    }

    private fun initView() {
        binding.rvImageList.layoutManager =
            StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL)
        binding.rvImageList.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                outRect: Rect,
                view: View,
                parent: RecyclerView,
                state: RecyclerView.State
            ) {
                val pos = parent.getChildAdapterPosition(view)
                if ((pos + 1) % 3 != 0) {
                    outRect.right = 1
                }
            }
        })
        adapter = ImageListRVAdapter()
        adapter.clickListener = object : ViewOnClick {
            override fun <T> onClick(t: T) {
                val args = Bundle().apply {
                    putString("PATH", t as String)
                }
                findNavController().navigate(
                    R.id.action_imagesListScreenFragment_to_imageViewFragment,
                    args
                )
            }
        }
        binding.rvImageList.adapter = adapter
    }

    private fun loadImages() {
        val dir = getOutputDirectory()
        if (dir.exists() && dir.isDirectory) {
            viewLifecycleOwner.lifecycleScope.launch {
                val imagesList = dir.listFiles()
                Log.d(TAG, "loadImages: size: ${imagesList?.size}")
                adapter.setImages(imagesList)
            }
        }
    }

    private fun getOutputDirectory(): File {
        val mediaDir = context?.externalMediaDirs?.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists())
            mediaDir else requireActivity().filesDir
    }

}