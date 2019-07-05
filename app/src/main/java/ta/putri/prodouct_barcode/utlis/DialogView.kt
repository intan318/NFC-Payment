package ta.putri.prodouct_barcode.utlis

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.view.LayoutInflater
import android.view.Window
import kotlinx.android.synthetic.main.add_product_layout.view.*
import ta.putri.prodouct_barcode.R

class DialogView(private var activity: Activity) {

    private lateinit var dialog: Dialog
    var status: Boolean = false

    fun showProgressDialog() {

        dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.loading_layout)

        dialog.show()
        status = true
    }

    fun hideProgressDialog() {
        dialog.dismiss()
        status = false
    }

    fun showConfirmationDialog(namaBarang : String, hargaBarang : String, eventConfirmation: ButtonEventConfirmationDialogListener) {

        val factory = LayoutInflater.from(activity)
        val exitDialogView = factory.inflate(R.layout.add_product_layout, null)
        val exitDialog = AlertDialog.Builder(activity).create()

        exitDialogView.nama_barang.text = namaBarang
        exitDialogView.harga_barang.text = hargaBarang

        exitDialogView.btn_save.setOnClickListener {
            eventConfirmation.onClickYa()
            exitDialog.dismiss()
        }

        exitDialogView.btn_cancel.setOnClickListener {
            eventConfirmation.onClickTidak()
            exitDialog.dismiss()
        }

        exitDialog.setView(exitDialogView)
        exitDialog.show()

    }
}