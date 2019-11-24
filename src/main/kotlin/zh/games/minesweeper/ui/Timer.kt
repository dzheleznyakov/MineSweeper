package zh.games.minesweeper.ui

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.rxjavafx.observables.JavaFxObservable
import io.reactivex.rxjavafx.schedulers.JavaFxScheduler
import io.reactivex.rxjavafx.sources.Change
import javafx.beans.property.LongProperty
import javafx.beans.property.SimpleLongProperty
import javafx.geometry.Insets
import javafx.scene.control.Label
import javafx.scene.layout.StackPane
import java.util.concurrent.TimeUnit

class Timer : StackPane() {
    private var time: LongProperty = SimpleLongProperty(0)
    private var timerSubscription: Disposable? = null
    private val timer = Observable.interval(1, TimeUnit.SECONDS)

    init {
        val timerLbl = Label().apply {
            padding = Insets(5.0)
            style = "-fx-border-color: black; -fx-border-width: 1px"
            text = formatTime(0)
        }
        JavaFxObservable.changesOf(time)
            .observeOn(JavaFxScheduler.platform())
            .subscribe { change -> timerLbl.text = formatTime(change.newVal as Long) }
        children.add(timerLbl)
    }

    private fun formatTime(totalSeconds: Long): String {
        val seconds = totalSeconds % 60
        val minutes = totalSeconds / 60

        val secondsString = if (seconds < 10) "0$seconds" else "$seconds"
        val minutesString = if (minutes < 10) "0$minutes" else "$minutes"
        return "$minutesString:$secondsString"
    }

    fun start() {
        time.set(0)
        timerSubscription = timer.observeOn(JavaFxScheduler.platform())
            .subscribe { t -> time.set(t) }
    }

    fun stop() {
        timerSubscription?.dispose()
        timerSubscription = null
    }
}
