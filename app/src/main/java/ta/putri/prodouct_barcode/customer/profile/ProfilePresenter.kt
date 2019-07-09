package ta.putri.prodouct_barcode.customer.profile

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import ta.putri.prodouct_barcode.repository.ApiFactory

class ProfilePresenter(private var profileView: ProfileView?) {

    private val service = ApiFactory.makeRetrofitService()
    var job: Job? = null

    fun getProfile(id : String, profileView: ProfileView?){

        this.profileView = profileView
        profileView?.onLoading()

        doAsync {
            job = GlobalScope.launch(Dispatchers.Main){

                try {
                    val data = service.getCustomerAsync(id)
                    val transaksi = service.getTrnsaksiCustomer(id)
                    val result = data.await()
                    val trans = transaksi.await()

                    uiThread {
                        profileView?.getResponses(result.body(), trans.body())
                        profileView?.onFinish()
                    }
                }

                catch (e : NullPointerException){
                    profileView?.error(e.toString())
                    profileView?.onFinish()
                }
            }
        }
    }

    fun viewOnDestroy() {
        job?.cancel()
        profileView = null
    }
}