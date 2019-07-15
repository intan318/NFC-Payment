package ta.putri.nfc.model


import com.google.gson.annotations.SerializedName

data class LoginRespons(
    @SerializedName("error")
    var error: Boolean? = null, // false
    @SerializedName("id")
    var id: String? = null, // 1
    @SerializedName("message")
    var message: String? = null // Silahkan masuk
)