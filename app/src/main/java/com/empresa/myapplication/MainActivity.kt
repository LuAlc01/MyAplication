package com.empresa.myapplication

import android.R.attr.contentDescription
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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

data class BarraNavegacion(
    val titulo: String,
    val iconoSeleccionado: ImageVector,
    val iconoNoSeleccionado: ImageVector,
    val notis: Boolean, //Si es true quiero que salga una bolita
    val contadorBadges: Int? = null //el ? hace que una variable pueda ser null //corresponde al numero de mensajes/notis
)
//Añadire mas iconos con una dependencia que hay que buscar (aún no implementada)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(2000)
        installSplashScreen()
        enableEdgeToEdge()
        setContent {
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
                    MenuOpciones()
                }
            }
        }
    }
}

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
                text = "TITILO APP",
                color = MaterialTheme.colorScheme.onPrimary,
                fontSize = 24.sp, //import sp
                textAlign = TextAlign.Center
            )
        }
    }
}





@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MenuOpciones(){
    val items = listOf(
        BarraNavegacion(
            titulo = "Home",
            iconoSeleccionado = Icons.Filled.Home, //La propiedad filled, hace que el icono se "rellene" cuando lo seleccionas
            iconoNoSeleccionado = Icons.Outlined.Home,
            notis = false,
        ),
        BarraNavegacion(
            titulo = "Posts",
            iconoSeleccionado = Icons.Filled.Info,
            iconoNoSeleccionado = Icons.Outlined.Info,
            notis = false,
            contadorBadges = 45  //Esto es un ejemplo de lo que quiero que salga. Gutura implemntacion saber el numero real de notis
        ),
        BarraNavegacion(
            titulo = "Ajustes",
            iconoSeleccionado = Icons.Filled.Settings,
            iconoNoSeleccionado = Icons.Outlined.Settings,
            notis = true, //Aquí creamos el ejemplo de que tengas que hacer algo en la cuenta, actualizar app....
        )
    )
    var selectedItemIndex by rememberSaveable {
        mutableStateOf(0)
    }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ){
        Scaffold (
            bottomBar = {
                NavigationBar {
                    items.forEachIndexed { index, item ->

                        NavigationBarItem(
                            selected = selectedItemIndex == index,
                            onClick = {
                                selectedItemIndex = index
                                //navController.navigate(item.titulo)
                            },
                            label = {
                                Text(text = item.titulo)
                            },
                            alwaysShowLabel = false, //
                            icon = {
                                BadgedBox(
                                    badge = {
                                        if(item.contadorBadges != null) {
                                            Badge {
                                                Text(text = item.contadorBadges.toString())
                                            }
                                        } else if(item.notis) {
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
        ){
            when (selectedItemIndex){
                0 -> PantallaPrincipal()
                1 -> PantallaPosts()
                2 -> PantallaAjustes()
                else -> throw IllegalStateException ("Indice inesperado: $selectedItemIndex")
                //Text("Ajustes Screen", modifier = Modifier.padding(it))
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
            }
 */



