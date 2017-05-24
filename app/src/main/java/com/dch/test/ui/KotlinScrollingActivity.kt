package com.dch.test.ui

import android.content.Context
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.dch.test.R
import com.dch.test.base.BaseActivity
import com.dch.test.base.BaseApplication
import com.dch.test.entity.MyFavorite
import kotlinx.android.synthetic.main.activity_kotlin_scrolling.*
import org.jetbrains.anko.find
import org.jetbrains.anko.onClick
import org.jetbrains.anko.onLongClick

class KotlinScrollingActivity : BaseActivity() {
    var dataList: List<MyFavorite>? = null
    override fun initData() {
        val myFavoriteDao = BaseApplication.application.daoSession.myFavoriteDao
        dataList = myFavoriteDao.loadAll()
        recyclerview_kotlin_scroll.layoutManager = LinearLayoutManager(this)
        recyclerview_kotlin_scroll.adapter = CollectAdapter(dataList!!, this)
    }

    override fun initView() {
        setSupportActionBar(toolbar_kotlin_scroll)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        collapsingtoolbarlayout_kotlin_scroll.title = "Kotlin--我的收藏"
        toolbar_kotlin_scroll.setNavigationOnClickListener { _ ->
            onBackPressed()
        }
        fab_kotlin_scroll.setOnClickListener { view ->
            Snackbar.make(view, "fab被点击了----", Snackbar.LENGTH_SHORT).setAction("Action", null).show()
        }
    }

    companion object {
        fun instance() = this
    }

    override fun setLayoutId() = R.layout.activity_kotlin_scrolling
    class CollectAdapter(var items: List<MyFavorite>, var context: Context) : RecyclerView.Adapter<CollectHolder>() {
        override fun onBindViewHolder(holder: CollectHolder, position: Int) {
            val myfavorite = items.get(position)
            holder.tv_item_title_gank!!.text = "android"
            holder.tv_item_content_gank!!.text = myfavorite.contentDiscription
            holder.tv_item_time_gank!!.text = myfavorite.collectTime
            Glide.with(context).load(myfavorite.imgUrl).into(holder.iv_item_gank)
            val relativeLayout = holder.tv_item_time_gank!!.parent as RelativeLayout
            relativeLayout.onClick { _ ->

            }
            relativeLayout.onLongClick { _ -> true
            }

        }

        override fun getItemCount(): Int = items.size

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) =
                CollectHolder(View.inflate(parent?.context, R.layout.item_gank, null))
    }

    class CollectHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_item_content_gank: TextView? = null
        var tv_item_title_gank: TextView? = null
        var tv_item_time_gank: TextView? = null
        var iv_item_gank: ImageView? = null

        init {
            super.itemView
            tv_item_content_gank = itemView.find<TextView>(R.id.tv_item_content_gank)
            tv_item_title_gank = itemView.find<TextView>(R.id.tv_item_title_gank)
            tv_item_time_gank = itemView.find<TextView>(R.id.tv_item_time_gank)
            iv_item_gank = itemView.find<ImageView>(R.id.iv_item_gank)
        }
    }
}
