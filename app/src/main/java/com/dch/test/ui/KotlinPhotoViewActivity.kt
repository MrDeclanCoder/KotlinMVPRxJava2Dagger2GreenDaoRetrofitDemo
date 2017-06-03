package com.dch.test.ui

import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.dch.test.R
import com.dch.test.base.BaseActivity
import com.dch.test.util.StatusBarUtils
import com.view.jameson.library.CardScaleHelper
import jp.wasabeef.glide.transformations.BlurTransformation
import kotlinx.android.synthetic.main.activity_kotlin_photo_view.*
import org.jetbrains.anko.act
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.wrapContent
import java.util.*

/**
 * 作者：Dch on 2017/5/23 11:09
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
class KotlinPhotoViewActivity : BaseActivity() {
    val mCardScaleHelper = CardScaleHelper()

    override fun initData() {
        val aaaa: Any? = null
    }

    override fun initView() {
        StatusBarUtils.setImage(act)
        val layoutParams = ViewPager.LayoutParams()
        layoutParams.gravity = Gravity.CENTER
        layoutParams.width = matchParent
        layoutParams.height = wrapContent

        val imglist = intent.getStringArrayListExtra("imglist")
        val pos = intent.getIntExtra("pos", 1)

        val linearLayoutManager = LinearLayoutManager(act, LinearLayoutManager.HORIZONTAL, false)
        recyclerView_photo.layoutManager = linearLayoutManager
        recyclerView_photo.adapter = CardAdapter(imglist) { imageView: ImageView, url: String -> showImage(imageView, url) }
        //recyclerview绑定scale效果
        mCardScaleHelper.currentItemPos = pos
        mCardScaleHelper.attachToRecyclerView(recyclerView_photo)

        initBlurBackground(imglist)
    }

    fun showImage(imageView: ImageView, url: String) {
        Glide.with(act).load(url).centerCrop().into(imageView)
    }

    override fun setLayoutId() = R.layout.activity_kotlin_photo_view

    fun initBlurBackground(imglist: ArrayList<String>) {
        recyclerView_photo.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    notifyBackgroundChange(imglist)
                }
            }
        })
        notifyBackgroundChange(imglist)
    }

    fun notifyBackgroundChange(imglist: ArrayList<String>) {
//        if (lastPosition == mCardScaleHelper.currentItemPos) return
//        val lastPosition = mCardScaleHelper.currentItemPos
        val url = imglist[mCardScaleHelper.currentItemPos]
        Glide.with(act).load(url).crossFade(500).bitmapTransform(BlurTransformation(act,23,4)).into(blurView_photo)
//        val blurRunnabl = Runnable {
//
//        }
//        blurView_photo.postDelayed(blurRunnabl,300)

    }

}
