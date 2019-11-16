package zh.games.minesweeper.game

import zh.games.minesweeper.board.Cell
import zh.games.minesweeper.board.MineSweeperBoard
import kotlin.random.Random

interface MineSweeperGameInitializer {
    fun nextValue(board: MineSweeperBoard): Cell?
}

object RandomMineSweeperGameInitializer: MineSweeperGameInitializer {
    override fun nextValue(board: MineSweeperBoard): Cell? = board.run {
        if (isFull()) null
        else getAllCells()
            .filter { board[it] == null }
            .run {
                if (isEmpty()) null else this[Random.nextInt(size)]
            }
    }

}
