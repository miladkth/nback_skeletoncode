package mobappdev.example.nback_cimpl.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import mobappdev.example.nback_cimpl.ui.viewmodels.GameViewModel
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.ui.platform.LocalConfiguration


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun GameScreen(
    vm: GameViewModel
) {
    var orientation by remember { mutableStateOf(Configuration.ORIENTATION_PORTRAIT) }

    val configuration = LocalConfiguration.current

    // If our configuration changes then this will launch a new coroutine scope for it
    LaunchedEffect(configuration) {
        // Save any changes to the orientation value on the configuration object
        snapshotFlow { configuration.orientation }
            .collect { orientation = it }
    }

    /*when(orientation) {//implementeras beronde på design ->stående eller liggande

    }
    else -> {

    }*/
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Visa poängen här
        Text(text = "Score: ${vm.score}")

        // Rutnät med 9x9 celler
        LazyVerticalGrid(
            cells = GridCells.Fixed(9),
            modifier = Modifier.fillMaxSize()
        ) {
            items(81) { index ->
                // Hantera interaktion med varje cell här om det är nödvändigt
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Gray)
                        .padding(4.dp)
                ) {
                    Text(
                        text = index.toString(),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                            .clip(MaterialTheme.shapes.medium)
                            .background(Color.White)
                            .padding(4.dp)
                            .rotate(45f)
                            .padding(4.dp)
                            .rotate(-45f)
                            .padding(4.dp)
                            .rotate(45f)
                            .padding(4.dp)
                            .rotate(-45f),
                        color = Color.Black,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Knapp för att starta spelet eller göra andra åtgärder
        Button(onClick = { vm.startGame() }) {
            Text(text = "Start Game")
        }

        // Här kan du lägga till andra komponenter för spelets logik, t.ex. en spelplan eller knappar för spelåtgärder
    }
}
