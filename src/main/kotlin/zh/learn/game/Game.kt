package zh.learn.game

import zh.learn.board.Cell
import zh.learn.board.Status

interface Game {
    fun initialize()
    fun hasWon(): Boolean
    fun toggleSecure(i: Int, j: Int)
    fun open(i: Int, j: Int): Int?
    operator fun get(i: Int, j: Int): Int?
    fun getStatus(i: Int, j: Int): Status
    fun getNeighbours(i: Int, j: Int): List<Cell>
}
