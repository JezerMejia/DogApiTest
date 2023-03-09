package com.example.dogapitest

import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.URL

class mViewModel : ViewModel() {
    var data by mutableStateOf("")

    fun onStart() {
        load()
    }

    fun load() {
        viewModelScope.launch(Dispatchers.IO) {
            data = getDogImageSource()
        }
    }
}

suspend fun getDogImageSource(): String = coroutineScope {
    val apiMessage = URL("https://dog.ceo/api/breeds/image/random").readText()
    val json = JSONObject(apiMessage)
    println(json)

    return@coroutineScope json.getString("message")
}

@Composable
fun DogImage(modifier: Modifier = Modifier, viewModel: mViewModel = mViewModel()) {
    DisposableEffect(key1 = viewModel) {
        viewModel.onStart()
        onDispose { }
    }

    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(viewModel.data)
            .crossfade(true)
            .build(),
        loading = {
            BoxWithConstraints(propagateMinConstraints = true, modifier = Modifier.padding(22.dp)) {
                CircularProgressIndicator(strokeWidth = 14.dp)
            }
        },
        contentDescription = "Dog",
        modifier = modifier,
        contentScale = ContentScale.Fit,
    )
}

@Preview(showBackground = true)
@Composable
fun DogPreview() {
    DogImage(modifier = Modifier.size(300.dp))
}
