package zh.learn.game

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MineSweeperGameStatusesTest : AbstractMineSweeperGameTest() {
    @Test
    fun whenCellOpenedItsValueReturned() {
        val game = getInitializedGame()

        assertEquals(-1, game.open(1, 4))
        assertEquals(0, game.open(1, 1))
        assertEquals(2, game.open(1, 3))
        assertEquals(2, game.open(2, 3))
        assertEquals(1, game.open(3, 3))
        assertEquals(0, game.open(3, 1))
    }

    @Test
    fun whenCellSecuredNullReturned() {
        val game = getInitializedGame()

        game.toggleSecure(1, 1)
        game.toggleSecure(1, 4)

        assertNull(game.open(1, 1))
        assertNull(game.open(1, 4))
    }

    private fun getInitializedGame(): Game {
        MockMineSweeperGameInitializer.init(createBoard("---B ---B ---- ----"))
        val game = newMineSweeperGame(width, mineLimit, MockMineSweeperGameInitializer)
        game.initialize()
        return game
    }
}
