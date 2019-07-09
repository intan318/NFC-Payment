package ta.putri.prodouct_barcode.customer.checkout

import android.content.Context
import android.util.Log
import kotlinx.coroutines.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import org.jetbrains.anko.uiThread
import ta.putri.prodouct_barcode.model.ProductModel
import ta.putri.prodouct_barcode.repository.ApiFactory

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

        val idProduks = produks.map { it.id }.joinToString(",")
        val jumlahPerProduk = produks.map { it.jumlah }.joinToString(",")

        doAsync {
            job = GlobalScope.launch(Dispatchers.Default) {

                try {
                    for (produk in produks) {

                        val data = service.triggerProduk(
                            customer_id,
                            produk.id.toString(),
                            produk.jumlah.toString()
                        )
                        val result = data.await()
                        Log.e("trigger", result.body()?.message)
                    }

                    val transactionData = service.inputTransaksi(
                        customer_id,
                        idProduks, jumlahPerProduk, subTotals, totalHarga
                    )
                    val profileUpdate = service.updateSaldoCustomer(saldoBaru)
                    val profileResult = profileUpdate.await()
                    val resultTransaction = transactionData.await()

                    withContext(Dispatchers.Main) {
                        uiThread {

                            context.toast("update saldo" + profileResult.body()?.error.toString())
                            checkoutView?.getResponses(resultTransaction.body())
                            checkoutView?.onFinish()
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        uiThread {
                            checkoutView?.onFinish()
                            checkoutView?.error(e.localizedMessage.toString())
                        }
                    }
                }
            }
        }

    }

    fun viewOnDestroy() {
        job?.cancel()
        checkoutView = null
    }

}