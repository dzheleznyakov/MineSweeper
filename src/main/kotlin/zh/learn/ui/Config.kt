package zh.learn.ui

import javafx.beans.property.DoubleProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty

open class Config {
    val tileSizeProperty: DoubleProperty = SimpleDoubleProperty(32.0)
    val tileMarginProperty: DoubleProperty = SimpleDoubleProperty(1.0)
    val fontSizeProperty: DoubleProperty = SimpleDoubleProperty(16.0)
    val fontFamilyProperty: StringProperty = SimpleStringProperty("Arial")
}

object ConfigInstance : Config()


