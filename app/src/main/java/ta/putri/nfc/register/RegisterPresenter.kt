package ta.putri.nfc.register

import android.annotation.SuppressLint
import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import ta.putri.nfc.repository.ApiFactory

class RegisterPresenter(val context: Context, var view: RegisterView?) {

    private val service = ApiFactory.makeRetrofitService()
    var job: Job? = null

    @SuppressLint("SimpleDateFormat")
    fun register(nama: String, email: String, password: String, uid : String) {

        view?.onLoading()

         doAsync {
            try {
                runBlocking {
                    launch(Dispatchers.IO) {
                        val data = service.regisCustomer(nama, email, password, "0", uid)
                        val result = data.await()

                        uiThread {
                            view?.getResponses(result.body()?.error!!)
                            view?.onFinish()
                        }
                    }
                }
            }
            catch (e : Exception){
                uiThread {
                    view?.onFinish()
                    view?.error(e.message.toString())
                }
            }
        }


    }
}