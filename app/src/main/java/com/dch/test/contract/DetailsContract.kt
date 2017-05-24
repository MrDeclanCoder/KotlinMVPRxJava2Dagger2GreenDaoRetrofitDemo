package com.dch.test.contract

import com.dch.test.base.BasePresenter
import com.dch.test.base.BaseView
import com.dch.test.entity.MyFavorite

/**
 * 作者：MrCoder on 2017/5/24 16:05
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
interface DetailsContract {

    interface DetailsView: BaseView<Presenter>{
        fun insertSuccess()
        fun insertError(exception: Exception)
    }

    interface Presenter: BasePresenter{

        fun insertMyFavoriteToDb(myFavorite: MyFavorite)

    }
}