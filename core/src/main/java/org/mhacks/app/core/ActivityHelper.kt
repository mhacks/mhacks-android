package org.mhacks.app.core

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory

private const val PACKAGE_NAME = "org.mhacks.app"

fun intentTo(addressableActivity: AddressableActivity): Intent {
    return Intent(Intent.ACTION_VIEW).setClassName(
            PACKAGE_NAME,
            addressableActivity.className)
}

abstract class AddressableFragment {
    /**
     * The activity class name.
     */
    abstract val className: String

    fun getFragment(context: Context): Fragment {
        if (!isClassAvailableInProject(className)) {
            throw ClassNotFoundException("Fragment class does not exist")
        }
        val clazz =
                FragmentFactory.loadFragmentClass(context.classLoader, className)
        return clazz.getConstructor().newInstance()
    }

    private fun isClassAvailableInProject(className: String): Boolean {
        return try {
            Class.forName(className)
            true
        } catch (e: ClassNotFoundException) {
            false
        }
    }

}

/**
 * An [android.app.Activity] that can be addressed by an intent.
 */
interface AddressableActivity {
    /**
     * The activity class name.
     */
    val className: String
}

object Activities {

    object SignIn : AddressableActivity {
        override val className = "$PACKAGE_NAME.signin.ui.SignInActivity"
    }

    object QRScan : AddressableActivity {
        override val className = "$PACKAGE_NAME.qrscan.ui.QRScanActivity"
    }
}

/**
 * All addressable fragments.
 *
 * Can contain intent extra names or functions associated with the activity creation.
 */
object Fragments {

    object Ticket : AddressableFragment() {
        override val className = "$PACKAGE_NAME.ticket.TicketDialogFragment"
    }

    object PostAnnouncement : AddressableFragment() {
        override val className = "$PACKAGE_NAME.postannouncement.ui.PostAnnouncementDialogFragment"
    }

}