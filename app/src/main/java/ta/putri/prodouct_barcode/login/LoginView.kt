package ta.putri.prodouct_barcode.login

interface LoginView {

    fun onLoading()
    fun onFinish()
    fun getResponses(pesan : String, status : Boolean, id : String)
}