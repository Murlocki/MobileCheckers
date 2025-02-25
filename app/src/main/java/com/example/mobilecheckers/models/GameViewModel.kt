import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobilecheckers.models.Checker
import kotlin.random.Random

class GameViewModel : ViewModel() {
    val checkers = MutableLiveData<MutableList<Checker>>()
    var selectedChecker = MutableLiveData<Checker?>()


    var isPlayerWhite = MutableLiveData<Boolean>()
    var currentPlayerTurn = MutableLiveData<Int>()
    var currentTurnCounts = MutableLiveData<Int>()
    var currentWhiteCounts = MutableLiveData<Int>()
    var currentBlackCounts = MutableLiveData<Int>()


    var currentMove: List<Pair<Int, Int>> = listOf()
    var currentAttack: List<Triple<Int, Int, Checker>> = listOf()
    init {
        if (checkers.value.isNullOrEmpty()) {
            loadCheckers()
        }
    }

    private fun loadCheckers() {
        val loadedCheckers = mutableListOf<Checker>()
        isPlayerWhite.value = Random.nextBoolean()
        // Шашки врага
//        for (row in 0..2) {
//            for (col in 0 until 8 step 2) {
//                loadedCheckers.add(Checker(row, if (row % 2 == 0) col + 1 else col, !isPlayerWhite.value!!))
//            }
//        }
        loadedCheckers.add(Checker(2,5,!isPlayerWhite.value!!))
        loadedCheckers.add(Checker(4,3,!isPlayerWhite.value!!))
        // Шашки игрока
        for (row in 5..7) {
            for (col in 0 until 8 step 2) {
                loadedCheckers.add(Checker(row, if (row % 2 == 0) col + 1 else col, isPlayerWhite.value!!))
            }
        }

        checkers.value = loadedCheckers
        currentBlackCounts.value = 12
        currentWhiteCounts.value = 12
        currentPlayerTurn.value = if(isPlayerWhite.value!!) 0 else 1
        currentTurnCounts.value = 0
    }

    fun saveCheckersState(): List<Checker> {
        return checkers.value ?: emptyList()
    }

    fun restoreCheckersState(savedCheckers: List<Checker>) {
        checkers.value = savedCheckers.toMutableList()
    }
    fun resetCheckers(){
        checkers.value?.clear()
    }
    fun selectChecker(checker: Checker?) {
        if (selectedChecker.value == checker){
            selectedChecker.value = null
            return
        }
        if(checker?.isWhite == isPlayerWhite.value) selectedChecker.value = checker
    }
    fun dropCheckerAtIndex(index:Int){
        this.checkers.value!!.removeAt(index)
    }
    fun getPossibleMovesWithHighlights(): Pair<List<Pair<Int, Int>>, List<Triple<Int, Int, Checker>>> {
        val checker = selectedChecker.value ?: return Pair(emptyList(), emptyList())
        val possibleMoves = mutableListOf<Pair<Int, Int>>()
        val attackMoves = mutableListOf<Triple<Int, Int,Checker>>()

        val directions = selectedChecker.value!!.getPossibleMoves()


        for ((rowOffset, colOffset) in directions) {
            val newRow = checker.row + rowOffset
            val newCol = checker.col + colOffset
            if (newRow in 0 until 8 && newCol in 0 until 8 && !isOccupied(newRow, newCol)) {
                possibleMoves.add(Pair(newRow, newCol))
            }
        }
        this.currentMove = possibleMoves

        val attackDirections = selectedChecker.value!!.getPossibleAttackMoves()
        for ((rowOffset, colOffset) in attackDirections){
            val newRow = checker.row + rowOffset
            val newCol = checker.col + colOffset
            if (newRow in 0 until 8 && newCol in 0 until 8 && isOccupied(newRow, newCol)
                && isEnemy(newRow,newCol)){
                val jumpRow = newRow + rowOffset
                val jumpCol = newCol + colOffset
                if (jumpRow in 0 until 8 && jumpCol in 0 until 8 && !isOccupied(jumpRow, jumpCol)
                ) {
                    attackMoves.add(Triple(jumpRow, jumpCol,returnCheckerAtPos(newRow,newCol) as Checker))
                }
            }
        }
        this.currentAttack = attackMoves

        if(attackMoves.isNotEmpty()){
            this.currentMove = listOf()
            possibleMoves.clear()
        }
        return Pair(possibleMoves, attackMoves)
    }

    private fun returnCheckerAtPos(row: Int,col: Int) = checkers.value?.find { it.row == row && it.col == col }

    private fun isOccupied(row: Int, col: Int): Boolean {
        return checkers.value?.any { it.row == row && it.col == col } == true
    }
    private fun isEnemy(row: Int, col: Int): Boolean {
        return checkers.value?.any { it.row == row && it.col == col && it.isWhite != selectedChecker.value!!.isWhite } == true
    }
    fun moveChecker(newRow: Int, newCol: Int) {
        selectedChecker.value?.moveChecker(newRow,newCol)
    }

    fun currentCheckerValue(): Checker? {
        return selectedChecker.value
    }
    fun restoreCurrentCheckerValue(checker: Checker?){
        this.selectedChecker.value = checker
    }
    fun clearCurrentChecker(){
        selectedChecker.value = null
    }


    //Управление статистикой
    fun increaseTurnCount(){
        this.currentTurnCounts.value = this.currentTurnCounts.value?.plus(1)
    }
    fun changeCurrentTurn(){
        this.currentPlayerTurn.value = if(this.currentPlayerTurn.value == 1) 0 else 1
    }
    fun decraseRemainCount(isWhite:Boolean){
        if(isWhite){
            this.currentWhiteCounts.value = this.currentWhiteCounts.value!!.minus(1)
        }
        else{
            this.currentBlackCounts.value = this.currentBlackCounts.value!!.minus(1)
        }
    }
}
