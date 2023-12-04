package mobappdev.example.nback_cimpl.ui.viewmodels

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import mobappdev.example.nback_cimpl.GameApplication
import mobappdev.example.nback_cimpl.NBackHelper
import mobappdev.example.nback_cimpl.data.UserPreferencesRepository

/**
 * This is the GameViewModel.
 *
 * It is good practice to first make an interface, which acts as the blueprint
 * for your implementation. With this interface we can create fake versions
 * of the viewmodel, which we can use to test other parts of our app that depend on the VM.
 *
 * Our viewmodel itself has functions to start a game, to specify a gametype,
 * and to check if we are having a match
 *
 * Date: 25-08-2023
 * Version: Version 1.0
 * Author: Yeetivity
 *
 */


interface GameViewModel {

    val gameState: StateFlow<GameState>
    val score: StateFlow<Int>
    val highscore: StateFlow<Int>
    val nBack: StateFlow<Int>
    val grid: StateFlow<List<List<Boolean>>>
    val counter: StateFlow<Int>
    fun setGameType(gameType: GameType)
    fun startGame()

    fun checkMatch()
    fun increaseNback() {}
    fun decreaseNback(){}
}

class GameVM(
    private val userPreferencesRepository: UserPreferencesRepository
): GameViewModel, ViewModel() {
    private val _gameState = MutableStateFlow(GameState())
    override val gameState: StateFlow<GameState>
        get() = _gameState.asStateFlow()


    private val _score = MutableStateFlow(0)
    override val score: StateFlow<Int>
        get() = _score

    private val _highscore = MutableStateFlow(0)
    override val highscore: StateFlow<Int>
        get() = _highscore

    // nBack is currently hardcoded
    private val _nBack = MutableStateFlow(1)
    override val nBack: StateFlow<Int>
        get() = _nBack

    private var job: Job? = null  // coroutine job for the game event
    private val eventInterval: Long = 2000L  // 2000 ms (2s)

    private val nBackHelper = NBackHelper()  // Helper that generate the event array
    private var events = emptyArray<Int>()  // Array with all events

    private var nBackHistory = emptyList<Int>()
    private val _grid: MutableStateFlow<List<List<Boolean>>> = MutableStateFlow(emptyList())
    override val grid: StateFlow<List<List<Boolean>>> get() = _grid.asStateFlow()

    private val _counter = MutableStateFlow(0)
    override val counter: StateFlow<Int>
        get() = _counter


    override fun setGameType(gameType: GameType) {
        // update the gametype in the gamestate
        _gameState.value = _gameState.value.copy(gameType = gameType)
    }

    override fun startGame() {
        job?.cancel()  // Cancel any existing game loop
        nBackHistory= emptyList()
        // Get the events from our C-model (returns IntArray, so we need to convert to Array<Int>)
        events = nBackHelper.generateNBackString(10, 9, 30, nBack.value).toList().toTypedArray()  // Todo Higher Grade: currently the size etc. are hardcoded, make these based on user input
        //val minArray = arrayOf(1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2, 1, 2)
        //events = minArray
        Log.d("GameVM", "The following sequence was generated: ${events.contentToString()}")

        job = viewModelScope.launch {
            when (gameState.value.gameType) {
                GameType.Audio -> runAudioGame()
                GameType.AudioVisual -> runAudioVisualGame()
                GameType.Visual -> runVisualGame(events)
            }
            // Todo: update the highscore
            if (_score.value > _highscore.value) {
                _highscore.value = _score.value
                userPreferencesRepository.saveHighScore(_highscore.value)
            }
        }
    }



    override fun checkMatch() {
        /**
         * Todo: This function should check if there is a match when the user presses a match button
         * Make sure the user can only register a match once for each event.
         */



        val currentGameState = gameState.value
        //values [1,2,1,2,1,2,1,2]
        //coutner 0 1 2 3 4 5 6 7
        //counter = 2
        // value = 1 match = true
            Log.d("mygame" ,"val"+currentGameState.eventValue)
        Log.d("mygame" ,"pval"+ nBackHistory[_counter.value-nBack.value] )

        if (currentGameState.eventValue==nBackHistory[_counter.value-nBack.value] && nBack.value <= _counter.value){
            Log.d("mygame" ,"match")
            _score.value++
        }

        //check if the event is valid(not equal to -1) and has not been registered yes
        if (currentGameState.eventValue != -1 && isEventAlreadyRegistered(currentGameState.eventValue)) {
            //check if there is a match based on user input

            val isMatch = false


            if (isMatch) {
                //update score and prevent registrering a match for the same event again
                _score.value += 1
                _gameState.value = currentGameState.copy(eventValue = -1)
            }
        }

    }

    // Set to keep track of registered events
    private val registeredEvents = mutableSetOf<Int>()

    // Function to check if the event has already been registered
    private fun isEventAlreadyRegistered(eventValue: Int): Boolean {
        return registeredEvents.contains(eventValue)
    }
    // Function to register the event
    private fun registerEvent(eventValue: Int) {
        registeredEvents.add(eventValue)
        _gameState.value = _gameState.value.copy(eventValue = -1)
    }

    // TODO: Implement the logic to check if user input matches the event
    private fun isUserInputMatch(userInput:GameType): Boolean {
        // TODO: Replace this with your actual logic to check if user input matches the event
        return false
    }


    private fun runAudioGame() {
        // Todo: Make work for Basic grade
    }

    /*private suspend fun runVisualGame(events: Array<Int>){
        // Todo: Replace this code for actual game code
        Log.d("mygame","in visual game")
        initGrid()
        for (value in events) {
            _gameState.value = _gameState.value.copy(eventValue = value)
            delay(eventInterval)
        }

    }*/

    private suspend fun runVisualGame(events: Array<Int>) {
        Log.d("mygame", "in visual game")

        for (value in events) {
            _gameState.value = _gameState.value.copy(eventValue = value)
            nBackHistory = nBackHistory + value
            Log.d("NBack History", nBackHistory.toString())
            delay(eventInterval)
            _counter.value++
        }
    }

    private fun runAudioVisualGame(){
        // Todo: Make work for Higher grade
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as GameApplication)
                GameVM(application.userPreferencesRespository)
            }
        }
    }

    init {
        // Code that runs during creation of the vm
        viewModelScope.launch {
            userPreferencesRepository.highscore.collect {
                _highscore.value = it
            }
        }
    }

    fun initGrid(){
        val grid = List(3) { List(3) { false } }
        _grid.value = grid
    }
    override fun increaseNback(){
        if (_nBack.value < 5) {
            _nBack.value +=1
        }else{
            _nBack.value = 5
        }
    }

     override fun decreaseNback(){
         if (_nBack.value>1) {
             _nBack.value -=1
         }else{
             _nBack.value = 1
         }
    }
}



// Class with the different game types
enum class GameType{
    Audio,
    Visual,
    AudioVisual
}

data class GameState(
    // You can use this state to push values from the VM to your UI.
    val gameType: GameType = GameType.Visual,  // Type of the game
    val eventValue: Int = -1  // The value of the array string
)

