package ta.putri.prodouct_barcode.model

class AdminModel(
    val id: String? = null,
    val username: String? = null,
    val name: String? = null,
    val password: String? = null
){
    companion object{
        const val TABLE_ADMIN: String = "TABLE_USER"
        const val ID_: String = "ID_"
        const val ADMIN_NAME: String = "NAMA"
        const val ADMIN_USERNAME: String = "USERNAME"
        const val ADMIN_PASSWORD: String = "PASSWORD"
    }
}
