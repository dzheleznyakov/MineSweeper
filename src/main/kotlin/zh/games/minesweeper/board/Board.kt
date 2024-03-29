package zh.games.minesweeper.board

data class Cell(val i: Int, val j: Int) {
    override fun toString()= "($i, $j)"
}

enum class Direction {
    UP, DOWN, RIGHT, LEFT;

    fun reversed() = when (this) {
        UP -> DOWN
        DOWN -> UP
        RIGHT -> LEFT
        LEFT -> RIGHT
    }
}

enum class Status {
    SECURED, CLOSED, OPEN
}

interface RectangularBoard {
    val width: Int
    val height: Int

    fun getCellOrNull(i: Int, j: Int): Cell?
    fun getCell(i: Int, j: Int): Cell

    fun getAllCells(): Collection<Cell>

    fun getRow(i: Int, jRange: IntProgression): List<Cell>
    fun getColumn(iRange: IntProgression, j: Int): List<Cell>

    fun Cell.getNeighbour(direction: Direction): Cell?
    fun Cell.getNeighbours(): List<Cell>
}

interface GameBoard<T> : RectangularBoard {
    operator fun get(cell: Cell): T?
    operator fun set(cell: Cell, value: T?)

    fun filter(predicate: (T?) -> Boolean): Collection<Cell>
    fun find(predicate: (T?) -> Boolean): Cell?
    fun any(predicate: (T?) -> Boolean): Boolean
    fun all(predicate: (T?) -> Boolean): Boolean
}

interface MineSweeperBoard : GameBoard<Int?> {
    operator fun set(cell: Cell, status: Status)
    fun getStatus(cell: Cell): Status
    fun isFull(): Boolean
}
