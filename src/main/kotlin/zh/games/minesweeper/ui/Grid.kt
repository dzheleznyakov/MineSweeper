package zh.games.minesweeper.ui

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.collections.FXCollections
import javafx.collections.ListChangeListener
import javafx.geometry.Insets
import javafx.scene.Node
import javafx.scene.layout.GridPane
import zh.games.minesweeper.board.Cell
import zh.games.minesweeper.game.Game
import zh.games.minesweeper.ui.config.ConfigInstance

class Grid(
    private val hasExploded: SimpleBooleanProperty,
    private val hasWon: SimpleBooleanProperty,
    private val secureCount: SimpleIntegerProperty,
    private val height: Int,
    private val width: Int
) : GridPane() {
    private val toOpenList = FXCollections.observableArrayList<Pair<Int, Int>>()
    private var toOpenListListener: (ListChangeListener.Change<out Pair<Int, Int>>) -> Unit = {}

    fun setGame(game: Game) {
        children.removeAll()
        getAllCells().map { (i, j) ->
            Tile(game, i, j, toOpenList, hasExploded, hasWon, secureCount)
                .apply { setMargin(this, Insets(ConfigInstance.tileMarginProperty.get())) }
        }
            .forEach(this::add)
        toOpenList.removeListener(toOpenListListener)
        toOpenListListener =  { it.openTiles() }
        toOpenList.addListener(toOpenListListener)
    }

    private fun getAllCells() = (1..height)
        .map { row -> (1..width).map { col -> Cell(row, col) } }
        .flatten()

    private fun ListChangeListener.Change<out Pair<Int, Int>>.openTiles() {
        next()
        if (wasAdded()) {
            (from until to).map { list[it] }
                .mapNotNull { (row, col) -> this@Grid.children.find { tile: Node -> tile is Tile && tile.col == col && tile.row == row } as Tile }
                .forEach { tile ->
                    tile.openTile()
                    list.remove(tile.row to tile.col)
                }
        }
    }
}
