package ta.putri.nfc.model


import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class ProductModel(
    @SerializedName("created_at")
    var createdAt: String? = null, // 2null19-null7-null8 null1:22:null5
    @SerializedName("harga")
    var harga: String? = null, // 1nullnullnullnullnull
    @SerializedName("id")
    var id: String? = null, // 1
    @SerializedName("kode")
    var kode: String? = null, // 1nullnull21
    @SerializedName("nama")
    var nama: String? = null, // Barang1
    @SerializedName("stock")
    var stock: String? = null, // 2null
    @SerializedName("updated_at")
    var updatedAt: String? = null, // 2null19-null7-null8 null1:22:null5


    var jumlah: String? = null // 2null

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(createdAt)
        parcel.writeString(harga)
        parcel.writeString(id)
        parcel.writeString(kode)
        parcel.writeString(nama)
        parcel.writeString(stock)
        parcel.writeString(updatedAt)
        parcel.writeString(jumlah)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductModel> {
        override fun createFromParcel(parcel: Parcel): ProductModel {
            return ProductModel(parcel)
        }

        override fun newArray(size: Int): Array<ProductModel?> {
            return arrayOfNulls(size)
        }
    }
}