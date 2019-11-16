package zh.learn.ui

import javafx.beans.property.IntegerProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.ObservableList
import javafx.scene.control.Label
import javafx.scene.input.MouseButton
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import zh.learn.board.Status
import zh.learn.game.Game

val DEFAULT_BACKGROUND = Background(BackgroundFill(Color.LIGHTSKYBLUE, CornerRadii(2.0), null))
val BACKGROUNDS = mapOf(
    Status.CLOSED to DEFAULT_BACKGROUND,
    Status.OPEN to Background(BackgroundFill(Color.WHITE, CornerRadii(2.0), null)),
    Status.SECURED to DEFAULT_BACKGROUND
)

val NUMBER_COLORS = mapOf(
    -1 to Color.BLACK,
    1 to Color.BLUE,
    2 to Color.GREEN,
    3 to Color.RED,
    4 to Color.PURPLE,
    5 to Color.MAROON,
    6 to Color.TURQUOISE,
    7 to Color.BLACK,
    6 to Color.GRAY
)

class Tile(
    private val game: Game,
    i: Int,
    j: Int,
    private val toOpenList: ObservableList<Pair<Int, Int>>,
    private val hasExploded: SimpleBooleanProperty,
    private val hasWon: SimpleBooleanProperty,
    private val secureCount: SimpleIntegerProperty
) : StackPane() {
    val row = i - 1
    val col = j - 1
    val value: IntegerProperty = SimpleIntegerProperty()

    private val statusProperty = SimpleObjectProperty(Status.CLOSED)

    init {
        prefWidthProperty().bind(ConfigInstance.tileSizeProperty)
        prefHeightProperty().bind(ConfigInstance.tileSizeProperty)
        style = """-fx-border-color: black;
            -fx-border-width: 2px;
            -fx-border-radius: 2px;
        """.trimMargin()

        background = Background(BackgroundFill(Color.LIGHTSKYBLUE, CornerRadii(2.0), null))
        statusProperty.addListener { _, _, newValue: Status -> background = BACKGROUNDS.getOrDefault(newValue, DEFAULT_BACKGROUND) }

        setOnMouseClicked { event -> when (event.button) {
            MouseButton.PRIMARY -> openTile()
            MouseButton.SECONDARY -> secureTile()
            MouseButton.MIDDLE -> openNeighbourTiles()
            else -> Unit
        } }
    }

    fun openTile() {
        when (statusProperty.get()) {
            Status.CLOSED -> {
                val value = game.open(this)
                val label = if (value == -1) Label("B") else Label("$value")
                label.textFill = NUMBER_COLORS[value]
                setLabelFont(label)
                children.setAll(label)
            }
            else -> Unit
        }
    }

    private fun secureTile() {
        when (statusProperty.get()) {
            Status.CLOSED -> {
                game.toggleSecure(this)
                val label = Label("S")
                setLabelFont(label)
                children.setAll(label)
            }
            Status.SECURED -> {
                game.toggleSecure(this)
                children.clear()
            }
            else -> Unit
        }
    }

    private fun openNeighbourTiles() =
        if (statusProperty.get() == Status.OPEN) game.openNeighbours(row + 1, col + 1)
        else Unit

    private fun setLabelFont(label: Label) {
        with(ConfigInstance) { label.font = Font.font(fontFamilyProperty.get(), FontWeight.BOLD, null, fontSizeProperty.get()) }
    }

    private fun Game.open(tile: Tile): Int? {
        val i = tile.row + 1
        val j = tile.col + 1
        val value = open(i, j)
        tile.statusProperty.set(getStatus(i, j))

        if (value == 0) openNeighbours(i, j)
        else if (value == -1) hasExploded.set(true)

        if (hasWon()) hasWon.set(true)

        return value ?: 0
    }

    private fun Game.openNeighbours(i: Int, j: Int) {
        getNeighbours(i, j)
            .map { (r, c) -> (r - 1) to (c - 1) }
            .forEach { toOpenList.add(it) }
    }

    private fun Game.toggleSecure(tile: Tile) {
        val i = tile.row + 1
        val j = tile.col + 1
        toggleSecure(i, j)
        tile.statusProperty.set(getStatus(i, j))
        if (getStatus(i, j) == Status.SECURED)
            secureCount.set(secureCount.get() - 1)
        else
            secureCount.set(secureCount.get() + 1)
    }
}
