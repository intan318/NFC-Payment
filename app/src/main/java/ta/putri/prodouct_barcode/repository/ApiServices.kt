package ta.putri.prodouct_barcode.repository

import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*
import ta.putri.prodouct_barcode.model.*

interface ApiServices {


    @Headers("Accept: application/json")
    @GET("produk/{code}")
    fun getProductAsync(@Path("code") code: String): Deferred<Response<ProductModel?>>

    @Headers("Accept: application/json")
    @GET("customer/{id}")
    fun getCustomerAsync(@Path("id") id: String): Deferred<Response<CustomerModel?>>

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
        @Field("password") password: String?
    ): Deferred<Response<PostResponses>>

    @FormUrlEncoded
    @Headers("Accept: application/json")
    @PUT("customer")
    fun updateSaldoCustomer(
        @Field("saldo") saldo: String?
    ): Deferred<Response<PostResponses>>

    @FormUrlEncoded
    @Headers("Accept: application/json")
    @PUT("produk")
    fun updateProduk(
        @Field("stock") stock: String?
    ): Deferred<Response<PostResponses>>

    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("transaksi")
    fun inputTransaksi(
        @Field("customer_id") customer_id: String?,
        @Field("produk_id") produks_id: String?,
        @Field("jumlah_per_produk") jumlah_produk: String?,
        @Field("subtotal_per_produk") subtotal: String?,
        @Field("total_harga") total_harga: String?
    ): Deferred<Response<PostResponses>>

    //updated when transaksi
    @FormUrlEncoded
    @Headers("Accept: application/json")
    @POST("terjual")
    fun triggerProduk(
        @Field("customer_id") customer_id: String,
        @Field("produk_id") produk_id: String,
        @Field("jumlah") jumlah: String
    ): Deferred<Response<LoginRespons?>>


    @Headers("Accept: application/json")
    @GET("transaksi/customer/{id}")
    fun getTrnsaksiCustomer(@Path("id") id: String): Deferred<Response<List<TransactionModel>?>>





}