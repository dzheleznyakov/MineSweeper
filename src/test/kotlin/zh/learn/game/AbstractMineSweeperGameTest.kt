package zh.learn.game

import zh.learn.board.Cell
import zh.learn.board.MineSweeperBoard
import zh.learn.board.createMineSweeperBoard

abstract class AbstractMineSweeperGameTest {
    protected val width = 4
    protected val mineLimit = 2

    fun createBoard(input: String): MineSweeperBoard {
        return createBoard(TestBoard(input))
    }

    private fun createBoard(input: TestBoard): MineSweeperBoard = createMineSweeperBoard(width, mineLimit)
        .apply {
            getAllCells().forEach { cell ->
                this[cell] = input.values[cell.i - 1][cell.j - 1]
            }
        }

    object MockMineSweeperGameInitializer: MineSweeperGameInitializer {
        private lateinit var iterator: Iterator<Cell>

        fun init(board: MineSweeperBoard) {
            iterator = board.filter { it == -1 }.iterator()
        }

        override fun nextValue(board: MineSweeperBoard): Cell? =
            if (iterator.hasNext()) iterator.next() else null

    }
}

data class TestBoard(val board: String) {
    val values: List<List<Int?>> by lazy {
        board.trim()
            .split(" ")
            .map { row -> row.map { ch -> when(ch) {
                '-' -> null
                'B' -> -1
                else -> ch.toInt()
            } } }
    }
}
