package ta.putri.prodouct_barcode.utlis

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.view.LayoutInflater
import android.view.Window
import kotlinx.android.synthetic.main.add_product_layout.*
import kotlinx.android.synthetic.main.add_product_layout.view.*
import kotlinx.android.synthetic.main.add_product_layout.view.btn_cancel
import kotlinx.android.synthetic.main.add_product_layout.view.jumlah_barang
import kotlinx.android.synthetic.main.add_product_layout.view.nama_barang
import kotlinx.android.synthetic.main.dialog_payment_layout.view.*
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

    fun showAddProductDialog(
        namaBarang: String,
        hargaBarang: String,
        jumlah: String,
        eventConfirmation: ButtonEventConfirmationDialogListener
    ) {

        val factory = LayoutInflater.from(activity)
        val addProductDialogView = factory.inflate(R.layout.add_product_layout, null)
        val addProductDialog = AlertDialog.Builder(activity).create()

        addProductDialogView.nama_barang.text = namaBarang
        addProductDialogView.harga_barang.text = hargaBarang
        addProductDialogView.jumlah_barang.setText(jumlah)


        var jumlahBarang = 0

        addProductDialogView.btn_minus.setOnClickListener {
            jumlahBarang--
            if (jumlahBarang <= 0) {
                addProductDialogView.jumlah_barang.setText(1.toString())
            } else {
                addProductDialogView.jumlah_barang.setText(jumlahBarang.toString())
            }

        }

        addProductDialogView.btn_plus.setOnClickListener {
            jumlahBarang++
            addProductDialogView.jumlah_barang.setText(jumlahBarang.toString())
        }


        addProductDialog.setCanceledOnTouchOutside(false)


        addProductDialogView.btn_save.setOnClickListener {
            eventConfirmation.onClickYa(addProductDialog.jumlah_barang.text.toString().toInt())
            addProductDialog.dismiss()
        }

        addProductDialogView.btn_cancel.setOnClickListener {
            eventConfirmation.onClickTidak()
            addProductDialog.dismiss()
        }

        addProductDialog.setView(addProductDialogView)
        addProductDialog.show()
    }

    fun showDialogOnPayment(totalHarga : String, saldoCustomer : String, saldoAkhir : String, event: ButtonEventPaymentDialog){

        val factory = LayoutInflater.from(activity)
        val paymentDialogView = factory.inflate(R.layout.add_product_layout, null)
        val paymentDialog = AlertDialog.Builder(activity).create()

        paymentDialogView.txt_old_saldo.text = saldoCustomer
        paymentDialogView.txt_new_saldo.text = saldoAkhir
        paymentDialogView.txt_total_harga.text = totalHarga

        paymentDialogView.btn_paid.setOnClickListener {
            event.onClickYa()
            paymentDialog.dismiss()
        }

        paymentDialogView.btn_cancel.setOnClickListener {
            event.onClickTidak()
            paymentDialog.dismiss()
        }

        paymentDialog.setView(paymentDialogView)
        paymentDialog.show()
    }
}