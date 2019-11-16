package zh.games.minesweeper.ui

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler
import javafx.application.Application
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.collections.FXCollections
import javafx.collections.ListChangeListener
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Node
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.layout.BorderPane
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.text.Font
import javafx.scene.text.FontWeight
import javafx.scene.text.Text
import javafx.stage.Stage
import zh.games.minesweeper.board.Cell
import zh.games.minesweeper.game.Game
import zh.games.minesweeper.game.RandomMineSweeperGameInitializer
import zh.games.minesweeper.game.newMineSweeperGame
import java.util.concurrent.TimeUnit

class PlayMineSweeperGame: Application() {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(PlayMineSweeperGame::class.java)
        }
    }

    private val width = 10
    private val mineLimit = 10
    private val game: Game = newMineSweeperGame(width, mineLimit, RandomMineSweeperGameInitializer)
    private val hasExploded = SimpleBooleanProperty(false)
    private val hasWon = SimpleBooleanProperty(false)
    private val secureCount = SimpleIntegerProperty(mineLimit)
    private val gameStarted = SimpleBooleanProperty(false)

    private var timerSubscription: Disposable? = null

    override fun init() {
        game.initialize()
    }

    override fun start(stage: Stage) {
        val grid = getGrid()

        val mineCounter = getMineCounter()
        val timer = getTimer()
        val statContainer = HBox(10.0, mineCounter, timer)

        val gridContainer = StackPane(grid)
        gridContainer.setOnMouseClicked { _ -> if (!gameStarted.get()) {
            gameStarted.set(true)
            timerSubscription = Observable.interval(1, TimeUnit.SECONDS)
                .observeOn(JavaFxScheduler.platform())
                .subscribe { timer.text = "$it" }
        } }

        val root = BorderPane().apply {
            top = statContainer
            center = gridContainer
        }
        BorderPane.setAlignment(mineCounter, Pos.CENTER)
        BorderPane.setMargin(mineCounter, Insets(8.0))

        listenToHasExploded(gridContainer)
        listenToHasWon(gridContainer)

        with(stage) {
            scene = Scene(root)
            title = "Mine Sweeper"
            show()
        }
    }

    private fun getGrid(): GridPane {
        val grid = GridPane()

        val toOpenList = FXCollections.observableArrayList<Pair<Int, Int>>()

        getAllCells().map { (i, j) ->
            Tile(game, i, j, toOpenList, hasExploded, hasWon, secureCount)
                .apply { GridPane.setMargin(this, Insets(ConfigInstance.tileMarginProperty.get())) }
        }
            .forEach(grid::add)

        toOpenList.addListener { change: ListChangeListener.Change<out Pair<Int, Int>> -> with(change) {
            next()
            if (wasAdded()) {
                (from until to).map { list[it] }
                    .mapNotNull { (row, col) -> grid.children.find { tile: Node -> tile is Tile && tile.col == col && tile.row == row } as Tile }
                    .forEach { tile ->
                        tile.openTile()
                        list.remove(tile.row to tile.col)
                    }
            }
        } }
        return grid
    }

    private fun getAllCells() = (1..width)
        .map { row -> (1..width).map { col -> Cell(row, col) } }
        .flatten()

    private fun getMineCounter() = Label().apply {
        textProperty().bind(secureCount.asString())
        textFill = Color.RED
        font = Font.font("Arial", FontWeight.BOLD, null, 20.0)
    }

    private fun listenToHasExploded(gridContainer: StackPane) {
        hasExploded.addListener { _, _, newValue -> if (newValue) setEndPanel(gridContainer, "Game Over!") }
        timerSubscription?.dispose()
    }

    private fun listenToHasWon(gridContainer: StackPane) {
        hasWon.addListener { _, _, newValue -> if (newValue) setEndPanel(gridContainer, "You Won!") }
        timerSubscription?.dispose()
    }

    private fun setEndPanel(gridContainer: StackPane, endPanelText: String) {
        val rect = Rectangle().apply {
            widthProperty().bind(gridContainer.widthProperty())
            heightProperty().bind(gridContainer.heightProperty())
            fill = Color.WHITE
            opacity = 0.7
        }
        val text = Text(endPanelText).apply {
            font = Font.font("Helvetica", FontWeight.BOLD, null, 24.0)
        }
        val textBackground = Rectangle(150.0, 50.0).apply {
            fill = Color.WHITE
            stroke = Color.BLACK
            strokeWidth = 2.0
        }
        gridContainer.children.addAll(rect, textBackground, text)
    }

    private fun getTimer(): Label = Label("0")
}

fun GridPane.add(tile: Tile) = add(tile, tile.row, tile.col)
