package zh.games.minesweeper.ui.menu.settings

import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.MenuItem
import javafx.scene.control.RadioButton
import javafx.scene.control.TextField
import javafx.scene.control.ToggleGroup
import javafx.scene.layout.GridPane
import javafx.scene.layout.HBox
import javafx.stage.Modality
import javafx.stage.Stage
import zh.games.minesweeper.ui.config.ConfigInstance as Config
import zh.games.minesweeper.ui.menu.settings.DifficultyLevel.EASY

class DifficultyItem : MenuItem("Difficulty") {
    private val widthTf = TextField().apply { prefColumnCount = 3 }
    private val heightTf = TextField().apply { prefColumnCount = 3 }
    private val mineLimitTf = TextField().apply { prefColumnCount = 3 }

    init {
        setLevel(EASY)
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

        setOnAction {
            val grid = GridPane().apply {
                vgap = 10.0
                hgap = 5.0
                padding = Insets(10.0)

                easyButton.toggleGroup = toggleGroup
                addRow(0, easyButton, Label(EASY.text))
                addRow(1, moderateButton, Label(DifficultyLevel.MODERATE.text))
                addRow(2, hardButton, Label(DifficultyLevel.HARD.text))
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

            cancelBtn.setOnAction { stage.hide() }
            okBtn.setOnAction {
                when {
                    easyButton.isSelected -> setLevel(EASY)
                    moderateButton.isSelected -> setLevel(DifficultyLevel.MODERATE)
                    hardButton.isSelected -> setLevel(DifficultyLevel.HARD)
                    else -> setLevel(widthTf.text.toInt(), heightTf.text.toInt(), mineLimitTf.text.toInt())
                }
                stage.hide()
            }
        }
    }

    private fun setLevel(level: DifficultyLevel) = with(level) { setLevel(height, width, mineLimit) }

    private fun setLevel(height: Int, width: Int, mineLimit: Int) = with(Config) {
        this.height.set(height)
        this.width.set(width)
        this.mineLimit.set(mineLimit)
    }
}
