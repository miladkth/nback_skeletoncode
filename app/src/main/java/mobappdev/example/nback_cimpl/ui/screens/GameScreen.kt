package mobappdev.example.nback_cimpl.ui.screens

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import mobappdev.example.nback_cimpl.ui.viewmodels.GameViewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import mobappdev.example.nback_cimpl.R
import mobappdev.example.nback_cimpl.ui.theme.Purple40
import mobappdev.example.nback_cimpl.ui.theme.PurpleGrey80
import mobappdev.example.nback_cimpl.ui.viewmodels.GameType


@Composable
fun GameScreen(
    vm: GameViewModel,
    navController: NavHostController
) {
    val gameState = vm.gameState.collectAsState()
    val score = vm.score.collectAsState()
    val highScore = vm.highscore


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Text displaying the current user's game score
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            text = "Score: ${score.value}",
            style = MaterialTheme.typography.headlineMedium

        )

        // LazyVerticalGrid for the 3x3 game grid
        if (gameState.value.gameType != GameType.Audio){
            Text(
                text = "Current event Number: ${gameState.value.eventValue} ",
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(8.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .padding(8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalArrangement = Arrangement.Center
            ){
                items(9) { index ->
                    GameBox(
                        card = index + 1,
                        currentEventValue = gameState.value.eventValue

                    )
                }
            }
        }


        // Row for the three buttons (Start Game, Sound, Position Match)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            // Start Game button
            Button(
                onClick = {
                    // TODO: Add logic to start the game
                    vm.startGame()
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text(text = "Start")
            }

            // Sound button
            Button(
                onClick = {
                    // TODO: Add logic for the sound button clicked
                    if (gameState.value.gameType.equals(GameType.Audio)){
                        //vm.checkMatch()

                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .background(Color.Transparent)

            ) {
                Icon(
                    painter = painterResource(id = R.drawable.sound_on),
                    contentDescription = "Sound",
                    modifier = Modifier
                        .height(48.dp)
                        .aspectRatio(3f / 2f)
                )
            }

            // Position Match button
            Button(
                onClick = {
                    // TODO: Add logic for the position match button clicked

                    if (gameState.value.gameType.equals(GameType.Visual)){
                        //vm.checkMatch()
                    }

                },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
                    .background(Color.Transparent)

            ) {
                Icon(
                    painter = painterResource(id = R.drawable.visual),
                    contentDescription = "Position Match",
                    modifier = Modifier
                        .height(48.dp)
                        .aspectRatio(3f / 2f)

                )
            }
        }
    }

}




@Composable
fun GameBox(
    card: Int,
    currentEventValue: Int
) {
    val inactiveColor = PurpleGrey80
    val activeColor = Purple40
    // Use animateColorAsState to animate color change
    val backgroundColor by animateColorAsState(targetValue = if (currentEventValue == card) activeColor else inactiveColor)
    Box(
        modifier = Modifier
            .padding(8.dp)
            .background(backgroundColor)
            .aspectRatio(1f)
    ) {
        // Content of the box
    }
}





/*
@Composable
fun GameScreen(vm: GameViewModel, navController: NavHostController) {
    // ... (unchanged code)
    val grid by vm.grid.collectAsState()
    val gameState by vm.gameState.collectAsState()
 val orientation = null;
 Row(
    horizontalArrangement = Arrangement.Center


) {

  Text(text = "Score")
 }
 Column(
  modifier = Modifier
      .fillMaxSize()
      .padding(5.dp),
  verticalArrangement = Arrangement.Center,
  horizontalAlignment = Alignment.CenterHorizontally
 ) {
     Grid(grid=grid)
 }

}


/*@Composable
fun Grid(
 grid: List<List<Boolean>>,
) {
    Column {
        for (i in grid.indices){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ){
                for (j in grid[i].indices){
                    Box()
                }
            }
        }
    }

}*/



@Composable
fun Grid(grid: List<List<Boolean>>) {
    Column {
        for (i in grid.indices) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                for (j in grid[i].indices) {
                    val cellValue = grid[i][j]
                    Box(
                        modifier = Modifier
                            .size(50.dp) // Ändra storlek efter behov
                            .background(if (cellValue) Color.Blue else Color.Gray) // Färg efter cellens värde
                    ) {
                        // Här kan du eventuellt lägga till ytterligare innehåll för varje ruta
                    }
                }
            }
        }
    }
}


@Composable
fun Box() {
    var boxSize = 300f

    Box(
        modifier = Modifier
            .size(boxSize.dp)
            .background(Color.Blue)
    ){

    }
}


*/


