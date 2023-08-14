package com.mypet.util

import android.content.Context
import android.graphics.Bitmap
import android.widget.ImageView
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import java.io.File
import java.io.FileOutputStream

object ImageUtils {
    private const val PROFILE_DIRECTORY = "/profile"
    private const val PICTURE_NAME = "profilepic.png"
    private var counter = 1
    fun saveImage(context: Context, bitmap: Bitmap) {
        counter++
        val direct = File(context.applicationInfo.dataDir + PROFILE_DIRECTORY)
        if (!direct.exists()) {
            direct.mkdirs()
        }
        val imageFIle = File(context.applicationInfo.dataDir + PROFILE_DIRECTORY, PICTURE_NAME)
        if (imageFIle.exists()) {
            imageFIle.delete()
        }
        val out = FileOutputStream(imageFIle)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
        out.flush()
        out.close()
    }

    fun loadImageIfExists(context: Context, target: ImageView) {
        val file = File("${context.applicationInfo.dataDir}$PROFILE_DIRECTORY/$PICTURE_NAME")
        if (file.exists()) {
            Picasso.get().invalidate(file)
            Picasso.get().load(file).memoryPolicy(MemoryPolicy.NO_CACHE).into(target)
        }
    }

}