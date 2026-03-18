package com.example.appsscript.view.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.appsscript.viewmodel.InsertViewModel

import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsertScreen(
    navController: NavController,
    viewModel: InsertViewModel
) {
    var nombre by remember { mutableStateOf("") }
    var desarrollador by remember { mutableStateOf("") }
    var publisher by remember { mutableStateOf("") }
    var owners by remember { mutableStateOf("") }
    var precio by remember { mutableStateOf("") }

//Añadir delay de 2 segundos despues de crear un juego
    var isWaitingForNavigation by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val loading by viewModel.loading.observeAsState(false)
    val message by viewModel.insertMessage.observeAsState()
    val success by viewModel.insertSuccess.observeAsState()
    val nombreError by viewModel.nombreError.observeAsState()
    val desarrolladorError by viewModel.desarrolladorError.observeAsState()
    val precioError by viewModel.precioError.observeAsState()

    LaunchedEffect(success) {
        if (success == true && !isWaitingForNavigation) {
            isWaitingForNavigation = true

            delay(1500)

            navController.popBackStack()
            viewModel.clearMessage()
            isWaitingForNavigation = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Insertar juego") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre *") },
                isError = nombreError != null,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            nombreError?.let {
                Text(it, color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth())
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = desarrollador,
                onValueChange = { desarrollador = it },
                label = { Text("Desarrollador *") },
                isError = desarrolladorError != null,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            desarrolladorError?.let {
                Text(it, color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth())
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = publisher,
                onValueChange = { publisher = it },
                label = { Text("Publisher (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = owners,
                onValueChange = { owners = it },
                label = { Text("Owners (opcional)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = precio,
                onValueChange = { precio = it },
                label = { Text("Precio en céntimos *") },
                isError = precioError != null,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            precioError?.let {
                Text(it, color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth())
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.insertGame(nombre, desarrollador, publisher, owners, precio) },
                enabled = !loading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Guardar juego")
                }
            }

            message?.let {
                Spacer(modifier = Modifier.height(12.dp))
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (success == true)
                            Color(0xFF4CAF50).copy(alpha = 0.15f)
                        else
                            MaterialTheme.colorScheme.errorContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = it,
                        color = if (success == true) Color(0xFF2E7D32) else MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
