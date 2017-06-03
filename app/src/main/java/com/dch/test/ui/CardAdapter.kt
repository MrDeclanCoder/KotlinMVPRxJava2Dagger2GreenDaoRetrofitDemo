package com.dch.test.ui

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.dch.test.R
import kotlinx.android.synthetic.main.card_item_photoview.view.*

/**
 * 作者：MrCoder on 2017/6/2 17:47
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
public class CardAdapter(var items: List<String>,val showImageView:(ImageView, String)->Unit): RecyclerView.Adapter<CardAdapter.CardHolder>(){
    override fun onBindViewHolder(holder: CardHolder?, position: Int) {
        holder?.bindView(items[position])
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CardHolder {
        var view = View.inflate(parent?.context, R.layout.card_item_photoview, null)
        return CardHolder(view,showImageView)
    }


    class CardHolder(itemView: View, val showView: (ImageView,String) -> Unit):RecyclerView.ViewHolder(itemView){
        fun bindView(url: String){
            with(url){
                showView(itemView.imageView_photoview,url)
            }
        }
    }
}