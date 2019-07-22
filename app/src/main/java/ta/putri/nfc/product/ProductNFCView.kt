package ta.putri.nfc.product

import ta.putri.nfc.model.ProductModel

interface ProductNFCView {

    fun onLoading()
    fun onFinish()
    fun getResponses(respon : ProductModel?)
    fun error(pesan : String?)
}