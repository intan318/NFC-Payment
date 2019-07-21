package ta.putri.nfc.customer

import kotlinx.coroutines.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import ta.putri.nfc.repository.ApiFactory
import java.lang.NullPointerException

class ProductNFCPresenter {

    private var productNfcView : ProductNFCView? = null

    private val service = ApiFactory.makeRetrofitService()
    var job: Job? = null

    fun retriveProduct(code : String, productNfcView: ProductNFCView?){
        this.productNfcView = productNfcView
        productNfcView?.onLoading()
        doAsync {
            runBlocking {
                job = launch(Dispatchers.IO) {
                    try {
                        val data = service.getProductAsync(code)
                        val result = data.await()
                        uiThread {
                            productNfcView?.getResponses(result.body())
                            productNfcView?.onFinish()
                        }
                    } catch (e: NullPointerException) {
                        productNfcView?.error(e.toString())
                        productNfcView?.onFinish()
                    }
                }
            }
        }
    }

    fun viewOnDestroy() {
        job?.cancel()
        productNfcView = null
    }
}