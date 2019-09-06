package org.mhacks.ratingmanager.rate

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import androidx.annotation.StyleRes
import org.mhacks.ratingmanager.rate.DialogManager.create
import org.mhacks.ratingmanager.rate.PreferenceHelper.getInstallDate
import org.mhacks.ratingmanager.rate.PreferenceHelper.getIsAgreeShowDialog
import org.mhacks.ratingmanager.rate.PreferenceHelper.getLaunchTimes
import org.mhacks.ratingmanager.rate.PreferenceHelper.getRemindInterval
import org.mhacks.ratingmanager.rate.PreferenceHelper.isFirstLaunch
import org.mhacks.ratingmanager.rate.PreferenceHelper.setInstallDate
import java.util.*

class RatingManager private constructor(context: Context) {

    private val context: Context = context.applicationContext

    private val options = DialogOptions()

    private var installDate = 10

    private var launchTimes = 10

    private var remindInterval = 1

    private var isDebug = false

    private val isOverLaunchTimes: Boolean
        get() = getLaunchTimes(context) >= launchTimes

    private val isOverInstallDate: Boolean
        get() = isOverDate(getInstallDate(context), installDate)

    private val isOverRemindDate: Boolean
        get() = isOverDate(getRemindInterval(context), remindInterval)

    /** The Rate Dialog theme resource ID. */
    @StyleRes private var themeResId: Int? = null

    fun setThemeResId(@StyleRes themeResId: Int): RatingManager {
        this.themeResId = themeResId
        return this
    }

    fun setLaunchTimes(launchTimes: Int): RatingManager {
        this.launchTimes = launchTimes
        return this
    }

    fun setInstallDays(installDate: Int): RatingManager {
        this.installDate = installDate
        return this
    }

    fun setRemindInterval(remindInterval: Int): RatingManager {
        this.remindInterval = remindInterval
        return this
    }

    fun setShowLaterButton(isShowNeutralButton: Boolean): RatingManager {
        options.setShowNeutralButton(isShowNeutralButton)
        return this
    }

    fun setShowNeverButton(isShowNeverButton: Boolean): RatingManager {
        options.setShowNegativeButton(isShowNeverButton)
        return this
    }

    fun setShowTitle(isShowTitle: Boolean): RatingManager {
        options.setShowTitle(isShowTitle)
        return this
    }

    fun clearAgreeShowDialog(): RatingManager {
        PreferenceHelper.setAgreeShowDialog(context, true)
        return this
    }

    fun clearSettingsParam(): RatingManager {
        PreferenceHelper.setAgreeShowDialog(context, true)
        PreferenceHelper.clearSharedPreferences(context)
        return this
    }

    fun setAgreeShowDialog(clear: Boolean): RatingManager {
        PreferenceHelper.setAgreeShowDialog(context, clear)
        return this
    }

    fun setView(view: View): RatingManager {
        options.view = view
        return this
    }

    fun setOnClickButtonListener(listener: OnClickButtonListener): RatingManager {
        options.listener = listener
        return this
    }

    fun setTitle(resourceId: Int): RatingManager {
        options.titleResId = resourceId
        return this
    }

    fun setTitle(title: String): RatingManager {
        options.setTitleText(title)
        return this
    }

    fun setMessage(resourceId: Int): RatingManager {
        options.messageResId = resourceId
        return this
    }

    fun setMessage(message: String): RatingManager {
        options.setMessageText(message)
        return this
    }

    fun setTextRateNow(resourceId: Int): RatingManager {
        options.textPositiveResId = resourceId
        return this
    }

    fun setTextRateNow(positiveText: String): RatingManager {
        options.setPositiveText(positiveText)
        return this
    }

    fun setTextLater(resourceId: Int): RatingManager {
        options.textNeutralResId = resourceId
        return this
    }

    fun setTextLater(neutralText: String): RatingManager {
        options.setNeutralText(neutralText)
        return this
    }

    fun setTextNever(resourceId: Int): RatingManager {
        options.textNegativeResId = resourceId
        return this
    }

    fun setTextNever(negativeText: String): RatingManager {
        options.setNegativeText(negativeText)
        return this
    }

    fun setCancelable(cancelable: Boolean): RatingManager {
        options.cancelable = cancelable
        return this
    }

    fun setStoreType(appstore: StoreType): RatingManager {
        options.storeType = appstore
        return this
    }

    fun monitor() {
        if (isFirstLaunch(context)) {
            setInstallDate(context)
        }
        PreferenceHelper.setLaunchTimes(context, getLaunchTimes(context) + 1)
    }

    fun showRateDialog(activity: Activity) {
        if (!activity.isFinishing) {
            create(activity, options).show()
        }
    }

    fun shouldShowRateDialog(): Boolean {
        return getIsAgreeShowDialog(context) &&
                isOverLaunchTimes &&
                isOverInstallDate &&
                isOverRemindDate
    }

    fun isDebug(): Boolean {
        return isDebug
    }

    fun setDebug(isDebug: Boolean): RatingManager {
        this.isDebug = isDebug
        return this
    }

    companion object {

        @SuppressLint("StaticFieldLeak")
        private var singleton: RatingManager? = null

        fun with(context: Context): RatingManager {
            if (singleton == null) {
                synchronized(RatingManager::class.java) {
                    if (singleton == null) {
                        singleton = RatingManager(context)
                    }
                }
            }
            return singleton!!
        }

        fun showRateDialogIfMeetsConditions(activity: Activity): Boolean {
            if (singleton == null) return false
            val isMeetsConditions = singleton!!.isDebug || singleton!!.shouldShowRateDialog()
            if (isMeetsConditions) {
                singleton!!.showRateDialog(activity)
            }
            return isMeetsConditions
        }

        private fun isOverDate(targetDate: Long, threshold: Int): Boolean {
            return Date().time - targetDate >= threshold * 24 * 60 * 60 * 1000
        }
    }

}
