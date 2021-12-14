package com.juniverse.wheelchat.ui.fragment

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipDescription
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.Observer
import com.juniverse.wheelchat.R
import com.juniverse.wheelchat.base.DataBindingDialogFragment
import com.juniverse.wheelchat.databinding.DialogFragmentWheelChatBinding
import com.juniverse.wheelchat.model.User
import com.juniverse.wheelchat.ui.activity.home.MainActivity
import com.juniverse.wheelchat.ui.activity.home.MainActivity.Companion.CURRENT_USER_KEY
import com.juniverse.wheelchat.viewmodel.FirebaseViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class WheelChatDialogFragment : DataBindingDialogFragment<DialogFragmentWheelChatBinding>() {

    companion object {
        const val USER_KEY = "USER_KEY"

        fun newInstance(currentUser: User, user: User): WheelChatDialogFragment {
            val fragment = WheelChatDialogFragment()

            val bundle = Bundle().apply {
                putParcelable(MainActivity.CURRENT_USER_KEY, currentUser)
                putParcelable(USER_KEY, user)
            }

            fragment.arguments = bundle

            return fragment
        }
    }

    override fun layoutId(): Int = R.layout.dialog_fragment_wheel_chat

    private var currentUser: User? = null

    private var user: User? = null

    private val viewModel: FirebaseViewModel by sharedViewModel()

    @SuppressLint("ClickableViewAccessibility")
    override fun start() {

        currentUser = this.arguments?.getParcelable(CURRENT_USER_KEY)
        user = this.arguments?.getParcelable(USER_KEY)


        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        initObserver()

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
                    else -> false
                }
            }

            wheelChatAreaTop.setOnDragListener(dragListen)
            wheelChatAreaBottom.setOnDragListener(dragListen)
            wheelChatAreaRight.setOnDragListener(dragListen)
            wheelChatAreaLeft.setOnDragListener(dragListen)


        }
    }

    private fun initObserver() {
        viewModel.wheelChatItem.observe(this, Observer {
            if (it != null) {
                with(viewBinding) {
                    wheelChatTvTop.setText(it.top)
                    wheelChatTvRight.setText(it.right)
                    wheelChatTvBottom.setText(it.bottom)
                    wheelChatTvLeft.setText(it.left)
                }
            }
        })
    }

    private class MyDragShadowBuilder(v: ImageView) : View.DragShadowBuilder(v) {

        private val shadow = v

        override fun onProvideShadowMetrics(size: Point, touch: Point) {
            val width: Int = view.width

            val height: Int = view.height


            size.set(width, height)

            touch.set(width / 2, height / 2)

            Log.i("DragMove", "X:${touch.x} Y:${touch.y}")

        }


        override fun onDrawShadow(canvas: Canvas) {
            shadow.draw(canvas)
        }


    }

    private val dragListen = View.OnDragListener { v, event ->

        when (event.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                if (event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {

                    v.invalidate()

                    true
                } else {
                    false
                }

            }
            DragEvent.ACTION_DRAG_ENTERED -> {
                viewBinding.apply {
                    when (v.id) {
                        R.id.wheel_chat_area_top -> {
                            onEnterArea(wheelChatTvTop, wheelChatSelectorTop)
                        }
                        R.id.wheel_chat_area_bottom -> {
                            onEnterArea(wheelChatTvBottom, wheelChatSelectorBottom)
                        }
                        R.id.wheel_chat_area_left -> {
                            onEnterArea(wheelChatTvLeft, wheelChatSelectorLeft)
                        }
                        R.id.wheel_chat_area_right -> {
                            onEnterArea(wheelChatTvRight, wheelChatSelectorRight)
                        }
                    }
                }


                v.invalidate()

                true
            }
            DragEvent.ACTION_DRAG_LOCATION -> {
                Log.i("DragMove", "X:${event.x} Y:${event.y}")
                true
            }
            DragEvent.ACTION_DRAG_EXITED -> {

                viewBinding.apply {
                    when (v.id) {
                        R.id.wheel_chat_area_top -> {
                            onExitArea(wheelChatTvTop, wheelChatSelectorTop)
                        }
                        R.id.wheel_chat_area_bottom -> {
                            onExitArea(wheelChatTvBottom, wheelChatSelectorBottom)
                        }
                        R.id.wheel_chat_area_left -> {
                            onExitArea(wheelChatTvLeft, wheelChatSelectorLeft)
                        }
                        R.id.wheel_chat_area_right -> {
                            onExitArea(wheelChatTvRight, wheelChatSelectorRight)
                        }
                    }
                }

                v.invalidate()

                true
            }
            DragEvent.ACTION_DROP -> {
                val TAG = "DragMove"
                viewBinding.apply {
                    when (v.id) {
                        R.id.wheel_chat_area_top -> {
                            viewModel.performSendMessage(
                                currentUser!!.uid,
                                user!!.uid,
                                wheelChatTvTop.text.toString()
                            )
                            Log.i("DragMove", "DropTop")
                        }
                        R.id.wheel_chat_area_right -> {
                            viewModel.performSendMessage(
                                currentUser!!.uid,
                                user!!.uid,
                                wheelChatTvRight.text.toString()
                            )
                            Log.i("DragMove", "DropRight")
                        }
                        R.id.wheel_chat_area_bottom -> {
                            viewModel.performSendMessage(
                                currentUser!!.uid,
                                user!!.uid,
                                wheelChatTvBottom.text.toString()
                            )
                        }
                        R.id.wheel_chat_area_left -> {
                            viewModel.performSendMessage(
                                currentUser!!.uid,
                                user!!.uid,
                                wheelChatTvLeft.text.toString()
                            )

                        }
                    }
                }

                this.dismiss()




                (v as? ImageView)?.clearColorFilter()

                v.invalidate()

                true
            }
            DragEvent.ACTION_DRAG_ENDED -> {

                viewBinding.wheelBall.visibility = View.VISIBLE

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

    private fun onEnterArea(tv: TextView, v: ImageView) {
        tv.alpha = 1f
        tv.setTextColor(Color.BLACK)
        v.alpha = 1f
    }

    private fun onExitArea(tv: TextView, v: ImageView) {
        tv.alpha = 0.5f
        tv.setTextColor(Color.GRAY)
        v.alpha = 0f
    }


}