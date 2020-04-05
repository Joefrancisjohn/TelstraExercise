package com.example.telstra.model


import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.telstra.R


data class FactsData(
    val rows: List<Row>,
    val title: String
)

data class Row(
    val description: String,
    val imageHref: String,
    val title: String
)
object DataBindingAdapter {
    @BindingAdapter("imageHref")
    @JvmStatic
    fun loadImage(imageView: ImageView, imageURL: String?) {
        Glide.with(imageView.getContext())
            .setDefaultRequestOptions(
                RequestOptions()
                    .circleCrop()
            )
            .load(imageURL)
            //.override(100, Target.SIZE_ORIGINAL)
            .placeholder(R.drawable.ic_image_place_holder)
            .error(R.drawable.ic_no_image)
            .fitCenter()
            .into(imageView)
    }
}