package ta.putri.nfc.repository

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*
import ta.putri.nfc.model.*

interface ApiServices {


    @Headers("Accept: application/json")
    @GET("produk/{code}")
    fun getProductAsync(@Path("code") code: String): Deferred<Response<ProductModel?>>

    @Headers("Accept: application/json")
    @GET("customer/{id}")
    fun getCustomerAsync(@Path("id") id: String): Deferred<Response<CustomerModel?>>

    @Headers("Accept: application/json")
    @GET("transaksi/customer/{id}")
    fun getTrnsaksiCustomer(@Path("id") id: String): Deferred<Response<List<TransactionModel>?>>

    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("customer/login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ): Deferred<Response<LoginRespons?>>

    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("customer")
    fun regisCustomer(
        @Field("nama") nama: String?,
        @Field("email") email: String?,
        @Field("password") password: String?,
        @Field("saldo") saldo : String?,
        @Field("uid") uid : String?
    ): Deferred<Response<APIResponses>>


    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("transaksi")
    fun inputTransaksi(
        @Field("customer_id") customer_id: String?,
        @Field("produk_id") produks_id: String?,
        @Field("jumlah_per_produk") jumlah_produk: String?,
        @Field("subtotal_per_produk") subtotal: String?,
        @Field("total_harga") total_harga: String?,
        @Field("kode_device") kode_device : String?,
        @Field("saldo_awal") saldow_awal : String?,
        @Field("saldo_akhir") saldo_akhir : String?,
        @Field("status") status : String
    ): Deferred<Response<APIResponses>>

    //updated when transaksi
    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("terjual")
    fun insertProdukTerjual(
        @Field("customer_id") customer_id: String,
        @Field("produk_id") produk_id: String,
        @Field("jumlah") jumlah: String
    ): Deferred<Response<APIResponses?>>

    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("histori")
    fun insertHistory(
        @Field("uid") uid : String,
        @Field("saldo_awal") saldo_awal: String,
        @Field("saldo_akhir") saldo_akhir: String,
        @Field("total_harga") pengurangan : String,
        @Field("status") status : String
    ): Deferred<Response<APIResponses?>>


    @FormUrlEncoded
    @Headers("Accept: application/json")
    @PUT("customer/{id}")
    fun changeNameCustomer(
        @Field("nama") nama: String?,
        @Path("id") id: String
    ): Deferred<Response<APIResponses>>

    @DELETE("transaksi/{id}")
    @Headers("Accept: application/json")
    fun deleteTransaction(
        @Path("id") id: String?
    ): Deferred<Response<APIResponses>>

}