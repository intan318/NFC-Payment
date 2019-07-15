package ta.putri.nfc.model


import com.google.gson.annotations.SerializedName

data class CustomerModel(
    @SerializedName("created_at")
    var createdAt: String? = "", // 2019-07-08 01:23:20
    @SerializedName("email")
    var email: String? = "", // naufalmahfudzismail@gmail.com
    @SerializedName("id")
    var id: Int? = 0, // 1
    @SerializedName("nama")
    var nama: String? = "", // Naufal Mahfudz I
    @SerializedName("password")
    var password: String? = "", // $2y$10$QZQSQjiZB8Gdzi9zftyPrOSCXFWhic.G4ws2CIYiDi1kqUEm2/OL6
    @SerializedName("saldo")
    var saldo: String? = "", // 2000000
    @SerializedName("updated_at")
    var updatedAt: String? = "" // 2019-07-08 02:53:40
)