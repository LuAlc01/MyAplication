package com.empresa.myapplication

import android.R.attr.contentDescription
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.empresa.myapplication.ui.theme.MyApplicationTheme
import kotlinx.coroutines.delay
import org.w3c.dom.Text
import kotlin.concurrent.thread
import kotlin.collections.List
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp

//Imports del estado de Activity, los LOGS, aqui tambien va el Bundle y componentActivity
import android.util.Log
import androidx.core.content.ContextCompat

//permisos
import android.Manifest
import android.app.Activity

//Cuadro permisos
import android.os.Build //veridicador de versiones android
import android.widget.Toast
import androidx.compose.runtime.remember
import androidx.core.app.ActivityCompat //Este comprueba y verifica permisos.




//imports Base de Datos

// Crear entidad
import androidx.room.Entity
import androidx.room.PrimaryKey

//DAO, para hacer las querys.
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Room
import com.empresa.myapplication.Database.Post
import com.empresa.myapplication.Database.PostDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


data class BarraNavegacion(
    val titulo: String,
    val iconoSeleccionado: ImageVector,
    val iconoNoSeleccionado: ImageVector,
    val notis: Boolean, //Si es true quiero que salga una bolita
    val contadorBadges: Int? = null //el ? hace que una variable pueda ser null //corresponde al numero de mensajes/notis
)
//Añadire mas iconos con una dependencia que hay que buscar (aún no implementada)
class MainActivity : ComponentActivity() { //linea 307
    private lateinit var postDatabase: PostDatabase //AQUI DATABASE declarada

    private val TAG = "ActivityLifeCycle"  //TAG Asociado a la actividad creada para no escribir sempre lo mismo.

    private  val REQUEST_STORAGE_PERMISSION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)//Empieza
        Log.d(TAG, "onCreate: Activity creada")
        Thread.sleep(2000)
        installSplashScreen()
        enableEdgeToEdge()

        //inicializar la base de datos
        try {
            Log.d(TAG, "Inicializando la base de datos...")
            postDatabase = Room.databaseBuilder(
                applicationContext,
                PostDatabase::class.java,
                "post-database"
            ).fallbackToDestructiveMigration().build()
            Log.d(TAG, "Base de datos inicializada correctamente")
        } catch (e: Exception) {
            Log.e(TAG, "Error al inicializar la base de datos: ${e.message}")
            e.printStackTrace()
        }

        // ACCESO A BASE DE DATOS EN LA COMPOSABLE 'PostsLista'

        //Quiero que revise los permisos mientras "Crea" el proceso de la APP
        if (!TienepermisoAlmacen()){
            solicitarpermisoAlmacen()
        }

        setContent { //Acaba,   Hasta aquí es la declaracion de nuestra actividad, y declaramos el Entorno en el que se creara lo que haya en el Compose de abajo
            MyApplicationTheme {
                var mostrarPrincipio by rememberSaveable { mutableStateOf(true) }


                //Transicion
                LaunchedEffect(Unit) {
                    delay(3000)
                    mostrarPrincipio = false
                }

                if (mostrarPrincipio){
                    Principio()
                }
                else{
                    MenuOpciones(postDatabase)
                }
            }
        }
    }


    //tema LOGS
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: Activity visible")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: Usuario puede interactuar con la Activity")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause: Activity parcialmente visible")
    }

    override fun onStop(){
        super.onStop()
        Log.d(TAG, "onStop: Activity ya no es visible")
    }

    override fun onDestroy(){
        super.onDestroy()
        Log.d(TAG, "onDestroy: Activity destruida, recursos liberados")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart: Activity reiniciada")
    }

    //Este extra es para la memoria

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        Log.d(TAG, "onTrimMemory: Nivel de memoria bajo: $level")
    }

    //Metodo que acompaña a los declarado en AndroidManifest que hace que la propia actividad, maneje esos datos, que luego personalizo logs en este metodo
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            Log.d(TAG, "Pantalla en modo horizontal")
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
           Log.d(TAG, "Pantalla en modo vertical")
        }
    }


    //Apartado permisos verificable en LOGCAT con ActivityLifeCycle
    private fun TienepermisoAlmacen(): Boolean{
        //Para android 10 y superiors, no necesito permisos de escritura.
        val permisoConcedido = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
            true // Scoped Storage no requiere WRITE_EXTERNAL_STORAGE
        } else {
            //El metodo si que aplica a todas las verisones, esto complementa la modificacion dl android manifest.
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
        Log.d(TAG, "TienepermisoAlmacen: Permiso concedido = $permisoConcedido")
        return permisoConcedido
    }


    private fun solicitarpermisoAlmacen() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this, Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                Toast.makeText(
                    this,
                    "El permiso es necesario para acceder a archivos públicos.",
                    Toast.LENGTH_LONG
                ).show()
            }

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_STORAGE_PERMISSION
            )
        } else {
            Toast.makeText(
                this,
                "Scoped Storage está habilitado, no se necesitan permisos adicionales.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    //MOdiifcacion metodo de cuadricula.
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String?>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId) //Hasta aqui generado auto
        if (requestCode == REQUEST_STORAGE_PERMISSION){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permiso otorgado, Gracias!", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this,"Permiso denegado, No aprovecharas todas las funciones", Toast.LENGTH_SHORT).show()
            }
        }
    }


} // Fin clase MainActivity




@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}

