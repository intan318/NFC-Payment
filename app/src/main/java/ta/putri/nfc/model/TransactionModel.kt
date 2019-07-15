package ta.putri.nfc.model


import com.google.gson.annotations.SerializedName

data class TransactionModel(
    @SerializedName("customer")
    var customer: String? = null, // Naufal Mahfudz I
    @SerializedName("jumlah_per_produk")
    var jumlahPerProduk: String? = null, // 2,1
    @SerializedName("produk")
    var produk: String? = null, // Barang1,Barang2
    @SerializedName("subtotal_per_produk")
    var subtotalPerProduk: String? = null, // 200000,500000
    @SerializedName("total_harga")
    var totalHarga: String? = null, // 700000
    @SerializedName("tanggal")
    var tanggal: String? = null // 70000
)