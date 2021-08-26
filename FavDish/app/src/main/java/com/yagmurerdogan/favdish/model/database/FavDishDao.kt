package com.yagmurerdogan.favdish.model.database

import androidx.room.*
import com.yagmurerdogan.favdish.model.entities.FavDish
import kotlinx.coroutines.flow.Flow

@Dao
interface FavDishDao {

    @Insert
    suspend fun insertFavDishDetails(favDish: FavDish)

    @Query("SELECT * FROM FAV_DISHES_TABLE ORDER BY ID")
    fun getAllDishList(): Flow<List<FavDish>>

    @Update
    suspend fun updateFavDishDetails(favDish: FavDish)

    @Query("SELECT * FROM FAV_DISHES_TABLE WHERE favorite_dish = 1")
    fun getFavoriteDishesList(): Flow<List<FavDish>>

    @Delete
    suspend fun deleteFavDishDetails(favDish:FavDish)

    @Query("SELECT * FROM FAV_DISHES_TABLE WHERE type = :filterType")
    fun getFilteredDishesList(filterType: String) : Flow<List<FavDish>>
}