package zh.games.minesweeper.ui.config

import javafx.beans.property.DoubleProperty
import javafx.beans.property.IntegerProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty

open class Config {
    val tileSizeProperty: DoubleProperty = SimpleDoubleProperty(32.0)
    val tileMarginProperty: DoubleProperty = SimpleDoubleProperty(1.0)
    val fontSizeProperty: DoubleProperty = SimpleDoubleProperty(16.0)
    val fontFamilyProperty: StringProperty = SimpleStringProperty("Arial")

    val width: IntegerProperty = SimpleIntegerProperty(10)
    val height: IntegerProperty = SimpleIntegerProperty(10)
    val mineLimit: IntegerProperty = SimpleIntegerProperty(10)
}

object ConfigInstance : Config()


