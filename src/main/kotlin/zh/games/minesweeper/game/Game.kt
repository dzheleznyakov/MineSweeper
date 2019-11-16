package zh.games.minesweeper.game

import zh.games.minesweeper.board.Cell
import zh.games.minesweeper.board.Status

interface Game {
    fun initialize()
    fun hasWon(): Boolean
    fun toggleSecure(i: Int, j: Int)
    fun open(i: Int, j: Int): Int?
    operator fun get(i: Int, j: Int): Int?
    fun getStatus(i: Int, j: Int): Status
    fun getNeighbours(i: Int, j: Int): List<Cell>
}
