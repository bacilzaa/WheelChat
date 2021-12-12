package com.juniverse.wheelchat.ui.fragment

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipDescription
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import com.juniverse.wheelchat.R
import com.juniverse.wheelchat.base.DataBindingDialogFragment
import com.juniverse.wheelchat.databinding.DialogFragmentWheelChatBinding

class WheelChatDialogFragment : DataBindingDialogFragment<DialogFragmentWheelChatBinding>() {

    override fun layoutId(): Int = R.layout.dialog_fragment_wheel_chat

    @SuppressLint("ClickableViewAccessibility")
    override fun start() {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        viewBinding.apply {

            wheelBall.setOnTouchListener { view, motionEvent ->
                when (motionEvent.action) {
                    MotionEvent.ACTION_DOWN -> {

                        val item = ClipData.Item(view.tag as? CharSequence)

                        val dragData = ClipData(
                            view.tag as? CharSequence,
                            arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN), item
                        )

                        val myShadow = MyDragShadowBuilder((view as? ImageView)!!)

                        view.startDragAndDrop(
                            dragData,
                            myShadow,
                            null,
                            0
                        )

                        view.visibility = View.INVISIBLE
                        true
                    }
                    MotionEvent.ACTION_UP->{
                        view.visibility = View.VISIBLE
                        true
                    }
                    else->false
                }
            }
                wheelCircle.setOnDragListener(dragListen)
            }
        }

        private class MyDragShadowBuilder(v: ImageView) : View.DragShadowBuilder(v) {

            private val shadow = v

            override fun onProvideShadowMetrics(size: Point, touch: Point) {
                val width: Int = view.width

                val height: Int = view.height


                size.set(width, height)

                touch.set(width / 2, height / 2)

            }


            override fun onDrawShadow(canvas: Canvas) {
                shadow.setColorFilter(R.color.purple_200)
                shadow.draw(canvas)
            }



        }

        private val dragListen = View.OnDragListener { v, event ->

            when (event.action) {
                DragEvent.ACTION_DRAG_STARTED -> {

                    if (event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {

                        (v as? ImageView)?.setColorFilter(Color.BLUE)

                        v.invalidate()

                        true
                    } else {
                        false
                    }

                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    (v as? ImageView)?.setColorFilter(Color.GREEN)

                    v.invalidate()

                    true
                }
                DragEvent.ACTION_DRAG_LOCATION -> {
                    if(event.y > 153){

                    }
                    Log.i("DragMove","X:${event.x} Y:${event.y}")
                    true
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    (v as? ImageView)?.setColorFilter(Color.BLUE)

                    v.invalidate()

                    true
                }
                DragEvent.ACTION_DROP -> {
                    Log.i("DragMove",view?.id.toString())
                    (v as? ImageView)?.clearColorFilter()

                    v.invalidate()

                    true
                }
                DragEvent.ACTION_DRAG_ENDED -> {

                    (v as? ImageView)?.clearColorFilter()

                    v.invalidate()

                    when (event.result) {
                        true -> Log.i("Drop Status", "The drop was handled")
                        else -> Log.i("Drop Status", "The drop didn't work")
                    }

                    true
                }
                else -> {
                    Log.e("DragDrop", "Unknow action type received by OnDragListener")
                    false
                }
            }
        }


    }