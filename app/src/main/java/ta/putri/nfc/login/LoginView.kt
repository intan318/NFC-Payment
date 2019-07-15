package ta.putri.nfc.login

import ta.putri.nfc.model.LoginRespons

interface LoginView {

    fun onLoading()
    fun onFinish()
    fun getResponses(respon : LoginRespons?)
}