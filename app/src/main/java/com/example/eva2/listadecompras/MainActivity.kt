package com.example.eva2.listadecompras

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.example.eva2.listadecompras.database.DatabaseConnection
import com.example.eva2.listadecompras.entities.Compra
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PantallaInicio()
        }
    }
}

@Preview
@Composable
fun PantallaInicio() {
    val noProductsText = stringResource(id = R.string.no_products)
    val buyText = stringResource(id = R.string.buy)
    val purchasedText = stringResource(id = R.string.purchased)
    val deleteItemText = stringResource(id = R.string.delete_item)
    val createText = stringResource(id =R.string.create )

    val contexto = LocalContext.current
    Log.i("MainActivity", "Menu principal")

    var (compras, setCompras) = remember {
        mutableStateOf(emptyList<Compra>())
    }

    LaunchedEffect(Unit) {
        withContext(Dispatchers.IO){
            val dao = DatabaseConnection.getInstance(contexto).compraDAO()
            setCompras(dao.getAll())
        }
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White)) {
        if (compras.isEmpty()) {
            Text(text = noProductsText, modifier = Modifier.align(Alignment.Center))
        } else {
            LazyColumn (modifier = Modifier
                .fillMaxSize()
                .padding(40.dp)) {
                items(compras) {
                    compra ->
                    Log.i("MainACtivity", "CompraId: ${compra.id}")
                    Row {
                        if (!compra.isCompraRealizada) {
                            Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = buyText,
                                Modifier
                                    .padding(horizontal = 10.dp)
                                    .size(40.dp)
                                    .clickable {
                                        updateCompra(compra, contexto)
                                        val intent = Intent(contexto, MainActivity::class.java)
                                        contexto.startActivity(intent)
                                    })
                        } else {
                            Icon(imageVector = Icons.Default.Check, contentDescription = purchasedText,
                                Modifier
                                    .padding(horizontal = 10.dp)
                                    .size(40.dp))
                        }

                        Text(text = compra.compraName,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold,
                            fontSize = TextUnit(4f, TextUnitType.Em),
                            modifier = Modifier.align(Alignment.CenterVertically)
                        )

                        Box(modifier = Modifier
                            .fillMaxSize()){
                            Icon(imageVector = Icons.Default.Delete, contentDescription = deleteItemText,
                                Modifier
                                    .padding(horizontal = 10.dp)
                                    .align(Alignment.CenterEnd)
                                    .size(40.dp)
                                    .clickable {
                                        deleteCompra(compra, contexto)
                                        val intent = Intent(contexto, MainActivity::class.java)
                                        contexto.startActivity(intent)
                                    },
                                tint = Color.Red
                            )
                        }
                    }
                }
            }
        }

        FloatingActionButton(onClick = {
            Log.i("MainActivity", "Agregando compra")
            val intent = Intent(contexto, CreateCompra::class.java)
            contexto.startActivity(intent)

        }, modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(16.dp)
            .width(100.dp)) {
            Icon(imageVector = Icons.Default.Add, contentDescription = createText,
                Modifier
                    .align(Alignment.CenterStart)
                    .padding(horizontal = 10.dp))
            //Spacer(modifier = Modifier.width(8.dp)) // Space between icon and text
            Text(text = createText, modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(horizontal = 24.dp))
        }
    }

}

fun deleteCompra(compra: Compra, contexto: Context){
    CoroutineScope(Dispatchers.IO).launch {
        Log.i("deleteComra", "Eliminar producto en base de datos")

        val dao = DatabaseConnection.getInstance(contexto).compraDAO()
        dao.deleteCompra(compra)

        Log.i("deleteComra", "Compra eliminada correctamente!!!")
    }
}

fun updateCompra(compra: Compra, contexto: Context){
    CoroutineScope(Dispatchers.IO).launch {
        Log.i("updateCompra", "Actualizando producto en base de datos")
        compra.isCompraRealizada = true
        val dao = DatabaseConnection.getInstance(contexto).compraDAO()
        dao.updateCompra(compra)

        Log.i("updateCompra", "Compra actualizada correctamente!!!")
    }
}