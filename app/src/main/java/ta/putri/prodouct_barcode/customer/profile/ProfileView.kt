package ta.putri.prodouct_barcode.customer.profile

import ta.putri.prodouct_barcode.model.CustomerModel
import ta.putri.prodouct_barcode.model.TransactionModel

interface ProfileView {

    fun onLoading()
    fun onFinish()
    fun getResponses(respon :CustomerModel?, transaksi : List<TransactionModel>?)
    fun error(pesan : String?)
}