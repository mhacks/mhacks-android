package org.mhacks.app

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory

private const val PACKAGE_NAME = "org.mhacks.app"

fun intentTo(addressableFragment: AddressableFragment): Intent {
    return Intent(Intent.ACTION_VIEW).setClassName(
            PACKAGE_NAME,
            addressableFragment.className)
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
        val clazz = FragmentFactory.loadFragmentClass(
                context.classLoader,
                Fragments.Event.className)

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
 * All addressable fragments.
 *
 * Can contain intent extra names or functions associated with the activity creation.
 */
object Fragments {

    object Event : AddressableFragment() {
        override val className = "$PACKAGE_NAME.events.widget.EventFragment"
    }

}
