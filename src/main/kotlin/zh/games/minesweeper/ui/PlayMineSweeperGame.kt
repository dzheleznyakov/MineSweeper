package zh.games.minesweeper.ui

import javafx.application.Application
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
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
import zh.games.minesweeper.game.Game
import zh.games.minesweeper.game.RandomMineSweeperGameInitializer
import zh.games.minesweeper.game.newMineSweeperGame

class PlayMineSweeperGame: Application() {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(PlayMineSweeperGame::class.java)
        }
    }

    private val height = 16
    private val width = 30
    private val mineLimit = 99
    private var game: Game = newMineSweeperGame(height, width, mineLimit, RandomMineSweeperGameInitializer)
    private val hasExploded = SimpleBooleanProperty(false)
    private val hasWon = SimpleBooleanProperty(false)
    private val secureCount = SimpleIntegerProperty(mineLimit)
    private val gameStarted = SimpleBooleanProperty(false)

    private val backdrop = StackPane()
    private val gridContainer = StackPane()
    private var grid = Grid(hasExploded, hasWon, secureCount, height, width)

    private val timer: Timer = Timer()
    private val startBtn: Button = Button("Start")

    override fun init() {
        game.initialize()
    }

    override fun start(stage: Stage) {
        val mineCounter = getMineCounter()
        val statContainer = HBox(10.0, mineCounter, startBtn, timer)
            .apply { alignment = Pos.CENTER }

        grid.setGame(game)
        gridContainer.children.add(grid)
        gridContainer.setOnMouseClicked { _ -> if (!gameStarted.get()) gameStarted.set(true) }

        val root = BorderPane().apply {
            top = statContainer
            center = gridContainer
        }
        BorderPane.setAlignment(mineCounter, Pos.CENTER)
        BorderPane.setMargin(mineCounter, Insets(8.0))

        listenToHasExploded()
        listenToHasWon()
        listenToGameStarted()
        listenToStartButtonHit()

        with(stage) {
            scene = Scene(root)
            title = "Mine Sweeper"
            show()
        }
    }

    private fun getMineCounter() = Label().apply {
        textProperty().bind(secureCount.asString())
        textFill = Color.RED
        font = Font.font("Arial", FontWeight.BOLD, null, 20.0)
    }

    private fun listenToHasExploded() {
        hasExploded.addListener { _, _, newValue ->
            if (newValue) setEndPanel(gridContainer, "Game Over!")
            gameStarted.set(false)
        }
    }

    private fun listenToHasWon() {
        hasWon.addListener { _, _, newValue ->
            if (newValue) setEndPanel(gridContainer, "You Won!")
            gameStarted.set(false)
        }
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
        backdrop.children.setAll(rect, textBackground, text)
        gridContainer.children.addAll(backdrop)
    }

    private fun listenToGameStarted() {
        gameStarted.addListener { _, _, newValue ->
            if (newValue && !hasWon.get() && !hasExploded.get()) timer.start()
            else timer.stop()
        }
    }

    private fun listenToStartButtonHit() {
        startBtn.setOnAction {
            hasWon.set(false)
            hasExploded.set(false)
            game = newMineSweeperGame(height, width, mineLimit, RandomMineSweeperGameInitializer)
                .apply { initialize() }
            secureCount.set(mineLimit)
            gameStarted.set(false)
            timer.reset()
            grid = Grid(hasExploded, hasWon, secureCount, height, width)
            grid.setGame(game)
            gridContainer.children.clear()
            gridContainer.children.add(grid)
        }
    }
}

fun GridPane.add(tile: Tile) = add(tile, tile.col, tile.row)
