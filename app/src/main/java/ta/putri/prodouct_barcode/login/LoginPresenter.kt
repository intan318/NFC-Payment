package ta.putri.prodouct_barcode.login

import android.content.Context
import android.database.sqlite.SQLiteException
import kotlinx.coroutines.selects.select
import org.jetbrains.anko.db.classParser
import org.jetbrains.anko.db.select
import org.jetbrains.anko.toast
import ta.putri.prodouct_barcode.database
import ta.putri.prodouct_barcode.model.CurrentUser
import ta.putri.prodouct_barcode.model.UserModel

class LoginPresenter(val context: Context, val view: LoginView) {

    private var isRegistered = false
    private var isPasswordtrue = false
    private var isLogged = false
    private var message = " "
    private var id = ""

    fun login(email: String, password: String) {

        view.onLoading()
        try {
            context.database.use {
                val result = select(UserModel.TABLE_USER)
                    .whereArgs("(EMAIL = {email})", "email" to email)
                val user = result.parseList(classParser<UserModel>())

                if (user.isNotEmpty()) {
                    isRegistered = true
                    if (password == user[0].password) {
                        isPasswordtrue = true
                        CurrentUser.nama = user[0].nama
                        CurrentUser.email = user[0].email
                        CurrentUser.id = user[0].id.toString()
                        id = user[0].id.toString()
                    }
                }
            }

            when {
                !isRegistered -> message = "Anda Belum mendaftar, klik tombol daftar"
                !isPasswordtrue -> message = "Password anda salah, coba lagi"
                else -> {
                    message = "Berhasil Login !"
                    isLogged = true
                }
            }
            view.getResponses(message, isLogged, id)
            view.onFinish()
        } catch (e: SQLiteException) {

            context.toast(e.localizedMessage)
            view.onFinish()
        }


    }
}