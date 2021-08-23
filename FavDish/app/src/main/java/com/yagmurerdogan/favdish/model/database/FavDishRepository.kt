package com.yagmurerdogan.favdish.model.database

import androidx.annotation.WorkerThread
import com.yagmurerdogan.favdish.model.entities.FavDish

class FavDishRepository(private val favDishDao: FavDishDao) {

    @WorkerThread
    suspend fun insertFavDishData(favDish: FavDish) {
        favDishDao.insertFavDishDetails(favDish)
    }
}