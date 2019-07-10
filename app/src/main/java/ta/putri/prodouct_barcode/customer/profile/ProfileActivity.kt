package ta.putri.prodouct_barcode.customer.profile

import android.content.DialogInterface
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_profile.*
import org.jetbrains.anko.*
import ta.putri.prodouct_barcode.R
import ta.putri.prodouct_barcode.login.LoginActivity
import ta.putri.prodouct_barcode.model.CurrentUser
import ta.putri.prodouct_barcode.model.CustomerModel
import ta.putri.prodouct_barcode.model.TransactionModel
import ta.putri.prodouct_barcode.utlis.DialogView
import ta.putri.prodouct_barcode.utlis.SessionManager

class ProfileActivity : AppCompatActivity(), ProfileView {

    private lateinit var customerModel: CustomerModel
    private var transactions: MutableList<TransactionModel> = mutableListOf()
    private lateinit var sessionManager: SessionManager
    private lateinit var dialogView: DialogView
    private lateinit var profilePresenter: ProfilePresenter
    private lateinit var dialogAlert: DialogInterface
    private lateinit var transactionAdapter: TransactionAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        customerModel = CustomerModel()
        profilePresenter = ProfilePresenter(this)
        dialogView = DialogView(this)
        sessionManager = SessionManager(this)
        transactionAdapter = TransactionAdapter(transactions)

        init()

        profilePresenter.getProfile(CurrentUser.id!!, this)

    }

    private fun init() {


        btn_back.setOnClickListener {
            onBackPressed()
        }

        btn_logout.setOnClickListener {
            dialogAlert =
                alert(
                    message = "Anda yakin ingin keluar akun ?",
                    title = "Log out"
                ) {
                    okButton {
                        sessionManager.setLogin(false, "")
                        startActivity(intentFor<LoginActivity>())
                        finish()
                    }
                    cancelButton { dialogAlert.dismiss() }
                }.show()
        }


        rcy_transaksi.layoutManager = LinearLayoutManager(this)
        rcy_transaksi.setHasFixedSize(true)
        rcy_transaksi.itemAnimator = DefaultItemAnimator()
        rcy_transaksi.adapter = transactionAdapter


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


        if(transactions.size == 0 || transaksi.isEmpty()){
            rcy_transaksi.visibility = View.GONE
            ll_empty.visibility = View.VISIBLE
        }
        else{
            rcy_transaksi.visibility = View.VISIBLE
            ll_empty.visibility = View.GONE
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