//Mi pantalla de carga
@Composable
fun Principio(){
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.primary
    ){
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription ="Imagen a presentar",
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(16.dp)) //import dp
            Text(
                text = "Assistance cloud",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 24.sp, //import sp
                textAlign = TextAlign.Center
            )
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MenuOpciones(postDatabase: PostDatabase) {
    val items = listOf(
        BarraNavegacion(
            titulo = "Home",
            iconoSeleccionado = Icons.Filled.Home,
            iconoNoSeleccionado = Icons.Outlined.Home,
            notis = false
        ),
        BarraNavegacion(
            titulo = "Posts",
            iconoSeleccionado = Icons.Filled.Info,
            iconoNoSeleccionado = Icons.Outlined.Info,
            notis = false,
            contadorBadges = 45
        ),
        BarraNavegacion(
            titulo = "Ajustes",
            iconoSeleccionado = Icons.Filled.Settings,
            iconoNoSeleccionado = Icons.Outlined.Settings,
            notis = true
        )
    )

    var selectedItemIndex by rememberSaveable { mutableStateOf(0) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            bottomBar = {
                NavigationBar {
                    items.forEachIndexed { index, item ->
                        NavigationBarItem(
                            selected = selectedItemIndex == index,
                            onClick = {
                                selectedItemIndex = index
                            },
                            label = {
                                Text(text = item.titulo)
                            },
                            alwaysShowLabel = false,
                            icon = {
                                BadgedBox(
                                    badge = {
                                        if (item.contadorBadges != null) {
                                            Badge {
                                                Text(text = item.contadorBadges.toString())
                                            }
                                        } else if (item.notis) {
                                            Badge()
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (index == selectedItemIndex) {
                                            item.iconoSeleccionado
                                        } else item.iconoNoSeleccionado,
                                        contentDescription = item.titulo
                                    )
                                }
                            }
                        )
                    }
                }
            }
        ) {
            when (selectedItemIndex) {
                0 -> PantallaPrincipal()
                1 -> PostsLista(postDatabase)  // Pasa la base de datos a la pantalla de posts
                2 -> PantallaAjustes()
                else -> throw IllegalStateException("Índice inesperado: $selectedItemIndex")
            }
        }
    }
}

@Composable
fun PantallaPosts(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(text = "Posts", style = MaterialTheme.typography.titleLarge)
    }
}
@Composable
fun PantallaAjustes(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(text = "Ajustes", style = MaterialTheme.typography.titleLarge)
    }
}
@Composable
fun PantallaPrincipal(){
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Text(text = "¡HOLAAA!", style = MaterialTheme.typography.titleLarge)
    }
}


@Composable
fun PostsLista(postDatabase: PostDatabase) {
    var posts by remember { mutableStateOf<List<Post>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }

    // Recuperación de datos desde la base de datos
    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            val postDao = postDatabase.postDao()// Obteniendo el DAO
            posts = postDao.obtenerTodosLosPosts() // Cargando todos los posts
            loading = false
        }
    }

    if (loading) {
        Text("Cargando...")
    } else {
        // Visualización de la lista de posts
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(posts) { post ->
                Text(
                    text = post.titulo, // Titulo de cada post cargado desde la BD
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}




@Preview(showBackground = true)
@Composable
fun PreviewPantalla(){
    MyApplicationTheme {
        Principio()
    }
}












/*

MyApplicationTheme {
                Scaffold( modifier = Modifier.fillMaxSize() ) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
 */


/*Actividad:
Es una contenedor de una o más pantallas, mejor dicho, una Unidad de la app donde los usuarios interactúan
y es por eso que se llaman actividades, que tendrán información:
 * Si está en segundo plano
 * Punto de entrada, por ejemplo, si el usuario entra a al APP desde el navegador,  Es decir, una Activity es un componente que inicia la app directamente
...

las d en los logs

Niveles de prioridad en Log

Android proporciona los siguientes niveles de prioridad para clasificar los mensajes de log:

    Log.v (Verbose):
        Prioridad más baja.
        Se utiliza para mensajes muy detallados que podrían ser excesivos en la mayoría de las circunstancias.
        Ejemplo: Log.v(TAG, "Mensaje muy detallado").

    Log.d (Debug):
        Se utiliza para mensajes de depuración.
        Son útiles para entender el flujo de la aplicación y diagnosticar problemas durante el desarrollo.
        Ejemplo: Log.d(TAG, "Variable valorX tiene: $valorX").

    Log.i (Info):
        Nivel informativo.
        Se usa para mensajes generales sobre el estado de la aplicación.
        Ejemplo: Log.i(TAG, "Conexión establecida con éxito").

    Log.w (Warning):
        Indica que algo inesperado ocurrió, pero no es un problema grave.
        Ejemplo: Log.w(TAG, "El servidor tardó demasiado en responder").

    Log.e (Error):
        Prioridad alta.
        Indica que ha ocurrido un error que probablemente necesita atención inmediata.
        Ejemplo: Log.e(TAG, "Error al conectar con la base de datos").

    Log.wtf (What a Terrible Failure):
        Nivel más crítico.
        Se utiliza para situaciones que no deberían ocurrir bajo ninguna circunstancia.
        Ejemplo: Log.wtf(TAG, "¡Estado imposible alcanzado!").


EN LOGCAT, se ve con la palabra clave ActivityLifecycle que es el TAG.
Al rotar la pantalla, se hace un cambio de configuración, por lo que se relanza la actividad, aunque no lo veamos,
Vamos a modificar el manifest de android, para que al rotar, haya un log específico que lo indique, para tener más información y entendible.
Ahora tambien si hay permisos.



 */


