package com.dch.test.db

import com.dch.test.base.BaseApplication
import com.dch.test.entity.MyFavorite

/**
 * 作者：MrCoder on 2017/5/24 16:44
 * 描述：
 * 邮箱：daichuanhao@caijinquan.com
 */
object MyFavoriteLocalDataSource {


        fun insertMyFavorite(myFavorite: MyFavorite,callBack: CallBack) {
            try {
                BaseApplication.application.daoSession.myFavoriteDao.insert(myFavorite)
                callBack.insertSuccess()
            } catch(e: Exception) {
                callBack.insertError(e)
            }
        }


    interface CallBack{
        fun insertSuccess()
        fun insertError(e: Exception)
    }

}