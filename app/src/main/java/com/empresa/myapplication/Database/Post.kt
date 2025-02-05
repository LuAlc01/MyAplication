package com.empresa.myapplication.Database

//IMport entidad
import android.content.Context
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
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


//Esta es la marca la clase como uan entia e la base de datos. crea una tabla por cada clase que tenga @entity notacion
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

//Aqui esta el commit al que me refiero https://github.com/LuAlc01/MyAplication/commit/57794bd23367c490a05950024b099874d7e1185e


//Configuramos un módulo Hilt para proporcionar la instancia de Retrofit.
//Lo hacemos en singleton para dar prioridad y así evitar errores
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): PostDatabase {
        return Room.databaseBuilder(
            context,
            PostDatabase::class.java,
            "post_database"
        ).fallbackToDestructiveMigration()  // Esto puede ser útil en caso de cambios en la versión de la base de datos
            .build()
    }

    @Provides
    @Singleton
    fun providePostDao(database: PostDatabase): PostDao {
        return database.postDao()
    }
}
