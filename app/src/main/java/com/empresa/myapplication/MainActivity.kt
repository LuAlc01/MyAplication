package com.empresa.myapplication

import android.R.attr.contentDescription
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.empresa.myapplication.ui.theme.MyApplicationTheme

data class BarraNavegacion(
    val titulo: String,
    val iconoSeleccionado: ImageVector,
    val iconoNoSeleccionado: ImageVector,
    val notis: Boolean, //Si es true quiero que salga una bolita
    val contadorBadges: Int? = null //el ? hace que una variable pueda ser null //corresponde al numero de mensajes/notis
)
//Añadire mas iconos con una dependencia que hay que buscar (aun no implementada)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
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

                    }
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
