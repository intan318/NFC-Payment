package ta.putri.prodouct_barcode.register

import android.content.Context
import android.database.sqlite.SQLiteException
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import ta.putri.prodouct_barcode.database
import ta.putri.prodouct_barcode.model.UserModel

class RegisterPresenter(val context: Context, val view: RegisterView) {

    fun register(nama: String, email: String, password: String) {

        view.onLoading()

        doAsync {

            try {
                context.database.use {
                    insert(
                        UserModel.TABLE_USER,
                        UserModel.USER_NAME to nama,
                        UserModel.USER_EMAIL to email,
                        UserModel.USER_PASSWORD to password
                    )
                }
                view.getResponses(true)
                view.onFinish()
            } catch (e: SQLiteException) {
                context.toast(e.localizedMessage.toString())
                view.onFinish()
            }

        }

    }
}