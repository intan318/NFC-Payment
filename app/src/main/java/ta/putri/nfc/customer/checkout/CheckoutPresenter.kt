package ta.putri.nfc.customer.checkout

import android.content.Context
import kotlinx.coroutines.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import ta.putri.nfc.model.PostResponses
import ta.putri.nfc.model.ProductModel
import ta.putri.nfc.repository.ApiFactory
import java.util.concurrent.ConcurrentLinkedQueue

class CheckoutPresenter(private var checkoutView: CheckoutView?, private val context: Context) {


    private val service = ApiFactory.makeRetrofitService()
    var job: Job? = null

    fun payments(
        produks: List<ProductModel>,
        customer_id: String,
        totalHarga: String,
        subTotals: String,
        saldoBaru: String
    ) {

        checkoutView?.onLoading()

        doAsync {
            val lstOfReturnData = ConcurrentLinkedQueue<PostResponses>()
            val idProduks = produks.map { it.nama }.joinToString(",")
            val jumlahPerProduk = produks.map { it.jumlah }.joinToString(",")
            try {
                job = runBlocking {
                    produks.forEach {
                        launch(Dispatchers.IO) {
                            val data = service.triggerProduk(
                                customer_id,
                                it.id.toString(),
                                it.jumlah.toString()
                            )
                            val result = data.await()
                            lstOfReturnData.add(result.body())
                        }
                    }

                    launch(Dispatchers.IO) {
                        val transactionData = service.inputTransaksi(
                            customer_id,
                            idProduks, jumlahPerProduk, subTotals, totalHarga
                        )

                        val resultTransaction = transactionData.await()
                        lstOfReturnData.add(resultTransaction.body())
                    }
                }

                uiThread {
                    checkoutView?.getResponses(lstOfReturnData)
                    checkoutView?.onFinish()
                }
            } catch (e: Exception) {

                uiThread {
                    checkoutView?.error(e.toString())
                    checkoutView?.onFinish()
                }
            }
        }

    }

    fun viewOnDestroy() {
        job?.cancel()
        checkoutView = null
    }

}