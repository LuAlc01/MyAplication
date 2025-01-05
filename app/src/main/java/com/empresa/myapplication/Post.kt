package com.empresa.myapplication

//IMport entidad
import androidx.room.Entity
import androidx.room.PrimaryKey

//import DAO, los querys
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Insert
import androidx.room.Query

//Import base datos general Rooms

import androidx.room.*

import androidx.room.RoomDatabase




@Entity(tableName = "post")
data class Post(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titulo: String,
    val contenido: String
)

@Dao
interface PostDao{

    @Insert
    suspend fun insertarPost(post: Post)

    @Query("SELECT * FROM post")
    suspend fun obtenerTodosLosPosts(): List<Post>
}



