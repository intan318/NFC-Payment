package ta.putri.nfc.register

interface RegisterView {

    fun onLoading()
    fun onFinish()
    fun getResponses(succes : Boolean)
    fun error (error : String)
}