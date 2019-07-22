package ta.putri.nfc.profile

import kotlinx.coroutines.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import ta.putri.nfc.repository.ApiFactory

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

    fun editNama(nama :String, id :String, profileView: ProfileView?){

        this.profileView = profileView
        profileView?.onLoading()

        doAsync {
            runBlocking {
                launch(Dispatchers.IO) {
                    try {
                        val data = service.changeNameCustomer(nama, id)
                        val result = data.await()

                        uiThread {
                            profileView?.getChangeNameResponse(result.body())
                            profileView?.onFinish()
                        }
                    }catch (e : Exception){
                        profileView?.error(e.message.toString())
                        profileView?.onFinish()
                    }
                }
            }
        }
    }

    fun deleteTransaction(id : String, profileView: ProfileView?){

        this.profileView = profileView
        profileView?.onLoading()

        doAsync {
            runBlocking {
                launch(Dispatchers.IO) {
                    try {
                        val data = service.deleteTransaction(id)
                        val result = data.await()

                        uiThread {
                            profileView?.getChangeNameResponse(result.body())
                            profileView?.onFinish()
                        }
                    }catch (e : Exception){
                        profileView?.error(e.message.toString())
                        profileView?.onFinish()
                    }
                }
            }
        }
    }


    fun viewOnDestroy() {
        job?.cancel()
        profileView = null
    }
}