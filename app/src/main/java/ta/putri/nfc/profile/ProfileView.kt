package ta.putri.nfc.profile

import ta.putri.nfc.model.APIResponses
import ta.putri.nfc.model.CustomerModel
import ta.putri.nfc.model.TransactionModel

interface ProfileView {

    fun onLoading()
    fun onFinish()
    fun getResponses(respon :CustomerModel?, transaksi : List<TransactionModel>?)
    fun getChangeNameResponse(respon : APIResponses?)
    fun error(pesan : String?)
}