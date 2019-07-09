package ta.putri.prodouct_barcode.customer.checkout

import ta.putri.prodouct_barcode.model.PostResponses

interface CheckoutView {


    fun onLoading()
    fun onFinish()
    fun getResponses(respon : PostResponses?)
    fun error(pesan : String?)
}