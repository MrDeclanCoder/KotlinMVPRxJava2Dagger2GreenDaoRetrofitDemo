package com.dch.test.ui

import android.view.ViewGroup
import android.widget.ImageView
import com.dch.test.R
import com.dch.test.base.BaseActivity
import com.dch.test.widget.PlaneFloating
import com.ufreedom.floatingview.Floating
import com.ufreedom.floatingview.FloatingBuilder
import kotlinx.android.synthetic.main.activity_kotlin_settings.*
import org.jetbrains.anko.act
import org.jetbrains.anko.onClick

class KotlinSettingsActivity : BaseActivity() {
    override fun initData() {
        bt_plane.onClick { v ->
            val imageView = ImageView(act)
            imageView.layoutParams = ViewGroup.LayoutParams(bt_plane.measuredWidth, bt_plane.measuredHeight)
            imageView.setImageResource(R.drawable.floating_plane)

            val build = FloatingBuilder()
                    .anchorView(v)
                    .targetView(imageView)
                    .floatingTransition(PlaneFloating())
                    .build()
            Floating(this).startFloating(build)
        }

        bt_star.onClick { v ->
            val imageView = ImageView(act)
            imageView.layoutParams = ViewGroup.LayoutParams(bt_plane.measuredWidth, bt_plane.measuredHeight)
            imageView.setImageResource(R.drawable.floating_plane)

            val build = FloatingBuilder()
                    .anchorView(v)
                    .targetView(imageView)
                    .floatingTransition(PlaneFloating())
                    .build()
            Floating(this).startFloating(build)
        }
    }

    override fun initView() {
        circlepb.start()
    }

    override fun setLayoutId() = R.layout.activity_kotlin_settings
}
