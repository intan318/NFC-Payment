package ta.putri.prodouct_barcode.model

class UserModel(

    val id: Long?,
    val nama: String?,
    val email: String?,
    val password : String?,
    val tgl_daftar : String?

) {

    companion object {

        const val TABLE_USER: String = "TABLE_USER"
        const val ID: String = "ID_"
        const val USER_NAME: String = "NAMA"
        const val USER_EMAIL: String = "EMAIL"
        const val USER_PASSWORD: String = "PASSWORD"
        const val TGL_DAFTAR: String = "TGL_DAFTAR"

    }
}