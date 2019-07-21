package ta.putri.nfc.model


object CurrentUser {
    var id: String? = null
    var nama: String? = null
    var email: String? = null
    var saldo : String? = null
    var uid : String? = null

    var listProduk  : MutableList<ProductModel> = mutableListOf()
    var listSubTotal : MutableList<Int> = mutableListOf()
}