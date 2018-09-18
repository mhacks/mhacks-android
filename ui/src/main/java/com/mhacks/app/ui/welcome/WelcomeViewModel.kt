package com.mhacks.app.ui.welcome

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.CountDownTimer
import com.mhacks.app.data.Constants
import com.mhacks.app.data.models.Configuration
import com.mhacks.app.data.models.Result
import com.mhacks.app.data.models.common.TextMessage
import com.mhacks.app.ui.welcome.usecase.GetAndCacheConfigSingleUseCase
import org.mhacks.mhacksui.R
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset
import timber.log.Timber
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class WelcomeViewModel @Inject constructor(
        private val getAndCacheConfigUseCase: GetAndCacheConfigSingleUseCase): ViewModel() {

    private val getAndCacheConfigResult = getAndCacheConfigUseCase.observe()

    private val _config = MediatorLiveData<Configuration>()

    val config: LiveData<Configuration> = _config

    private val _timerText = MediatorLiveData<TextMessage>()

    val timerText: LiveData<TextMessage>
        get() = _timerText

    private val _timerProgress = MediatorLiveData<Int>()

    val timerProgress: LiveData<Int>
        get() = _timerProgress

    private val _snackBarMessage = MediatorLiveData<TextMessage>()

    val snackbarMessage: LiveData<TextMessage>
        get() = _snackBarMessage

    private var timer: HackingCountdownTimer? = null

    init {
        _config.addSource(getAndCacheConfigResult) {
            if (it is Result.Success) {
                Timber.d("Config Success")
                val duration = 129600000L
                val startDate = LocalDateTime.parse(Constants.FIXED_START_DATE)
                        .toEpochSecond(ZoneOffset.UTC)
                initCountdownIfNecessary(startDate, duration)
            } else if (it is Result.Error<*>) {
                Timber.d("Config Failure")

                when (it.kind) {
                    Result.Error.Kind.NETWORK -> {
                        _snackBarMessage.value =
                                TextMessage(R.string.welcome_network_failure, null)
                    }
                    else -> {
                        _snackBarMessage.value =
                                TextMessage(R.string.unknown_error, null)
                    }
                }
            }
        }
    }

    fun getAndCacheConfig() {
        getAndCacheConfigUseCase.execute(Unit)
    }

    private fun initCountdownIfNecessary(startDateTs: Long, endDateTs: Long) {

        val startDate = Instant.ofEpochSecond(startDateTs)
                .atZone(ZoneId.systemDefault()).toLocalDateTime()
        val duration = endDateTs - startDateTs

        val localDateTime = LocalDateTime.now()

        val curTime = localDateTime.toEpochSecond(ZoneOffset.UTC)
        val startTime = startDate!!.toEpochSecond(ZoneOffset.UTC)
        val endTime = startTime + duration


        when {
            curTime < startDate.toEpochSecond(ZoneOffset.UTC) ->
                _timerText.value  = TextMessage(R.string.countdown_timer_default, null)
            curTime < endTime -> {
                // Calculate the time remaining and the total time of hacking
                val timeRemaining = endTime - curTime
                val totalHackingTime = endTime - startTime

                // Start the countdown timer
                timer = HackingCountdownTimer(timeRemaining, totalHackingTime)
                timer?.start()
            }
            else -> {
                _timerProgress.value = 100
                _timerText.value = TextMessage(R.string.done, null)
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
            _timerText.value = TextMessage(null, formattedDate)

            val progress = (100 - (100 * millisUntilFinished) / totalHackingTimeInMillis).toInt()
            _timerProgress.value = progress
        }

        override fun onFinish() {
            _timerProgress.value = 100
            _timerText.value = TextMessage(R.string.done, null)
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