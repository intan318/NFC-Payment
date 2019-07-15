package ta.putri.nfc.customer

import ta.putri.nfc.model.ProductModel

interface NFCView {

    fun onLoading()
    fun onFinish()
    fun getResponses(respon : ProductModel?)
    fun error(pesan : String?)
}