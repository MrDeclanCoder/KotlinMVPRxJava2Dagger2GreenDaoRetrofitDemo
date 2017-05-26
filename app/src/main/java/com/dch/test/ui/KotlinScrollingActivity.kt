package com.dch.test.ui

import android.content.DialogInterface
import android.support.design.widget.Snackbar
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.dch.test.R
import com.dch.test.base.BaseActivity
import com.dch.test.base.BaseApplication
import com.dch.test.entity.MyFavorite
import com.dch.test.util.StatusBarUtils
import greendao.gen.MyFavoriteDao
import kotlinx.android.synthetic.main.activity_kotlin_scrolling.*
import kotlinx.android.synthetic.main.item_gank.view.*
import org.jetbrains.anko.*

/**
 * 作者：Dch on 2017/5/23 11:09
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
class KotlinScrollingActivity : BaseActivity() {
    var adapter: CollectAdapter? = null

    override fun initView() {
        StatusBarUtils.setImage(act)
        setSupportActionBar(toolbar_kotlin_scroll)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        collapsingtoolbarlayout_kotlin_scroll.title = "Kotlin--我的收藏"
        collapsingtoolbarlayout_kotlin_scroll.setExpandedTitleColor(resources.getColor(R.color.trans))
        collapsingtoolbarlayout_kotlin_scroll.setCollapsedTitleTextColor(android.R.color.white)
        toolbar_kotlin_scroll.setNavigationOnClickListener { _ ->
            onBackPressed()
        }
        fab_kotlin_scroll.setOnClickListener { view ->
            Snackbar.make(view, "I'm writing with the Kotlin language", Snackbar.LENGTH_SHORT).setAction("Action", null).show()
        }
        alert { }
    }

    override fun setLayoutId() = R.layout.activity_kotlin_scrolling

    override fun initData() {
        val myFavoriteDao = BaseApplication.application.daoSession.myFavoriteDao
        val dataList = myFavoriteDao.loadAll()
        recyclerview_kotlin_scroll.layoutManager = LinearLayoutManager(act)

        adapter = CollectAdapter(dataList) { view: View, myfavorite: MyFavorite, pos: Int ->
            Glide.with(act).load(myfavorite.imgUrl).into(view.iv_item_gank)
            view.onClick {
                alert("注意", "跳转了")
                {
                    yesButton { dialog: DialogInterface ->
                        toast("跳转")
                        dialog.dismiss()
                    }
                    noButton { dialog: DialogInterface -> dialog.dismiss() }
                }.show()
            }
            view.onLongClick {
                onLongClick(myfavorite, myFavoriteDao,adapter,dataList,pos)
            }
        }

        recyclerview_kotlin_scroll.adapter = adapter
    }

    fun onLongClick(myfavorite: MyFavorite, myFavoriteDao: MyFavoriteDao, adapter: CollectAdapter?, dataList: MutableList<MyFavorite>, pos: Int): Boolean {
        alert("操作", "从收藏中移除？") {
            yesButton { dialog ->
                try {
                    myFavoriteDao.delete(myfavorite)
                    dataList.removeAt(pos)
                    adapter?.notifyItemRemoved(pos)
                } catch(e: Exception) {
                    alert("警告", "移除失败")
                } finally {
                    dialog.dismiss()
                }
            }
            noButton { dialog ->
                dialog.dismiss()
            }
        }.show()
        return true
    }

    class CollectAdapter(var items: List<MyFavorite>, val init: (View, MyFavorite,Int) -> Unit) : RecyclerView.Adapter<CollectAdapter.CollectHolder>() {
        override fun onBindViewHolder(holder: CollectHolder, position: Int) {
            holder.bindFavorite(items[position],position)
        }

        override fun getItemCount(): Int = items.size

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CollectHolder {
            val view = View.inflate(parent?.context, R.layout.item_gank, null)
            return CollectHolder(view, init)
        }

        class CollectHolder(itemView: View, val init: (View, MyFavorite,Int) -> Unit) : RecyclerView.ViewHolder(itemView) {

            fun bindFavorite(myfavorite: MyFavorite, position: Int) {
                with(myfavorite) {
                    itemView.tv_item_title_gank.text = "Android"
                    itemView.tv_item_content_gank.text = myfavorite.contentDiscription
                    itemView.tv_item_time_gank.text = myfavorite.collectTime
                    init(itemView, myfavorite,position)
                }
            }
        }
    }


}
