package ta.putri.nfc.checkout

import ta.putri.nfc.model.APIResponses
import java.util.concurrent.ConcurrentLinkedQueue

interface CheckoutView {


    fun onLoading()
    fun onFinish()
    fun getResponses(respon: ConcurrentLinkedQueue<APIResponses>)
    fun error(pesan : String?)
}