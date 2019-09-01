package org.mhacks.app.welcome

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import org.mhacks.app.core.data.model.Outcome
import org.mhacks.app.core.data.model.RetrofitException
import org.mhacks.app.core.data.model.Text
import org.mhacks.app.welcome.data.model.Configuration
import org.mhacks.app.welcome.usecase.GetAndCacheConfigUseCase
import org.threeten.bp.*
import timber.log.Timber
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import org.mhacks.app.core.R as coreR

class WelcomeViewModel @Inject constructor(
        private val getAndCacheConfigUseCase: GetAndCacheConfigUseCase): ViewModel() {

    private val getAndCacheConfigResult = getAndCacheConfigUseCase.observe()

    private val _config = MediatorLiveData<Configuration>()

    val config: LiveData<Configuration> = _config

    private val _timerText = MediatorLiveData<Text>()

    val timerText: LiveData<Text>
        get() = _timerText

    private val _timerProgress = MediatorLiveData<Int>()

    val timerProgress: LiveData<Int>
        get() = _timerProgress

    private val _firstTimerProgress = MediatorLiveData<Int>()

    val firstTimerProgress: LiveData<Int>
        get() = _firstTimerProgress

    private val _snackBarMessage = MediatorLiveData<Text>()

    val snackBarMessage: LiveData<Text>
        get() = _snackBarMessage

    private var timer: HackingCountdownTimer? = null

    init {
        _config.addSource(getAndCacheConfigResult) {
            if (it is Outcome.Success) {
                Timber.d("Config Success")

                val startDateTs = 1539403200000L
                val endDateTs = 1539532800000L

                initCountdownIfNecessary(startDateTs, endDateTs)

            } else if (it is Outcome.Error<*>) {
                Timber.d("Config Failure")
                (it.exception as? RetrofitException)?.let { retrofitException ->
                    when (retrofitException.kind) {
                        RetrofitException.Kind.HTTP -> {
                            retrofitException.errorResponse?.let { errorResponse ->
                                _snackBarMessage.value =
                                        Text.TextString(errorResponse.message)
                            }
                        }
                        RetrofitException.Kind.NETWORK -> {
                            _snackBarMessage.value =
                                    Text.Res(R.string.welcome_network_failure)

                        }
                        RetrofitException.Kind.UNEXPECTED -> {
                            _snackBarMessage.value =
                                    Text.Res(coreR.string.unknown_error)
                        }
                        RetrofitException.Kind.UNAUTHORIZED -> {
                            _snackBarMessage.value =
                                    Text.Res(coreR.string.unauthorized_error)
                        }
                    }
                }
            }
        }
    }

    fun getAndCacheConfig() {
        _timerText.value = Text.Res(R.string.timer_placeholder)
        getAndCacheConfigUseCase.execute(Unit)
    }

    private fun initCountdownIfNecessary(startDateTs: Long, endDateTs: Long) {
        val curTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)
        val startTime = startDateTs
        val endTime = endDateTs

        when {
            curTime < startTime ->
                _timerText.value  = Text.Res(R.string.countdown_timer_default)
            curTime < endTime -> {
                // Calculate the time remaining and the total time of hacking
                val timeRemaining = endTime - curTime
                val totalHackingTime = endTime - startTime

                // Start the countdown timer
                timer = HackingCountdownTimer(timeRemaining, totalHackingTime)
                timer?.start()
            }
            else -> {
                _firstTimerProgress.value = 100
                _timerText.value = Text.Res(coreR.string.done)
            }
        }
    }

    /**
     * @param[totalHackingTimeInMillis]
     *
     * Total amount of hacking time in milliseconds.
     **/
    private inner class HackingCountdownTimer(
            millisInFuture: Long, internal var totalHackingTimeInMillis: Long)
        : CountDownTimer(millisInFuture, countdownUpdateIntervals) {
        internal var outFormat: DateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

        init {
            outFormat.timeZone = TimeZone.getTimeZone("UTC")
        }
        override fun onTick(millisUntilFinished: Long) {
            val hours = millisUntilFinished / 3600000
            val minutes = (millisUntilFinished - hours * 3600000) / 60000
            val seconds = millisUntilFinished - hours * 3600000 - minutes * 60000

            /**
             * In the case of there being a single digit during the countdown.
             *     e.g. 12:9:01
             *
             * This code would pad the text so it would be 12:09:01
             */
            val hrs = if (hours < 10) "0" + hours.toString() else hours.toString()
            val min = if (minutes < 10) "0" + minutes.toString() else minutes.toString()
            val sec = (if (seconds < 10) "0" + seconds.toString() else seconds.toString())
                    .substring(0, 2)

            val formattedDate = String.format("%s:%s:%s", hrs, min, sec)
            _timerText.value = Text.TextString(formattedDate)

            val progress = (100 - (100 * millisUntilFinished) / totalHackingTimeInMillis).toInt()
            _timerProgress.value = progress
        }

        override fun onFinish() {
            _timerProgress.value = 100
            _timerText.value = Text.Res(coreR.string.done)
        }
    }


    override fun onCleared() {
        super.onCleared()
        getAndCacheConfigUseCase.onCleared()
    }

    companion object {

        private const val countdownUpdateIntervals = 750L

    }

}