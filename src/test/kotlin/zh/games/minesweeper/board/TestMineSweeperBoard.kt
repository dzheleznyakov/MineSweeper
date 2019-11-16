package zh.games.minesweeper.board

import org.junit.Assert
import org.junit.Test

class TestMineSweeperBoard {
    @Test
    fun testStatus01ByDefaultCellStatusIsClosed() {
        val gameBoard = createMineSweeperBoard(2, 1)
        gameBoard.getAllCells().forEach {
            Assert.assertEquals(gameBoard.getStatus(it), Status.CLOSED)
        }
    }
}
