package com.example.blacklotus.ui.screens.mainscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun MainScreen(recommendationViewModel: RecommendationViewModel = hiltViewModel()) {
    var id by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    val uiState by recommendationViewModel.uiState.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    Column  {
        Header()
        Column(
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 16.dp)
                .weight(1F),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = id,
                onValueChange = { newValue ->
                    id = newValue
                    errorMessage = "" },
                label = { Text("Введите ID", fontSize = 20.sp) },
                singleLine = true,
                isError = errorMessage.isNotEmpty(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = MaterialTheme.colorScheme.tertiary,
                    unfocusedTextColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.7f),
                    cursorColor = Color.DarkGray,
                    focusedBorderColor = Color.DarkGray,
                    unfocusedBorderColor = Color.DarkGray.copy(alpha = 0.7f),
                    focusedLabelColor = MaterialTheme.colorScheme.tertiary,
                    unfocusedLabelColor = MaterialTheme.colorScheme.tertiary
                ),
                textStyle = TextStyle(
                    fontSize = 20.sp,
                    textDecoration = TextDecoration.None
                ),
                trailingIcon = {
                    Icon(
                        modifier = Modifier.clickable {
                            if (id.toIntOrNull() != null) {
                                recommendationViewModel.loadRecommendations(id.toInt())
                                id = ""
                                keyboardController?.hide()
                            } else {
                                errorMessage = "ID должно быть числом"
                            }
                        },
                        imageVector = Icons.Default.Done,
                        contentDescription = "Отправить id"
                    )
                }
            )
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 16.sp),
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                )
            }

            Text(
                text = "Рекомендации",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.tertiary,
                style = TextStyle(
                    shadow = Shadow(
                        color = Color.Gray,
                        offset = Offset(2f, 2f),
                        blurRadius = 4f
                    )
                ),
                modifier = Modifier.padding(vertical = 16.dp),
                textAlign = TextAlign.Center
            )
            when (uiState) {
                is UiEvent.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                is UiEvent.Success -> {

                    val recommendations = (uiState as UiEvent.Success).recommendations
                    val groupedItems = recommendations.groupBy { it.title }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .border(width = 2.dp, color = Color.LightGray)
                            .padding(horizontal = 16.dp)
                    ) {
                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                        groupedItems.forEach { (title, items) ->

                            item {
                                Text(
                                    text = title,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 8.dp),
                                    style = TextStyle(
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.tertiary,
                                        shadow = Shadow(
                                            color = Color.Gray,
                                            offset = Offset(2f, 2f),
                                            blurRadius = 4f
                                        ),
                                        textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline
                                    ),
                                    textAlign = TextAlign.Center
                                )
                            }
                            item {
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                            items(items) { item ->
                                RecommendationCard(body = item.body)
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }
                    }
                }
                is UiEvent.Error -> {
                    val errorMessage = (uiState as UiEvent.Error).message
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
                        modifier = Modifier.padding(top = 16.dp).align(Alignment.CenterHorizontally)
                    )
                }
                is UiEvent.None -> {
                    Text(
                        text = "Введите ID",
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 20.sp),
                        color = Color.DarkGray,
                        modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun RecommendationCard(body: String){
    var boxSize by remember { mutableStateOf(IntSize.Zero) }

    Box(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .onSizeChanged { boxSize = it }
            .background(
                Brush.linearGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.onSecondary,
                        MaterialTheme.colorScheme.secondary
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(boxSize.width.toFloat(), boxSize.height.toFloat()),
                    tileMode = TileMode.Clamp
                )
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,

            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = body,
                textAlign = TextAlign.Unspecified,
                style = TextStyle(
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onTertiary,
                    shadow = Shadow(
                        color = Color(0xFF6650a4),
                        offset = Offset(4f, 4f),
                        blurRadius = 8f
                    )
                )
            )
        }
    }
}

@Composable
fun Header() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black.copy(alpha = 0.7F)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "-ˋˏ ༻ Black Lotus ༺ ˎˊ-",
            style = TextStyle(
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
        )
    }
}
