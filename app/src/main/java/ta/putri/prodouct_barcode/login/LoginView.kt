package ta.putri.prodouct_barcode.login

import ta.putri.prodouct_barcode.model.LoginRespons

interface LoginView {

    fun onLoading()
    fun onFinish()
    fun getResponses(respon : LoginRespons?)
}