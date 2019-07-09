package ta.putri.prodouct_barcode.customer

import ta.putri.prodouct_barcode.model.ProductModel

interface NFCView {

    fun onLoading()
    fun onFinish()
    fun getResponses(respon : ProductModel?)
    fun error(pesan : String?)
}