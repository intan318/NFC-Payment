package ta.putri.nfc.customer.profile

import ta.putri.nfc.model.CustomerModel
import ta.putri.nfc.model.TransactionModel

interface ProfileView {

    fun onLoading()
    fun onFinish()
    fun getResponses(respon :CustomerModel?, transaksi : List<TransactionModel>?)
    fun error(pesan : String?)
}