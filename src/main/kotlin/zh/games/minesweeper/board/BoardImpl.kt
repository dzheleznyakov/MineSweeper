package zh.games.minesweeper.board

import zh.games.minesweeper.board.Direction.UP
import zh.games.minesweeper.board.Direction.DOWN
import zh.games.minesweeper.board.Direction.LEFT

fun createRectangularBoard(height: Int, width: Int): RectangularBoard = RectangularBoardImpl(height, width)
fun createSquareBoard(width: Int): RectangularBoard = RectangularBoardImpl(width)
fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl(createSquareBoard(width))
fun <T> createGameBoard(height: Int, width: Int): GameBoard<T> = GameBoardImpl(createRectangularBoard(height, width))
fun createMineSweeperBoard(width: Int, mineLimit: Int): MineSweeperBoard = MineSweeperBoardImpl(createGameBoard(width), mineLimit)
fun createMineSweeperBoard(height: Int, width: Int, mineLimit: Int): MineSweeperBoard = MineSweeperBoardImpl(createGameBoard(height, width), mineLimit)

private open class RectangularBoardImpl(override val height: Int, override val width: Int) : RectangularBoard {
    private val board: List<List<Cell>>

    init {
        require(width > 0) { "Width should be greater than zero" }
        require(height > 0) { "Height should be greater than zero" }
        board = (1 .. height)
            .map { i -> (1 .. width).map { j -> Cell(i, j) }.toList() }
            .toList()
    }

    constructor(width: Int) : this(width, width)

    override fun getCellOrNull(i: Int, j: Int): Cell? = when {
        i < 1 || i > height -> null
        j < 1 || j > width -> null
        else -> board[i - 1][j - 1]
    }

    override fun getCell(i: Int, j: Int): Cell =
        getCellOrNull(i, j) ?: throw IllegalArgumentException("Illegal coordinates: ($i, $j)")

    override fun getAllCells(): Collection<Cell> = (1..height)
        .flatMap { i -> getRow(i, 1..width) }
        .toList()

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        require(i in 1..height) { "i should be between 1 and $height (inclusively)" }
        return jRange.mapNotNull { j -> getCellOrNull(i, j) }
            .toList()
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        require(j in 1..width) { "j should be between 1 and $width (inclusively)" }
        return iRange.mapNotNull { i -> getCellOrNull(i, j) }
            .toList()
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? = when (direction) {
        UP -> getCellOrNull(i - 1, j)
        DOWN -> getCellOrNull(i + 1, j)
        LEFT -> getCellOrNull(i, j - 1)
        else -> getCellOrNull(i, j + 1)
    }

    override fun Cell.getNeighbours(): List<Cell> = listOf(-1, 0, 1)
        .run { this.map { rowShift -> this.map { colShift -> rowShift to colShift } } }
        .asSequence()
        .flatten()
        .map { (rowShift, colShift) -> (i + rowShift) to (j + colShift) }
        .filter { coordinates -> coordinates != (i to j) }
        .map { (row, col) -> getCellOrNull(row, col) }
        .filterNotNull()
        .toList()
}

private class GameBoardImpl<T>(val reqBoard: RectangularBoard) : GameBoard<T>, RectangularBoard by reqBoard {
    private var contentMap: Map<Cell, T?> = mapOf()

    override fun get(cell: Cell): T? = contentMap[cell]

    override fun set(cell: Cell, value: T?) {
        contentMap = if (value == null)
            contentMap - cell
        else
            contentMap + (cell to value)
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> = getAllCells().filter { predicate(get(it)) }

    override fun find(predicate: (T?) -> Boolean): Cell? = getAllCells().find { predicate(get(it)) }

    override fun any(predicate: (T?) -> Boolean): Boolean = getAllCells().any { predicate(get(it)) }

    override fun all(predicate: (T?) -> Boolean): Boolean = getAllCells().all { predicate(get(it)) }
}

private class MineSweeperBoardImpl(val gameBoard: GameBoard<Int?>, val mineLimit: Int) : MineSweeperBoard, GameBoard<Int?> by gameBoard {
    private var statusMap: Map<Cell, Status> = mutableMapOf()

    override fun set(cell: Cell, status: Status) {
        statusMap = statusMap + (cell to status)
    }

    override fun getStatus(cell: Cell): Status = statusMap[cell] ?: Status.CLOSED

    override fun isFull(): Boolean = filter { it == -1 }.size >= mineLimit
}
