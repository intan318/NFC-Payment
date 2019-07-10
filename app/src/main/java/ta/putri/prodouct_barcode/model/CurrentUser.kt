package ta.putri.prodouct_barcode.model


object CurrentUser {
    var id: String? = null
    var nama: String? = null
    var email: String? = null
    var saldo : String? = null

    var listProduk  : MutableList<ProductModel> = mutableListOf()
    var listSubTotal : MutableList<Int> = mutableListOf()
}