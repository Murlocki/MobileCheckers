import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobilecheckers.models.Checker
import kotlin.math.sign
import kotlin.random.Random

class GameViewModel : ViewModel() {
    val checkers = MutableLiveData<MutableList<Checker>>()
    var selectedChecker = MutableLiveData<Checker?>()


    var isPlayerWhite = MutableLiveData<Boolean>()
    var currentPlayerTurn = MutableLiveData<Int>()
    var currentTurnCount = MutableLiveData<Int>()
    var currentWhiteCount = MutableLiveData<Int>()
    var currentBlackCount = MutableLiveData<Int>()

    // Управление состоянием цвета игрока
    fun getIsPlayerWhite(): Boolean {
        return this.isPlayerWhite.value ?: false
    }
    fun restoreIsPlayerWhiteState(savedPlayerWhite: Boolean) {
        isPlayerWhite.value = savedPlayerWhite
    }
    fun resetIsPlayerWhiteState(){
        isPlayerWhite.value = false
    }

    // Управление состоянием текущего хода
    fun getCurrentPlayerTurn():Int{
        return currentPlayerTurn.value ?: 0
    }
    fun restoreCurrentPlayerTurnState(currentPlayerTurn: Int) {
        this.currentPlayerTurn.value = currentPlayerTurn
    }
    fun resetCurrentPlayerTurnState(){
        this.currentPlayerTurn.value = if(isPlayerWhite.value!!) 0 else 1
    }

    // Управление состоянием количества совершенных ходов
    fun getCurrentTurnCount():Int{
        return currentTurnCount.value ?: 0
    }
    fun restoreCurrentTurnCountState(currentTurnCount: Int) {
        this.currentTurnCount.value = currentTurnCount
    }
    fun resetCurrentTurnCountState(){
        this.currentTurnCount.value = 0
    }

    // Управление состоянием количества белых шашек
    fun getCurrentWhiteCount():Int{
        return currentWhiteCount.value ?: 12
    }
    fun restoreWhiteCountState(currentWhiteCount: Int) {
        this.currentWhiteCount.value = currentWhiteCount
    }
    fun resetCurrentWhiteCountState(){
        this.currentWhiteCount.value = 12
    }

    // Управление состоянием количества черных шашек
    fun getCurrentBlackCount():Int{
        return currentBlackCount.value ?: 0
    }
    fun restoreBlackCountState(currentBlackCount: Int) {
        this.currentBlackCount.value = currentBlackCount
    }
    fun resetCurrentBlackCountState(){
        this.currentBlackCount.value = 12
    }

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
        for (row in 0..2) {
            for (col in 0 until 8 step 2) {
                loadedCheckers.add(Checker(row, if (row % 2 == 0) col + 1 else col, !isPlayerWhite.value!!))
            }
        }
        // Шашки игрока
        for (row in 5..7) {
            for (col in 0 until 8 step 2) {
                loadedCheckers.add(Checker(row, if (row % 2 == 0) col + 1 else col, isPlayerWhite.value!!))
            }
        }

//        loadedCheckers.add(Checker(6,1, isPlayerWhite.value!!))
//        loadedCheckers.add(Checker(4,1, isPlayerWhite.value!!))
//        loadedCheckers.add(Checker(3,0, !isPlayerWhite.value!!))


        checkers.value = loadedCheckers
        currentBlackCount.value = 12
        currentWhiteCount.value = 12
        currentPlayerTurn.value = 0
        currentTurnCount.value = 0
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
        if(currentPlayerTurn.value == 0 && !isPlayerWhite.value!!
            || currentPlayerTurn.value == 1 && isPlayerWhite.value!!) return

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

        val directions = if(selectedChecker.value!!.isWhite != isPlayerWhite.value)selectedChecker.value!!.getPossibleEnemyMoves() else selectedChecker.value!!.getPossibleMoves()

        for ((rowOffset, colOffset) in directions) {
            val newRow = checker.row + rowOffset
            val newCol = checker.col + colOffset
            if (newRow in 0 until 8 && newCol in 0 until 8 && !isOccupied(newRow, newCol)) {
                possibleMoves.add(Pair(newRow, newCol))
            }
        }
        this.currentMove = possibleMoves

        val attackDirections = if(selectedChecker.value!!.isWhite != isPlayerWhite.value)selectedChecker.value!!.getPossibleEnemyAttackMoves() else selectedChecker.value!!.getPossibleAttackMoves()
        for ((rowOffset, colOffset) in attackDirections){
            val newRow = checker.row + rowOffset
            val newCol = checker.col + colOffset
            val maxOffSet = if(!checker.isQueen) 1 else 8
            for(offSet in 1..maxOffSet){
                if (newRow in 0 until 8 && newCol in 0 until 8 && isOccupied(newRow, newCol)
                    && isEnemy(newRow,newCol)){
                    val jumpRow = newRow + sign(rowOffset.toDouble()) * offSet
                    val jumpCol = newCol + sign(colOffset.toDouble()) * offSet
                    if (jumpRow.toInt() in 0 until 8 && jumpCol.toInt() in 0 until 8 && !isOccupied(jumpRow.toInt(), jumpCol.toInt())
                    ) {
                        attackMoves.add(Triple(jumpRow.toInt(), jumpCol.toInt(),returnCheckerAtPos(newRow,newCol) as Checker))
                    }
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
        println("После перехода")
        println(selectedChecker.value)
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
        this.currentTurnCount.value = this.currentTurnCount.value?.plus(1)
    }
    fun changeCurrentTurn(){
        this.currentPlayerTurn.value = if(this.currentPlayerTurn.value == 1) 0 else 1
    }
    fun decraseRemainCount(isWhite:Boolean){
        if(isWhite){
            this.currentWhiteCount.value = this.currentWhiteCount.value!!.minus(1)
        }
        else{
            this.currentBlackCount.value = this.currentBlackCount.value!!.minus(1)
        }
    }

    //ИИ врага
    fun chooseNextChecker():Checker?{
        val enemyCheckers = this.checkers.value!!.filter { it.isWhite != isPlayerWhite.value }
        for(checker in enemyCheckers){
            this.selectedChecker.value = checker
            val possibleMoves = this.getPossibleMovesWithHighlights()
            println("Следующий ход врага")
            println(possibleMoves)
            println(checker)
            if(possibleMoves.first.isNotEmpty() || possibleMoves.second.isNotEmpty()){
                return checker
            }
        }
        return null
    }
    fun chooseNextMove():List<Int>?{
        if(currentAttack.isEmpty() && currentMove.isEmpty()){
            return null
        }
        val nextMove:List<Int> = if(currentAttack.isEmpty()) currentMove.random().toList() else currentAttack.random().toList().dropLast(1).map { it as Int }
        return nextMove
    }
}
