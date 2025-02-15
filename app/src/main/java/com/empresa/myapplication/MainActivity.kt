package com.empresa.myapplication

// Imports del estado de Activity, los LOGS, aquí también va el Bundle y ComponentActivity

// Permisos

// Cuadro permisos

// Imports Base de Datos

// Crear entidad

// DAO, para hacer las queries.
import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
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
import androidx.compose.foundation.lazy.items
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.empresa.myapplication.PostViewModel.ChuckNorrisViewModel
import com.empresa.myapplication.PostViewModel.PostViewModel
import com.empresa.myapplication.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay


// Data class para la barra de navegación
data class BarraNavegacion(
    val titulo: String,
    val iconoSeleccionado: ImageVector,
    val iconoNoSeleccionado: ImageVector,
    val notis: Boolean, // Si es true, quiero que salga una bolita
    val contadorBadges: Int? = null // El ? hace que una variable pueda ser null // Corresponde al número de mensajes/notis
)

@AndroidEntryPoint // Anotación para habilitar Hilt en la actividad
class MainActivity : ComponentActivity() { // Línea 307
    private val TAG = "ActivityLifeCycle"  // TAG asociado a la actividad creada para no escribir siempre lo mismo.
    private val REQUEST_STORAGE_PERMISSION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) // Empieza
        Log.d(TAG, "onCreate: Activity creada")
        Thread.sleep(2000)
        installSplashScreen()
        enableEdgeToEdge()

        // Quiero que revise los permisos mientras "Crea" el proceso de la APP
        if (!TienepermisoAlmacen()) {
            solicitarpermisoAlmacen()
        }

        setContent { // Acaba, hasta aquí es la declaración de nuestra actividad, y declaramos el entorno en el que se creará lo que haya en el Compose de abajo
            MyApplicationTheme {
                var mostrarPrincipio by rememberSaveable { mutableStateOf(true) }

                // Transición
                LaunchedEffect(Unit) {
                    delay(3000)
                    mostrarPrincipio = false
                }

                if (mostrarPrincipio) {
                    Principio()
                } else {
                    MenuOpciones() // Ya no pasamos postDatabase, Hilt lo maneja
                }
            }
        }
    }

    // Tema LOGS
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

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop: Activity ya no es visible")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: Activity destruida, recursos liberados")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart: Activity reiniciada")
    }

    // Este extra es para la memoria
    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        Log.d(TAG, "onTrimMemory: Nivel de memoria bajo: $level")
    }

    // funcion que acompaña a los declarados en AndroidManifest que hace que la propia actividad maneje esos datos, que luego personalizo logs en este método
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d(TAG, "Pantalla en modo horizontal")
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.d(TAG, "Pantalla en modo vertical")
        }
    }

    // Apartado permisos verificable en LOGCAT con ActivityLifeCycle
    private fun TienepermisoAlmacen(): Boolean {
        // Para Android 10 y superiores, no necesito permisos de escritura.
        val permisoConcedido = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            true // Scoped Storage no requiere WRITE_EXTERNAL_STORAGE
        } else {
            // la funcion sí que aplica a todas las versiones, esto complementa la modificación del AndroidManifest.
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

}// Fin clase MainActivity

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

// Mi pantalla de carga
@Composable
fun Principio() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.primary
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Imagen a presentar",
                modifier = Modifier.size(100.dp)
            ) // Cierre del Image
            Spacer(modifier = Modifier.height(16.dp)) // Espaciador
            Text(
                text = "Assistance cloud",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 24.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MenuOpciones() {
    val viewModel: PostViewModel = hiltViewModel()
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
                1 -> PostsLista(viewModel)  // Pasa la base de datos a la pantalla de posts
                2 -> PantallaAjustes()
                else -> throw IllegalStateException("Índice inesperado: $selectedItemIndex")
            }
        }
    }
}

@Composable
fun PantallaPosts() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Posts", style = MaterialTheme.typography.titleLarge)
    }
}

@Composable
fun PantallaAjustes() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
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



