package com.dch.test.ui

import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.dch.test.R
import com.dch.test.base.BaseActivity
import com.dch.test.util.StatusBarUtils
import kotlinx.android.synthetic.main.activity_kotlin_photo_view.*
import org.jetbrains.anko.act
import org.jetbrains.anko.matchParent
import org.jetbrains.anko.onClick
import org.jetbrains.anko.wrapContent

/**
 * 作者：Dch on 2017/5/23 11:09
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
class KotlinPhotoViewActivity : BaseActivity() {
    override fun initData() {
        val aaaa : Any ? =null
    }

    override fun initView() {
        StatusBarUtils.setImage(act)
        val layoutParams = ViewPager.LayoutParams()
        layoutParams.gravity = Gravity.CENTER
        layoutParams.width = matchParent
        layoutParams.height = wrapContent

        val imglist = intent.getStringArrayListExtra("imglist")
        val pos = intent.getIntExtra("pos",1)
        val imageViewList = ArrayList<ImageView>()
        imglist.forEach { imageViewList.add(ImageView(act)) }
        viewpager_photoview.adapter = PhotoAdapter(imglist,imageViewList,{ pos ->
            showImg(pos,imageViewList[pos], layoutParams, imglist)
        })
        viewpager_photoview.setCurrentItem(pos,false)
    }

    override fun setLayoutId() = R.layout.activity_kotlin_photo_view

    fun showImg(position: Int, imageView: ImageView, layoutParams: ViewPager.LayoutParams, imglist: ArrayList<String>): Unit {
        imageView.layoutParams = layoutParams
        Glide.with(act).load(imglist[position]).asBitmap().into(imageView)
        imageView.onClick { act.finish() }
    }

    class PhotoAdapter(val imglist: ArrayList<String>, val imageViewlist: ArrayList<ImageView>, val showImg: (pos:Int) -> Unit) : PagerAdapter() {

        override fun instantiateItem(container: ViewGroup?, position: Int): Any {
            container?.addView(imageViewlist[position])
            showImg(position)
            return imageViewlist[position]
        }

        override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
            container?.removeView(imageViewlist[position])
        }

        override fun isViewFromObject(view: View?, `object`: Any?) = true

        override fun getCount() = imglist.size

    }
}
