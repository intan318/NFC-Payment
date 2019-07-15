package ta.putri.nfc.utlis

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*
import ta.putri.nfc.model.UserModel

class DatabaseOpenHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "pondok_kreatif.db", null, 1) {


    companion object {
        private var instance: DatabaseOpenHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): DatabaseOpenHelper {
            if (instance == null) {
                instance =
                    DatabaseOpenHelper(ctx.applicationContext)
            }
            return instance as DatabaseOpenHelper
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {

        db?.createTable(
            UserModel.TABLE_USER,
            true,
            UserModel.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
            UserModel.USER_NAME to TEXT + NOT_NULL,
            UserModel.USER_EMAIL to TEXT + NOT_NULL + UNIQUE,
            UserModel.USER_PASSWORD to TEXT + NOT_NULL,
            UserModel.TGL_DAFTAR to TEXT + NOT_NULL
        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.dropTable(UserModel.TABLE_USER, true)
    }
}

val Context.database: DatabaseOpenHelper
    get() = DatabaseOpenHelper.getInstance(applicationContext)