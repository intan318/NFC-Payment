package ta.putri.nfc.checkout

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_checkout.*
import org.jetbrains.anko.*
import ta.putri.nfc.R
import ta.putri.nfc.profile.ProfileActivity
import ta.putri.nfc.model.CurrentUser
import ta.putri.nfc.model.APIResponses
import ta.putri.nfc.model.ProductModel
import ta.putri.nfc.utlis.DialogView
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

class CheckoutActivity : AppCompatActivity(), CheckoutView {


    private lateinit var checkoutPresenter: CheckoutPresenter
    private var produks: MutableList<ProductModel> = mutableListOf()
    private var subTotals: MutableList<Int> = mutableListOf()
    private lateinit var checkoutProductAdapter: CheckoutProductAdapter
    private lateinit var dialogView: DialogView

    var sisaSaldo = 0
    var status: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)


        checkoutPresenter = CheckoutPresenter(this, this)
        dialogView = DialogView(this)
        init()


    }

    private fun init() {


        produks = CurrentUser.listProduk
        subTotals = CurrentUser.listSubTotal

        //Log.e("PRODUK", produks[0].jumlah)

        checkoutProductAdapter = CheckoutProductAdapter(produks)

        rcy_checkout.layoutManager = LinearLayoutManager(this)
        rcy_checkout.itemAnimator = DefaultItemAnimator()
        rcy_checkout.adapter = checkoutProductAdapter

        val jumlahBarang = produks.map {
            it.jumlah!!.toInt()
        }.sum()
        val totalHarga = subTotals.map { it }.sum()

        txt_totalJumlah.text = jumlahBarang.toString()
        txt_totalHarga.text = totalHarga.toString()

        val saldoCustomer = CurrentUser.saldo?.toInt()




        btn_pay.setOnClickListener {

            if (saldoCustomer != null) {
                sisaSaldo = if (saldoCustomer >= totalHarga) {
                    status = true
                    saldoCustomer.minus(totalHarga)
                } else {
                    status = false
                    saldoCustomer
                }
            }


            checkoutPresenter.payments(
                produks,
                CurrentUser.id.toString(),
                totalHarga.toString(),
                subTotals.joinToString(","),
                CurrentUser.uid.toString(),
                CurrentUser.saldo.toString(),
                sisaSaldo.toString(),
                status
            )


            /*dialogView.showDialogOnPayment(
            totalHarga.toString(),
            CurrentUser.saldo.toString(),
            sisaSaldo.toString(),
            object : ButtonEventPaymentDialog {
                override fun onClickYa() {

                }

                override fun onClickTidak() {
                }
            })*/
            /*else -> {
            Log.e("SALDO", "gk cukup")
            dialogAlert =
                alert(
                    message = "Saldo anda tidak mencukupi untuk melakukan pembayaran, silahkan isi saldo anda!",
                    title = "Saldo tidak cukup"
                ) {
                    okButton {
                        dialogAlert.dismiss()
                    }
                }.show()
        }*/
        }

        btn_back.setOnClickListener {
            onBackPressed()
        }

        btn_profile.setOnClickListener {
            startActivity(intentFor<ProfileActivity>())
        }
    }

    override fun onLoading() {

        dialogView.showProgressDialog()

    }

    override fun onFinish() {
        dialogView.hideProgressDialog()
    }

    @SuppressLint("SimpleDateFormat")
    override fun getResponses(respon: ConcurrentLinkedQueue<APIResponses>) {

        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        val currentDate = sdf.format(Date())

        if (respon.any { it.error == true }) {
            longToast("Gagal melakukan Transaksi")
        } else {
            longToast("Pemesanan telah di konfirmasi")
            startActivity(
                intentFor<ProfileActivity>(
                    "saldoAkhir" to sisaSaldo.toString(),
                    "tanggal" to currentDate.toString(),
                    "status" to status
                )
            )

            finish()
        }
    }

    override fun error(pesan: String?) {

        longToast(pesan.toString())
    }

    override fun onPause() {
        checkoutPresenter.viewOnDestroy()
        super.onPause()
    }

    override fun onDestroy() {
        checkoutPresenter.viewOnDestroy()
        super.onDestroy()
    }
}
