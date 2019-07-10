package ta.putri.prodouct_barcode.customer.checkout

import ta.putri.prodouct_barcode.model.PostResponses
import java.util.concurrent.ConcurrentLinkedQueue

interface CheckoutView {


    fun onLoading()
    fun onFinish()
    fun getResponses(respon: ConcurrentLinkedQueue<PostResponses>)
    fun error(pesan : String?)
}