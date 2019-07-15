package ta.putri.nfc.utlis

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class SessionManager(private var context: Context) {

    // Shared Preferences
    private var pref: SharedPreferences

    private var editor: SharedPreferences.Editor

    // Shared pref mode
    private var privateMode = 0

    val isLoggedIn: Boolean
        get() = pref.getBoolean(KEY_IS_LOGGED, false)

    init {
        pref = context.getSharedPreferences(PREF_NAME, privateMode)
        editor = pref.edit()
    }

    fun setLogin(isLoggedIn: Boolean, id: String) {

        editor.putBoolean(KEY_IS_LOGGED, isLoggedIn)
        editor.putString(ID_USER, id)

        // commit changes
        editor.commit()
        Log.d(TAG, "User LoginActivity session modified!")
    }

    fun getID(): String? {
        return when {
            isLoggedIn -> pref.getString(ID_USER, "1")
            else -> ""
        }
    }

    companion object {
        // LogCat tag
        private val TAG = SessionManager::class.java.simpleName

        // Shared preferences file name
        private const val PREF_NAME = "UserPaymentLogIn"
        private const val KEY_IS_LOGGED = "isLoggedIn"
        private const val ID_USER = "User_id"
    }
}