package com.md29.husein.myappsstory.view.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.md29.husein.myappsstory.R
import com.md29.husein.myappsstory.data.local.StoryDatabase

internal class StackRemoteViewsFactory(
    private val mContext: Context,
) : RemoteViewsService.RemoteViewsFactory {

    private val dataModelList = ArrayList<String>()


    override fun onCreate() {

    }

    override fun onDataSetChanged() {
        val database = StoryDatabase.getDatabase(mContext)
        dataModelList.clear()
        dataModelList.addAll(database.storyDao().getAllPhotoUrls())
    }

    override fun onDestroy() {

    }

    override fun getCount(): Int = dataModelList.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)

        val imageData = dataModelList[position]
        val bitmap = loadBitmapFromUrl(imageData)
        if (bitmap != null) {
            rv.setImageViewBitmap(R.id.imageView, bitmap)
        }

        val extras = bundleOf(
            StoryBannerWidget.EXTRA_ITEM to position
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false

    private fun loadBitmapFromUrl(url: String): Bitmap? {
        try {
            return Glide.with(mContext)
                .asBitmap()
                .load(url)
                .submit()
                .get()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

}