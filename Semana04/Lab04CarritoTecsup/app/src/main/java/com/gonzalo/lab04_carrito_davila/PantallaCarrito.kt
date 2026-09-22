package com.gonzalo.lab04_carrito_davila

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PantallaCarrito() {

    var nombre by rememberSaveable { mutableStateOf("") }
    var precio by rememberSaveable { mutableStateOf("") }
    var cantidad by rememberSaveable { mutableStateOf("") }

    // Esta lista persistirá y acumulará los productos
    val productos = remember {
        mutableStateListOf<Producto>()
    }

    Column(modifier = Modifier.padding(16.dp)) {

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = precio,
            onValueChange = { precio = it },
            label = { Text("Precio") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = cantidad,
            onValueChange = { cantidad = it },
            label = { Text("Cantidad") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                val precioNum = precio.toDoubleOrNull() ?: 0.0
                val cantidadNum = cantidad.toIntOrNull() ?: 0

                if (nombre.isNotBlank() && precioNum > 0 && cantidadNum > 0) {
                    // Se crea y agrega el nuevo producto a la lista existente
                    productos.add(
                        Producto(
                            nombre = nombre,
                            precio = precioNum,
                            cantidad = cantidadNum
                        )
                    )

                    // Limpieza de campos
                    nombre = ""
                    precio = ""
                    cantidad = ""
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text("AGREGAR")
        }

        // Contador de productos acumulados
        Text(
            text = "Productos: ${productos.size}",
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}