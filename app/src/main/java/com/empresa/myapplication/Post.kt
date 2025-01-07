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



//Esta es la marca la clase como uan entia e la base de datos.
@Entity(tableName = "post")
data class Post(
    //Es la clave priparia , al estar en autogenrate, creará un id automaticamente y único para cada post.
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titulo: String,
    val contenido: String
)


//Es la interfaz donde defino los métodos para interactuar en la base de datos, en este caso, En los post, quiero insertar(crear nuevos) , borrar y actualizar los post
@Dao
interface PostDao{

    @Insert
    //suspend indica que es una operación asincrónica, es decir, que no bloqueará el hilo principal,es decir la UI
    suspend fun insertarPost(post: Post)


    @Query("SELECT * FROM post")
    //Como su nombre indica, es la función para hacer consultas en la base de datos, aquí
    suspend fun obtenerTodosLosPosts(): List<Post>

    @Update
    suspend fun actualzarPost(post: Post)

    @Delete
    suspend fun eliminarPost(post: Post)

}



//Database es la notación que se referiere a la base de datos. La base de datos, tiene una entidad llamda Post y la v inicial es 1.
//A cada cambio, hay que incromentar.

//abstract fun postDao(): devuleve una instancia del DAO PostDao, que contiene las funciones necesarias para interactuar.

@Database(entities = [Post::class], version = 1)
abstract class PostDatabase : RoomDatabase(){
    abstract fun postDao(): PostDao
}


