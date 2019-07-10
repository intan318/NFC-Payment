package ta.putri.prodouct_barcode.customer.checkout

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_checkout.*
import org.jetbrains.anko.*
import ta.putri.prodouct_barcode.R
import ta.putri.prodouct_barcode.customer.profile.ProfileActivity
import ta.putri.prodouct_barcode.model.CurrentUser
import ta.putri.prodouct_barcode.model.CustomerModel
import ta.putri.prodouct_barcode.model.PostResponses
import ta.putri.prodouct_barcode.model.ProductModel
import ta.putri.prodouct_barcode.utlis.ButtonEventPaymentDialog
import ta.putri.prodouct_barcode.utlis.DialogView
import java.util.concurrent.ConcurrentLinkedQueue

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


        checkoutPresenter = CheckoutPresenter(this, this)
        dialogView = DialogView(this)
        init()


    }

    private fun init() {


        produks = CurrentUser.listProduk
        subTotals = CurrentUser.listSubTotal

        Log.e("PRODUK", produks[0].jumlah)

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




        btn_pay.setOnClickListener {

            Log.e("SALDO", CurrentUser.saldo)
            val sisaSaldo = CurrentUser.saldo?.toInt()?.minus(totalHarga)

            if (sisaSaldo != null) {
                when {
                    sisaSaldo >= 0 -> dialogView.showDialogOnPayment(
                        totalHarga.toString(),
                        CurrentUser.saldo.toString(),
                        sisaSaldo.toString(),
                        object : ButtonEventPaymentDialog {
                            override fun onClickYa() {

                                checkoutPresenter.payments(
                                    produks,
                                    CurrentUser.id.toString(),
                                    totalHarga.toString(),
                                    subTotals.joinToString(","),
                                    sisaSaldo.toString()
                                )
                            }

                            override fun onClickTidak() {

                            }

                        })
                    else -> {
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
                    }
                }
            }else{
                longToast("Terjadi kesalahan")
            }
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

    override fun getResponses(respon: ConcurrentLinkedQueue<PostResponses>) {

        if (respon.any { it.error == true }) {
            longToast("Gagal melakukan Transaksi")
        } else {
            longToast("Transaksi berhasil")
            startActivity(intentFor<ProfileActivity>())
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
