package ta.putri.prodouct_barcode.register

interface RegisterView {

    fun onLoading()
    fun onFinish()
    fun getResponses(succes : Boolean)
}