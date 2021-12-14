package com.juniverse.wheelchat.helper

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.juniverse.wheelchat.R
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.flow.callbackFlow


@BindingAdapter("imageUrl")
fun setImage(image: CircleImageView, url: String?) {

    Picasso.get()
        .load(url?:"")
        .error(R.drawable.ic_wheelchat_icon)
        .fit()
        .noFade()
        .into(image)
}
@BindingAdapter("customSrc")
fun setSrc(image: ImageView,id:Int){

    image.setImageResource(id)

}
