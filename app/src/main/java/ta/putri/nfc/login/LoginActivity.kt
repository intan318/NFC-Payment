package ta.putri.nfc.login

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.*
import ta.putri.nfc.AboutActivity
import ta.putri.nfc.customer.MainActivity
import ta.putri.nfc.R
import ta.putri.nfc.model.CurrentUser
import ta.putri.nfc.model.LoginRespons
import ta.putri.nfc.register.RegisterActivity
import ta.putri.nfc.utlis.DialogView
import ta.putri.nfc.utlis.SessionManager

class LoginActivity : AppCompatActivity(), LoginView {


    private lateinit var loginPresenter: LoginPresenter
    private lateinit var session: SessionManager

    private lateinit var dialogView: DialogView
    private lateinit var dialogAlert: DialogInterface

    private var succes = false
    private var message = ""
    private var user_id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        session = SessionManager(this)
        loginPresenter = LoginPresenter(this, this)
        dialogView  = DialogView(this)

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

        btn_aboutUs.setOnClickListener {
            startActivity(intentFor<AboutActivity>())
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
            } else {
                toast("Harap di isi semua !")
            }
        }
    }

    private fun onLogin(){
        session.setLogin(true, user_id)
        startActivity(intentFor<MainActivity>())
    }

    override fun onLoading() {
        dialogView.showProgressDialog()
    }

    override fun onFinish() {
        dialogView.hideProgressDialog()
        if (succes) {
            onLogin()
        }
    }

    override fun getResponses(respon: LoginRespons?) {

        if (respon?.error!!) {

            dialogAlert =
                alert(
                    message = respon.message.toString(),
                    title = "Login gagal"
                ) {
                    okButton {
                        dialogAlert.dismiss()
                    }
                }.show()

        } else {
            user_id = respon.id!!
            succes = !respon.error!!
        }

    }
}
