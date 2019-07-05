package ta.putri.prodouct_barcode.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.longToast
import org.jetbrains.anko.toast
import ta.putri.prodouct_barcode.R
import ta.putri.prodouct_barcode.login.LoginActivity

class RegisterActivity : AppCompatActivity(), RegisterView {


    private lateinit var registerPresenter: RegisterPresenter
    private var sukses = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

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

            if (nama.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && konfirmasiPassword.isNotEmpty()
            ) {

                if (password == konfirmasiPassword) {
                    registerPresenter.register(nama, email, password)

                } else {
                    toast("Password dan konfirmasi tidak sama !")
                }
            } else {
                toast("Harap semua form di isi !")
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
    }

    override fun onFinish() {
    }
    override fun getResponses(succes: Boolean) {
        sukses = succes

        if (sukses) {
            longToast("Registrasi berhasil!, Silahkan Login dahulu")
            startActivity(intentFor<LoginActivity>("pesan" to "Silahkan Login dahulu"))
        }
    }
}
