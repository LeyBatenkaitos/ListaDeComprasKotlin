package com.example.eva2.listadecompras

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.eva2.listadecompras.database.DatabaseConnection
import com.example.eva2.listadecompras.entities.Compra
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateCompra : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CreateCompraUI()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateCompraUI(){
    val buyCartText = stringResource(id = R.string.buy_cart)
    val productText = stringResource(id = R.string.product)
    val createText = stringResource(id =R.string.create )
    val contexto = LocalContext.current
    val estadoInicialCompra = ""
    var compra by remember {
        mutableStateOf(estadoInicialCompra)
    }

    Column (modifier = Modifier
        .fillMaxSize()
        .background(Color.White),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
        ) {
        Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = buyCartText,
            Modifier
                .padding(horizontal = 10.dp)
                .size(100.dp))

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            value = compra,
            onValueChange = { compra = it },
            label = { Text(text = productText) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            modifier = Modifier
                .width(300.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Button(onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                Log.i("CreateCompra", "Creando producto en base de datos")

                val dao = DatabaseConnection.getInstance(contexto).compraDAO()
                dao.insertCompra(Compra( compraName = compra,
                    isCompraRealizada = false))

                Log.i("CreateCompra", "Compra ingresada correctamente!!!")
            }
            val intent = Intent(contexto, MainActivity::class.java)
            contexto.startActivity(intent)
        }) {
            Text(text = createText)
        }

    }

    Box(modifier = Modifier.fillMaxSize()) {
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