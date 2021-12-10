package com.juniverse.wheelchat.helper

import androidx.databinding.BindingAdapter
import com.juniverse.wheelchat.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


@BindingAdapter("imageUrl")
fun setImage(image: CircleImageView, url: String?) {

    Picasso.get()
        .load(url?:"")
        .error(R.drawable.ic_wheelchat_icon)
        .fit()
        .noFade()
        .into(image)

}
