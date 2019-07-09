package ta.putri.prodouct_barcode.model

class TransactionModel(
    val id: Long?,
    val id_user : Long?,
    val nama_per_barang: String? = null,
    val jumlah_per_barang: String? = null,
    val harga_per_barang : String? = null,
    val subtotal_per_barang : String? = null,
    val tanggal : String?= null,
    val total_harga : String? = null
){
    companion object {

        const val TABLE_TRANSACTION: String = "TABLE_TRANSAKSI"
        const val ID: String = "ID_"
        const val ID_USER: String = "ID_USER"
        const val NAMA_PER_BARANG: String = "NAMA_PER_BARANG"
        const val JUMLAH_PER_BARANG: String = "JUMLAH_PER_BARANG"
        const val HARGA_PER_BARANG: String = "HARGA_PER_BARANG"
        const val TANGGAL: String = "TANGGAL"
        const val TOTAL_HARGA: String = "TOTAL_HARGA"
    }
}