//@Composable
//fun PantallaPrincipal(viewModel: ChuckNorrisViewModel = hiltViewModel()) {
//    // Observamos el StateFlow del chiste
//    val joke by viewModel.joke.collectAsState()
//    // Observamos el StateFlow de errores
//    val error by viewModel.error.collectAsState()
//
//    // Cargamos un chiste aleatorio cuando el Composable se inicia
//    LaunchedEffect(Unit) {
//        viewModel.loadRandomJoke()
//    }
//
//    Box(
//        modifier = Modifier.fillMaxSize(),
//        contentAlignment = Alignment.Center
//    ) {
//        if (true) {
//            // Si hay un error, lo mostramos
//            Text(
//                text = error,
//                style = MaterialTheme.typography.bodyLarge,
//                color = MaterialTheme.colorScheme.error
//            )
//        } else if (true) {
//            // Si hay un chiste, lo mostramos
//            Text(
//                text = joke,
//                style = MaterialTheme.typography.bodyLarge,
//                textAlign = TextAlign.Center,
//                modifier = Modifier.padding(16.dp)
//            )
//        } else {
//            // Si no hay chiste, mostramos un mensaje de carga
//            Text("Cargando chiste...", style = MaterialTheme.typography.bodyLarge)
//        }
//    }
//}

@Composable
fun PostsLista(viewModel: PostViewModel = hiltViewModel()) {
    // Observamos el StateFlow de posts
    val posts by viewModel.posts.collectAsState()
    // Observamos el StateFlow de errores
    val error by viewModel.error.collectAsState()

    // Cargamos los posts cuando el Composable se inicia
    LaunchedEffect(Unit) {
        viewModel.loadPosts()
    }

    // Mostramos la UI según el estado
    if (error != null) {
        // Si hay un error, lo mostramos
        Text(
            text = error!!,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error
        )
    } else if (posts.isEmpty()) {
        // Si no hay posts, mostramos un mensaje
        Text("No hay posts disponibles", style = MaterialTheme.typography.bodyLarge)
    } else {
        // Si hay posts, los mostramos en una LazyColumn
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(posts) { post ->
                Text(
                    text = post.titulo,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}

//comentar

//@Composable
//fun PostsLista(postDatabase: PostDatabase) {
//    // Se utiliza un estado local para manejar la lista de posts, el estado de carga y si ocurrió un error.
//    var posts by remember { mutableStateOf<List<Post>>(emptyList()) }
//    var loading by remember { mutableStateOf(true) }
//    var error by remember { mutableStateOf(false) }
//
//    // Recuperación de datos desde la base de datos en un hilo separado.
//    LaunchedEffect(Unit) {
//        try {
//            // Usamos withContext para cambiar a un hilo de fondo (IO) para no bloquear el hilo principal.
//            withContext(Dispatchers.IO) {
//                val postDao = postDatabase.postDao() // Se obtiene el DAO de la base de datos.
//                // Recolectamos el Flow y lo asignamos a `posts`.
//                postDao.obtenerTodosLosPosts().collect { postList ->
//                    posts = postList
//                    loading =
//                        false // Una vez que los posts son cargados, se cambia el estado de carga.
//                }
//            }
//        } catch (e: Exception) {
//            // Si ocurre un error al cargar los posts, se maneja el error y se marca el estado de error como true.
//            error = true
//            loading = false // Se indica que la carga ha terminado, aunque haya ocurrido un error.
//            Log.e("PostsLista", "Error al cargar los posts", e) // Se loguea el error.
//        }
//    }
//
//    // Dependiendo del estado de carga y error, mostramos diferentes mensajes o la lista de posts.
//    if (loading) {
//        // Muestra un texto indicando que los posts se están cargando.
//        Text("Cargando...", style = MaterialTheme.typography.bodyLarge)
//    } else if (error) {
//        // Si ocurrió un error al cargar los posts, se muestra un mensaje de error.
//        Text(
//            "Error al cargar los posts",
//            style = MaterialTheme.typography.bodyLarge,
//            color = MaterialTheme.colorScheme.error
//        )
//    } else {
//        // Si está bien, mostramos la lista de posts en una LazyColumn.
//        LazyColumn(
//            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.spacedBy(8.dp),
//            contentPadding = PaddingValues(16.dp)
//        ) {
//            // Recorremos la lista de posts y mostramos cada uno en la columna.
//            items(posts) { post ->
//                // Muestra el título de cada post.
//                Text(
//                    text = post.titulo, // Título de cada post cargado desde la BD.
//                    style = MaterialTheme.typography.bodyLarge,
//                    modifier = Modifier.padding(8.dp)
//                )
//            }
//        }
//    }
//}





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


