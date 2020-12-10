package pt.amn.projectacademy

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide

fun ImageView.loadImage(view: View, imagePath: String) {
    Glide.with(view)
        .load(imagePath)
        .into(this@loadImage)
}