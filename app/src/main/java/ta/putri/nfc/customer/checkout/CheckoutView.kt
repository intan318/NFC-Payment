package ta.putri.nfc.customer.checkout

import ta.putri.nfc.model.PostResponses
import java.util.concurrent.ConcurrentLinkedQueue

interface CheckoutView {


    fun onLoading()
    fun onFinish()
    fun getResponses(respon: ConcurrentLinkedQueue<PostResponses>)
    fun error(pesan : String?)
}