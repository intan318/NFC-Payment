package ta.putri.nfc.model


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class TransactionModel(
    @SerializedName("id")
    var id: String? = null,
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
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(customer)
        parcel.writeString(jumlahPerProduk)
        parcel.writeString(produk)
        parcel.writeString(subtotalPerProduk)
        parcel.writeString(totalHarga)
        parcel.writeString(tanggal)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<TransactionModel> {
        override fun createFromParcel(parcel: Parcel): TransactionModel {
            return TransactionModel(parcel)
        }

        override fun newArray(size: Int): Array<TransactionModel?> {
            return arrayOfNulls(size)
        }
    }

}