package by.dro.testapp.fileexplorer.ui

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.dro.testapp.fileexplorer.R
import by.dro.testapp.fileexplorer.viewmodels.FileViewModel
import kotlinx.android.synthetic.main.fragment_base_explorer.*
import java.io.File


abstract class BaseExplorerFragment : Fragment(R.layout.fragment_base_explorer) {

    abstract val viewModel: FileViewModel
    private val adapter = FileAdapter{
            Log.d("kkk", "mediaType - ${it.mediaType}")

        when(it.mediaType){

            MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO ->
                startActivity(VideoPlayerActivity.newIntent(context, it.path))

            MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE ->
                PhotoDialogFragment.newInstance(it.path).show(childFragmentManager, "OpenPhoto")
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fileRecycler.layoutManager = getLayoutManager(requireContext())
        fileRecycler.adapter = adapter

        viewModel.listMedia.observe(viewLifecycleOwner, Observer {

            Log.d("kkk", "internal - $it")

           adapter.submitList(it)

        })


    }



    private fun getLayoutManager(context: Context): RecyclerView.LayoutManager {

        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val size = Point()
        display.getSize(size)
        val width: Int = size.x
//        val height: Int = size.y

        val spanCount = (width / context.resources.getDimension(R.dimen.file_vh_width)).toInt()

        return GridLayoutManager(context, spanCount)
    }

    fun onBackPressed(): Boolean {

        return if (isResumed) {

            true
        } else false

    }

    override fun onResume() {
        super.onResume()
        Log.d("kkk", "onResume")
    }
}