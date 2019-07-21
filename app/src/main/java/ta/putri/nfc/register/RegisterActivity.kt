package ta.putri.nfc.register

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.*
import ta.putri.nfc.R
import ta.putri.nfc.login.LoginActivity
import ta.putri.nfc.utlis.DialogView

class RegisterActivity : AppCompatActivity(), RegisterView {



    private lateinit var registerPresenter: RegisterPresenter
    private var sukses = false
    private lateinit var dialogView: DialogView
    private lateinit var dialogAlert: DialogInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        dialogView  = DialogView(this)
        registerPresenter = RegisterPresenter(this, this)
        handleRegister()

        btn_back.setOnClickListener{
            onBackPressed()
        }
    }


    private fun handleRegister() {

        btn_daftar.setOnClickListener {

            val nama = edt_nama.text.toString().trim()
            val email = edt_email.text.toString().trim()
            val password = edt_password.text.toString().trim()
            val konfirmasiPassword = edt_konfirmasi_password.text.toString().trim()
            val uid = edt_uid.text.toString().trim()

            if (nama.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && konfirmasiPassword.isNotEmpty()
                && uid.isNotEmpty()
            ) {

                if (password == konfirmasiPassword) {
                    registerPresenter.register(nama, email, password, uid)

                } else {
                    toast("Password dan konfirmasi tidak sama !")
                }
            } else {
                toast("Harap semua data di isi !")
            }
        }
        btn_reset.setOnClickListener {
            edt_nama.setText("")
            edt_email.setText("")
            edt_konfirmasi_password.setText("")
            edt_password.setText("")
        }
    }

    override fun onLoading() {

        dialogView.showProgressDialog()
    }

    override fun onFinish() {

        dialogView.hideProgressDialog()
    }
    override fun getResponses(succes: Boolean) {
        sukses = succes

        if (!sukses) {
            dialogAlert =
                alert(
                    message = "Registrasi berhasil!, Silahkan Login dahulu",
                    title = "Berhasil"
                ) {
                    okButton {
                        startActivity(intentFor<LoginActivity>("pesan" to "Silahkan Login dahulu"))
                        finish()
                    }

                    setFinishOnTouchOutside(false)
                }.show()
        }
        else{
            longToast("Email sudah terdaftar, gunakan email lain!")
        }
    }

    override fun error(error: String) {
        longToast(error)
    }
}
