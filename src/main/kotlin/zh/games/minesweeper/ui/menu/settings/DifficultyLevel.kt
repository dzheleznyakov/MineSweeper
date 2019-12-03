package zh.games.minesweeper.ui.menu.settings

enum class DifficultyLevel(
    val text: String,
    val height: Int,
    val width: Int,
    val mineLimit: Int
) {
    EASY("Easy", 10, 10, 10),
    MODERATE("Moderate", 16, 16, 40),
    HARD("Hard", 16, 30, 99);
}
