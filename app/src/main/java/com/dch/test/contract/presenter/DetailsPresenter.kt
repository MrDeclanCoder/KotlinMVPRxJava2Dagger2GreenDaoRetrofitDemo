package com.dch.test.contract.presenter

import com.dch.test.contract.DetailsContract
import com.dch.test.db.MyFavoriteLocalDataSource
import com.dch.test.entity.MyFavorite

/**
 * 作者：MrCoder on 2017/5/24 16:12
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
open class DetailsPresenter(detailsView: DetailsContract.DetailsView, val myFavoriteLocalDataSource: MyFavoriteLocalDataSource) : DetailsContract.Presenter {

    val detailsView = detailsView

    override fun insertMyFavoriteToDb(myFavorite: MyFavorite) {
        myFavoriteLocalDataSource.insertMyFavorite(myFavorite, cb())
    }

    open class cb : MyFavoriteLocalDataSource.CallBack {
        override fun insertSuccess() {

        }

        override fun insertError(e: Exception) {

        }

    }


    override fun start() {}

}