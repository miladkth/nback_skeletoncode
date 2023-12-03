package mobappdev.example.nback_cimpl.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import mobappdev.example.nback_cimpl.ui.viewmodels.GameViewModel
import androidx.navigation.NavHostController


@Composable
fun GameScreen(vm: GameViewModel, navController: NavHostController) {
    // ... (unchanged code)
    val grid by vm.grid.collectAsState()

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


@Composable
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





