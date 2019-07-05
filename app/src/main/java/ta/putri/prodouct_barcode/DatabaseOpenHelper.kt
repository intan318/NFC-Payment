package ta.putri.prodouct_barcode

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*
import ta.putri.prodouct_barcode.model.TransactionModel
import ta.putri.prodouct_barcode.model.UserModel

class DatabaseOpenHelper(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "pondok_kreatif.db", null, 1) {


    companion object {
        private var instance: DatabaseOpenHelper? = null

        @Synchronized
        fun getInstance(ctx: Context): DatabaseOpenHelper {
            if (instance == null) {
                instance = DatabaseOpenHelper(ctx.applicationContext)
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
            UserModel.USER_EMAIL to TEXT + NOT_NULL,
            UserModel.USER_PASSWORD to TEXT + NOT_NULL,
            UserModel.TGL_DAFTAR to TEXT + NOT_NULL
        )

        db?.createTable(
            TransactionModel.TABLE_TRANSACTION,
            true,
            TransactionModel.ID to INTEGER + PRIMARY_KEY + AUTOINCREMENT,
            TransactionModel.ID_USER to INTEGER + NOT_NULL,
            TransactionModel.NAMA_PER_BARANG to TEXT,
            TransactionModel.JUMLAH_PER_BARANG to TEXT,
            TransactionModel.HARGA_PER_BARANG to TEXT,
            TransactionModel.TANGGAL to TEXT,
            TransactionModel.TOTAL_HARGA to TEXT

        )
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.dropTable(UserModel.TABLE_USER, true)
        db?.dropTable(TransactionModel.TABLE_TRANSACTION, true)
    }
}

val Context.database: DatabaseOpenHelper
    get() = DatabaseOpenHelper.getInstance(applicationContext)