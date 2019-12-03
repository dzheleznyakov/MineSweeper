package zh.games.minesweeper.ui

import javafx.beans.property.SimpleIntegerProperty
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.Menu
import javafx.scene.control.MenuItem
import javafx.scene.control.RadioButton
import javafx.scene.control.TextField
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.stage.Modality
import javafx.stage.Stage
import zh.games.minesweeper.ui.DifficultyLevel.EASY
import zh.games.minesweeper.ui.DifficultyLevel.HARD
import zh.games.minesweeper.ui.DifficultyLevel.MODERATE

class SettingsMenu : Menu("Settings") {
    val width = SimpleIntegerProperty()
    val height  = SimpleIntegerProperty()
    val mineLimit = SimpleIntegerProperty()

    private val widthTf = TextField().apply { prefColumnCount = 3 }
    private val heightTf = TextField().apply { prefColumnCount = 3 }
    private val mineLimitTf = TextField().apply { prefColumnCount = 3 }

    init {
        setLevel(EASY)
        val difficultyItem = getDifficultyMenuItem()
        items.addAll(difficultyItem)
    }

    private fun getDifficultyMenuItem(): MenuItem {
        val easyButton = RadioButton().apply { selectedProperty().set(true) }
        val moderateButton = RadioButton()
        val hardButton = RadioButton()
        val customButton = RadioButton()
        val toggleGroup = ToggleGroup().apply {
            easyButton.toggleGroup = this
            moderateButton.toggleGroup = this
            hardButton.toggleGroup = this
            customButton.toggleGroup = this
        }
        val customContainer = HBox(5.0,
            Label("height:"), heightTf,
            Label("width:"), widthTf,
            Label("mine limit"), mineLimitTf)
        customContainer.disableProperty().bind(customButton.selectedProperty().not())

        val okBtn = Button("OK")
        val cancelBtn = Button("Cancel")
        val buttonBox = HBox(5.0, okBtn, cancelBtn).apply {
            alignment = Pos.BOTTOM_RIGHT
        }

        val difficultyItem = MenuItem("Difficulty")
        difficultyItem.setOnAction {
            val grid = GridPane().apply {
                vgap = 10.0
                hgap = 5.0
                padding = Insets(10.0)

                easyButton.toggleGroup = toggleGroup
                addRow(0, easyButton, Label(EASY.text))
                addRow(1, moderateButton, Label(MODERATE.text))
                addRow(2, hardButton, Label(HARD.text))
                addRow(3, customButton, Label("Custom"))
                add(customContainer, 1, 4);
                add(buttonBox, 1, 5)
            }

            val stage = Stage().apply {
                initModality(Modality.APPLICATION_MODAL)
                scene = Scene(grid)
                title = "Choose Difficulty Level"
                show()
            }
            cancelBtn.setOnAction {
                stage.hide()
            }
            okBtn.setOnAction {
                when {
                    easyButton.isSelected -> setLevel(EASY)
                    moderateButton.isSelected -> setLevel(MODERATE)
                    hardButton.isSelected -> setLevel(HARD)
                    else -> {
                        width.set(widthTf.text.toInt())
                        height.set(heightTf.text.toInt())
                        mineLimit.set(mineLimitTf.text.toInt())
                    }
                }
                stage.hide()
            }
        }
        return difficultyItem
    }

    private fun setLevel(level: DifficultyLevel) = with(level) {
        this@SettingsMenu.width.set(width)
        this@SettingsMenu.height.set(height)
        this@SettingsMenu.mineLimit.set(mineLimit)
    }
}
