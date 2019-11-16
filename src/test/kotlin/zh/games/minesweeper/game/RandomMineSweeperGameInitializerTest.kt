package zh.games.minesweeper.game

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import zh.games.minesweeper.board.Cell

class RandomMineSweeperGameInitializerTest : AbstractMineSweeperGameTest() {
    @Test
    fun initEmptyBoard() = testNextValue("---- ---- ---- ----")

    @Test
    fun initNotEmptyBoard1() = testNextValue("---- --B- ---- ----")

    @Test
    fun initNotEmptyBoard2() = testNextValue("---- ---- --B- ----")

    @Test
    fun initFullBoard() {
        val board = createBoard("--B- ---- -B-- ----")
        val nextCell = RandomMineSweeperGameInitializer.nextValue(board)
        assertNull(nextCell)
    }

    private fun testNextValue(input: String) {
        val board = createBoard(input)
        val nextCell = RandomMineSweeperGameInitializer.nextValue(board)
        val empty = board.filter { it == null }
        assertTrue(empty.contains(nextCell))
    }

    @Test
    fun initializerGivesCellsRandomly() {
        val initializedCells: MutableSet<Cell?> = mutableSetOf()
        repeat(1000) { createBoard("---- ---- ---- ----")
            .run { RandomMineSweeperGameInitializer.nextValue(this) }
            .apply { initializedCells.add(this) }
        }
        assertEquals(initializedCells.size, 16)
    }
}
