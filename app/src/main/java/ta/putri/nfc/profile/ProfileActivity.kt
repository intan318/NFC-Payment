package ta.putri.nfc.profile

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_profile.*
import org.jetbrains.anko.*
import ta.putri.nfc.R
import ta.putri.nfc.login.LoginActivity
import ta.putri.nfc.model.APIResponses
import ta.putri.nfc.model.CurrentUser
import ta.putri.nfc.model.CustomerModel
import ta.putri.nfc.model.TransactionModel
import ta.putri.nfc.utlis.ButtonEventConfirmationEditDialogListener
import ta.putri.nfc.utlis.DialogView
import ta.putri.nfc.utlis.SessionManager

class ProfileActivity : AppCompatActivity(), ProfileView {


    private lateinit var customerModel: CustomerModel
    private var transactions: MutableList<TransactionModel> = mutableListOf()

    private lateinit var sessionManager: SessionManager
    private lateinit var dialogView: DialogView
    private lateinit var profilePresenter: ProfilePresenter
    private lateinit var dialogAlert: DialogInterface

    private lateinit var transactionAdapter: TransactionAdapter
    private lateinit var lastActivityAdapter: LastActivityAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        customerModel = CustomerModel()
        profilePresenter = ProfilePresenter(this)
        dialogView = DialogView(this)
        sessionManager = SessionManager(this)


        transactionAdapter = TransactionAdapter(transactions) { transaksi ->

            dialogAlert =
                alert(
                    message = "Anda yakin mau menghapus data transaksi ini?",
                    title = "Hapus Transaksi"
                ) {
                    okButton {
                        profilePresenter.deleteTransaction(transaksi.id.toString(), this@ProfileActivity)
                    }
                    cancelButton { dialogAlert.dismiss() }
                }.show()

        }
        lastActivityAdapter = LastActivityAdapter(CurrentUser.listProduk)



        init()
        profilePresenter.getProfile(CurrentUser.id!!, this)

    }

    private fun init() {


        btn_back.setOnClickListener {
            onBackPressed()
        }

        edt_nama_customer.setOnClickListener {
            dialogView.showDialogEditNama(
                customerModel.nama.toString(),
                object : ButtonEventConfirmationEditDialogListener {
                    override fun onClickYa(nama: String) {
                        profilePresenter.editNama(nama, customerModel.id.toString(), this@ProfileActivity)
                    }

                    override fun onClickTidak() {
                        toast("Batal mengubah nama")
                    }
                })
        }


        btn_logout.setOnClickListener {
            dialogAlert =
                alert(
                    message = "Anda yakin ingin keluar akun ?",
                    title = "Log out"
                ) {
                    okButton {
                        sessionManager.setLogin(false, "")
                        finish()
                        startActivity(intentFor<LoginActivity>())

                    }
                    cancelButton { dialogAlert.dismiss() }
                }.show()
        }


        rcy_transaksi.layoutManager = LinearLayoutManager(this)
        rcy_transaksi.setHasFixedSize(true)
        rcy_transaksi.itemAnimator = DefaultItemAnimator()
        rcy_transaksi.adapter = transactionAdapter

        rcy_lastTransaction.layoutManager = LinearLayoutManager(this)
        rcy_lastTransaction.setHasFixedSize(true)
        rcy_lastTransaction.itemAnimator = DefaultItemAnimator()
        rcy_lastTransaction.adapter = lastActivityAdapter


        val bundle = intent.extras

        if (bundle != null) {
            val jumlahBarang = CurrentUser.listProduk.map {
                it.jumlah!!.toInt()
            }.sum()
            val totalHarga = CurrentUser.listSubTotal.map { it }.sum()

            val status = intent.getBooleanExtra("status", false)

            val currentStatus = if (status) {
                "Berhasil"
            } else {
                "Saldo tidak cukup"
            }


            txt_totalJumlah.text = jumlahBarang.toString()
            txt_totalHarga.text = totalHarga.toString()
            txt_saldoawal.text = CurrentUser.saldo
            txt_saldoakhir.text = intent.getStringExtra("saldoAkhir")
            txt_tanggal.text = intent.getStringExtra("tanggal")

        } else {
            card_lastActivity_span.visibility = View.GONE
            card_lastActivity.visibility = View.GONE
        }
    }


    override fun onLoading() {

        dialogView.showProgressDialog()
    }

    override fun onFinish() {

        dialogView.hideProgressDialog()
    }

    override fun getResponses(respon: CustomerModel?, transaksi: List<TransactionModel>?) {

        customerModel = respon!!
        transactions.addAll(transaksi!!)
        transactionAdapter.notifyDataSetChanged()

        txt_nama.text = customerModel.nama
        txt_email.text = customerModel.email
        txt_daftar.text = customerModel.createdAt
        txt_saldo.text = customerModel.saldo


        if (transactions.size == 0 || transaksi.isEmpty()) {
            rcy_transaksi.visibility = View.GONE
            ll_empty.visibility = View.VISIBLE
        } else {
            rcy_transaksi.visibility = View.VISIBLE
            ll_empty.visibility = View.GONE
        }

    }

    override fun getChangeNameResponse(respon: APIResponses?) {
        if (!respon?.error!!) {
            toast("Nama berhasil di ubah")
            finish()
            startActivity(intent)
        } else {
            toast("Nama gagal di ubah")
        }
    }

    override fun error(pesan: String?) {

        toast(pesan.toString())
    }

    override fun onDestroy() {
        profilePresenter.viewOnDestroy()
        super.onDestroy()
    }

    override fun onPause() {
        profilePresenter.viewOnDestroy()
        super.onPause()
    }

}
