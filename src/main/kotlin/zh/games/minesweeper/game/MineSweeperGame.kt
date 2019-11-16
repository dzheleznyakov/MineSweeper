package zh.games.minesweeper.game

import zh.games.minesweeper.board.Cell
import zh.games.minesweeper.board.MineSweeperBoard
import zh.games.minesweeper.board.Status
import zh.games.minesweeper.board.createMineSweeperBoard

fun newMineSweeperGame(width: Int = 10, mineLimit: Int = 10, initializer: MineSweeperGameInitializer): Game
        = MineSweeperGame(width, mineLimit, initializer)

class MineSweeperGame(
    width: Int,
    private val mineLimit: Int,
    private val initializer: MineSweeperGameInitializer
) : Game {
    private val board = createMineSweeperBoard(width, mineLimit)

    override fun initialize() {
        repeat(mineLimit) {
            board.addNewMine(initializer)
        }
    }

    override fun hasWon(): Boolean {
        return board.filter { value -> value == -1 }.all { cell -> board.getStatus(cell) == Status.SECURED } &&
                board.filter { value -> value != -1 }.all { cell -> board.getStatus(cell) == Status.OPEN }
    }

    override fun toggleSecure(i: Int, j: Int) {
        val cell = Cell(i, j)
        board.getStatus(cell).run {
            when (this) {
                Status.CLOSED -> Status.SECURED
                Status.SECURED -> Status.CLOSED
                else -> this
            }
        }.run {
            board[cell] = this
        }
    }

    override fun open(i: Int, j: Int): Int?  = Cell(i, j).let {
        when(board.getStatus(it)) {
            Status.SECURED -> null
            else -> it
                .apply { board[this] = Status.OPEN }
                .run { board.getCellValue(this) }
        }
    }

    override fun get(i: Int, j: Int): Int? = board.run { get(getCell(i, j)) }

    override fun getStatus(i: Int, j: Int): Status = with(board) { getStatus(getCell(i, j)) }

    override fun getNeighbours(i: Int, j: Int) = board.run { getCell(i, j).getNeighbours() }
}

fun MineSweeperBoard.addNewMine(initializer: MineSweeperGameInitializer) {
    val nextCell = initializer.nextValue(this) ?: return
    this[nextCell] = -1
}

fun MineSweeperBoard.getCellValue(cell: Cell): Int {
    return this[cell] ?: cell.getNeighbours()
        .filter { this[it] == -1 }
        .apply { this@getCellValue[cell] = this.size }
        .run { this.size }
}
