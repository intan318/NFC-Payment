package ta.putri.nfc.customer.checkout

import android.content.Context
import kotlinx.coroutines.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import ta.putri.nfc.model.CurrentUser
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
        kode_device: String,
        saldoAwal: String,
        saldoAkhir: String,
        status: Boolean
    ) {

        checkoutView?.onLoading()

        val currentStatus = if (status) {
            "Berhasil"
        } else {
            "Saldo tidak cukup"
        }

        doAsync {
            val lstOfReturnData = ConcurrentLinkedQueue<PostResponses>()
            val idProduks = produks.map { it.nama }.joinToString(",")
            val jumlahPerProduk = produks.map { it.jumlah }.joinToString(",")
            try {
                if(status) {
                    job = runBlocking {
                        produks.forEach {
                            launch(Dispatchers.IO) {
                                val data = service.insertProdukTerjual(
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
                                idProduks,
                                jumlahPerProduk,
                                subTotals,
                                totalHarga,
                                kode_device,
                                saldoAwal,
                                saldoAkhir,
                                currentStatus
                            )

                            val resultTransaction = transactionData.await()
                            lstOfReturnData.add(resultTransaction.body())
                        }

                        launch(Dispatchers.IO) {

                            val history = service.insertHistory(
                                kode_device,
                                saldoAwal,
                                saldoAkhir,
                                totalHarga,
                                currentStatus
                            )

                            val data = history.await()
                            lstOfReturnData.add(data.body())

                        }
                    }
                }

                else{
                    job = runBlocking {
                        launch(Dispatchers.IO) {

                            val history = service.insertHistory(
                                kode_device,
                                saldoAwal,
                                saldoAkhir,
                                totalHarga,
                                currentStatus
                            )

                            val data = history.await()
                            lstOfReturnData.add(data.body())
                        }
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