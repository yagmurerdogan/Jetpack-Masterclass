package com.yagmurerdogan.favdish.model.entities

import android.media.Image
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fav_dishes_table")
data class FavDish(
    @ColumnInfo val image: String,
    @ColumnInfo(name = "image_source") val imageSource: String,
    @ColumnInfo val title: String,
    @ColumnInfo val type: String,
    @ColumnInfo val category: String,
    @ColumnInfo val ingredients: String,
    @ColumnInfo(name = "cooking_time") val cookingTime: String,
    @ColumnInfo(name = "instructions") val directionToCook: String,
    @ColumnInfo(name = "favorite_dish") var favoriteDish : Boolean = false,
    @PrimaryKey(autoGenerate = true) val id: Int = 0

)