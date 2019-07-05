package ta.putri.prodouct_barcode.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_register.btn_daftar
import kotlinx.android.synthetic.main.activity_register.edt_email
import kotlinx.android.synthetic.main.activity_register.edt_password
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import ta.putri.prodouct_barcode.MainActivity
import ta.putri.prodouct_barcode.R
import ta.putri.prodouct_barcode.model.CurrentUser
import ta.putri.prodouct_barcode.register.RegisterActivity
import ta.putri.prodouct_barcode.utlis.SessionManager

class LoginActivity : AppCompatActivity(), LoginView {


    private lateinit var loginPresenter: LoginPresenter
    private lateinit var session: SessionManager

    private var succes = false
    private var message = ""
    private var user_id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        session = SessionManager(this)
        loginPresenter = LoginPresenter(this, this)

        when {
            session.isLoggedIn -> {

                CurrentUser.id = session.getID()
                startActivity(
                    intentFor<MainActivity>()
                )
                finish()
            }
        }

        btn_daftar.setOnClickListener {
            startActivity(intentFor<RegisterActivity>())
        }

        handleLogin()
    }

    private fun handleLogin() {

        btn_login.setOnClickListener {
            val email = edt_email.text.toString().trim()
            val pass = edt_password.text.toString().trim()

            Log.e("Email", email)
            Log.e("Pass", pass)

            if (email != "" && pass != "") {
                loginPresenter.login(email, pass)
                if (succes) {
                    session.setLogin(true, user_id)
                    startActivity(intentFor<MainActivity>())
                } else {
                    toast(message)
                }
            } else {
                toast("Harap di isi semua !")
            }
        }
    }

    override fun onLoading() {
    }
    override fun onFinish() {
    }

    override fun getResponses(pesan: String, status: Boolean, id: String) {
        user_id = id
    }
}
