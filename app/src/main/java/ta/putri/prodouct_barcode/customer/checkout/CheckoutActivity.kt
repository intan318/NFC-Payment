package ta.putri.prodouct_barcode.customer.checkout

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.card_transaksi.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.cancelButton
import org.jetbrains.anko.longToast
import org.jetbrains.anko.okButton
import ta.putri.prodouct_barcode.R
import ta.putri.prodouct_barcode.model.CurrentUser
import ta.putri.prodouct_barcode.model.CustomerModel
import ta.putri.prodouct_barcode.model.PostResponses
import ta.putri.prodouct_barcode.model.ProductModel
import ta.putri.prodouct_barcode.utlis.ButtonEventPaymentDialog
import ta.putri.prodouct_barcode.utlis.DialogView

class CheckoutActivity : AppCompatActivity(), CheckoutView {


    private lateinit var checkoutPresenter: CheckoutPresenter
    private var produks: MutableList<ProductModel> = mutableListOf()
    private var subTotals: MutableList<Int> = mutableListOf()
    private lateinit var checkoutProductAdapter: CheckoutProductAdapter
    private lateinit var dialogView: DialogView
    private lateinit var dialogAlert: DialogInterface


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)


        checkoutPresenter = CheckoutPresenter(this)
        dialogView = DialogView(this)

        if (intent != null) {
            produks = intent.getParcelableArrayListExtra("produks")
            subTotals = intent.getIntegerArrayListExtra("subtotals")
        }

        checkoutProductAdapter = CheckoutProductAdapter(produks)

        val jumlahBarang = produks.map { it.jumlah!!.toInt() }.sum()
        val totalHarga = subTotals.map { it }.sum()

        txt_jumlahBarang.text = jumlahBarang.toString()
        txt_totalHarga.text = totalHarga.toString()

        btn_pay.setOnClickListener {

            val sisaSaldo = CurrentUser.saldo?.toInt()?.minus(totalHarga)
            if (sisaSaldo != null) {
                if (sisaSaldo >= 0)

                    dialogView.showDialogOnPayment(
                        totalHarga.toString(),
                        CurrentUser.saldo.toString(),
                        sisaSaldo.toString(),
                        object : ButtonEventPaymentDialog {
                            override fun onClickYa() {

                                checkoutPresenter.payments()
                            }

                            override fun onClickTidak() {

                            }

                        })
            } else {

                longToast("Saldo anda tidak mencukupi untuk melakukan pembayaran, silahkan isi saldo anda!")

                dialogAlert =
                    alert(
                        message = "Saldo anda tidak mencukupi untuk melakukan pembayaran, silahkan isi saldo anda!",
                        title = "Saldo tidak cukup"
                    ) {
                        okButton {
                            dialogAlert.dismiss()
                        }
                    }.show()

            }
        }

    }

    override fun onLoading() {

    }

    override fun onFinish() {

    }

    override fun getResponses(respon: PostResponses?) {

    }

    override fun error(pesan: String?) {

    }
}
