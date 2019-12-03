package zh.games.minesweeper.ui.menu.settings

import javafx.scene.control.Menu

class SettingsMenu : Menu("Settings") {

    init {
        val difficultyItem = DifficultyItem()
        items.addAll(difficultyItem)
    }
}
