@file:Suppress("DEPRECATION")

package ta.putri.prodouct_barcode.customer

import android.app.PendingIntent
import android.nfc.NfcAdapter
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import ta.putri.prodouct_barcode.utlis.ButtonEventConfirmationDialogListener
import ta.putri.prodouct_barcode.utlis.DialogView
import android.nfc.NdefRecord
import android.annotation.SuppressLint
import android.nfc.tech.Ndef
import android.util.Log
import ta.putri.prodouct_barcode.utlis.NFCUtils
import android.content.IntentFilter.MalformedMimeTypeException
import android.app.Activity
import android.content.*
import android.nfc.Tag
import android.os.*
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import org.jetbrains.anko.*
import ta.putri.prodouct_barcode.R
import ta.putri.prodouct_barcode.customer.checkout.CheckoutActivity
import ta.putri.prodouct_barcode.customer.profile.ProfileActivity
import ta.putri.prodouct_barcode.customer.profile.ProfilePresenter
import ta.putri.prodouct_barcode.customer.profile.ProfileView
import ta.putri.prodouct_barcode.model.CurrentUser
import ta.putri.prodouct_barcode.model.CustomerModel
import ta.putri.prodouct_barcode.model.ProductModel
import ta.putri.prodouct_barcode.model.TransactionModel
import ta.putri.prodouct_barcode.utlis.SessionManager
import java.io.UnsupportedEncodingException
import java.util.*
import kotlin.experimental.and


class MainActivity : AppCompatActivity() {

    private lateinit var dialogView: DialogView
    private var mNfcAdapter: NfcAdapter? = null
    private lateinit var vibrator: Vibrator
    private lateinit var nfcPresenter: NFCPresenter
    private lateinit var profilePresenter: ProfilePresenter

    val MIME_TEXT_PLAIN = "text/plain"
    val TAG = "NfcDemo"

    private lateinit var productAdapter: ProductAdapter
    private var produks: MutableList<ProductModel> = mutableListOf()
    private var subTotals: MutableList<Int> = mutableListOf()
    private lateinit var dialogAlert: DialogInterface
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dialogView = DialogView(this)
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this)
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        sessionManager = SessionManager(this)
        CurrentUser.id = sessionManager.getID()

        if (savedInstanceState != null) {
            produks = savedInstanceState.getParcelableArrayList("LIST")
        }

        init()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList("LIST", java.util.ArrayList(produks))
    }

    private fun init() {

        profilePresenter = ProfilePresenter(profileView = null)

        profilePresenter.getProfile(CurrentUser.id.toString(), object : ProfileView{
            override fun onLoading() {
                dialogView.showProgressDialog()
            }

            override fun onFinish() {
                dialogView.hideProgressDialog()
            }

            override fun getResponses(respon: CustomerModel?, transaksi: List<TransactionModel>?) {
                CurrentUser.saldo = respon!!.saldo
                CurrentUser.email = respon.email
                CurrentUser.nama = respon.nama
            }

            override fun error(pesan: String?) {
                toast("Gagal mendapatkan profile")
            }

        })


        rcy_product.layoutManager = LinearLayoutManager(this)
        rcy_product.itemAnimator = DefaultItemAnimator()

        val item = ProductModel(id = "1", harga = "100000", nama = "Barang1", jumlah = "2")
        val item2 = ProductModel(id = "2", harga = "500000", nama = "Barang2", jumlah = "3")
        val item3 = ProductModel(id = "3", harga = "200000", nama = "Barang3", jumlah = "4")

        produks.add(item)
        produks.add(item2)
        produks.add(item3)

        productAdapter = ProductAdapter(produks, subTotals, { position ->
            dialogView.showAddProductDialog(
                produks[position].nama.toString(),
                produks[position].harga.toString(),
                produks[position].jumlah.toString(),

                object : ButtonEventConfirmationDialogListener {
                    override fun onClickYa(jumlah: Int) {
                        produks[position].jumlah = jumlah.toString()
                        subTotals[position] = produks[position].jumlah!!.toInt() * produks[position].harga!!.toInt()
                        productAdapter.notifyItemChanged(position)
                    }

                    override fun onClickTidak() {
                    }

                })
        },
            { position ->
                dialogAlert =
                    alert(
                        message = "Anda yakin mau menghapus produk ini?",
                        title = "Hapus barang " + produks[position].nama
                    ) {
                        okButton {
                            produks.removeAt(position)
                            subTotals.removeAt(position)
                            productAdapter.notifyItemRemoved(position)
                            productAdapter.notifyItemChanged(position, produks.size)
                            controlUI()
                        }
                        cancelButton { dialogAlert.dismiss() }
                    }.show()
            })

        rcy_product.adapter = productAdapter

        //toast(NFCUtils.retrieveNFCMessage(this.intent))
        showAddProduct("5, Naufal,  5000000")

        btn_profile.setOnClickListener {
            startActivity(intentFor<ProfileActivity>())
        }
        btn_checkout.setOnClickListener {
            startActivity(
                intentFor<CheckoutActivity>(
                    "produks" to produks,
                    "subtotals" to subTotals
                )
            )
        }

        controlUI()
    }

    private fun controlUI() {

        if (produks.size == 0) {
            btn_checkout.visibility = View.GONE
            rcy_product.visibility = View.GONE
            ll_empty.visibility = View.VISIBLE
        } else {
            btn_checkout.visibility = View.VISIBLE
            rcy_product.visibility = View.VISIBLE
            ll_empty.visibility = View.GONE
        }
    }


    @SuppressLint("SetTextI18n")
    fun showAddProduct(product: String) {

        if (produks.any { it.id == product }) {
            dialogAlert =
                alert(
                    message = "Barang yang anda scan sudah tersedia di list belanja anda!",
                    title = "Sudah belanja"
                ) {
                    okButton {
                        dialogAlert.dismiss()
                    }
                }.show()

        } else {

            var produk = ProductModel()
            nfcPresenter.retriveProduct(product, object : NFCView {
                override fun onLoading() {
                    dialogView.showProgressDialog()
                }

                override fun onFinish() {
                    dialogView.hideProgressDialog()
                }

                override fun getResponses(respon: ProductModel?) {
                    produk = respon!!
                }

                override fun error(pesan: String?) {
                    toast(pesan.toString())
                }

            })

            vibrator.vibrate(300)
            dialogView.showAddProductDialog(
                produk.nama.toString(),
                produk.harga.toString(),
                "1",
                object : ButtonEventConfirmationDialogListener {
                    override fun onClickYa(jumlah: Int) {
                        if (jumlah > 0) {
                            produk.jumlah = jumlah.toString()

                            val subTotal = when {
                                produk.jumlah == null || produk.harga == null -> 0
                                else -> produk.harga!!.toInt() * jumlah
                            }


                            produks.add(produk)
                            subTotals.add(subTotal)
                            productAdapter.notifyDataSetChanged()

                            controlUI()
                        }
                        else{
                            longToast("Barang batal di pesan")
                        }
                    }

                    override fun onClickTidak() {
                        toast("Barang batal di pesan")
                    }

                })
        }


    }


    //////////////////   NFC CONTROLLER ///////////////////////////

    private fun handleIntent(intent: Intent) {
        val action = intent.action
        if (NfcAdapter.ACTION_NDEF_DISCOVERED == action) {

            val type = intent.type
            if (MIME_TEXT_PLAIN == type) {

                val tag = intent.getParcelableExtra<Parcelable>(NfcAdapter.EXTRA_TAG) as Tag
                NdefReaderTask().execute(tag)

            } else {
                Log.d(TAG, "Wrong mime type: " + type!!)
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED == action) {

            // In case we would still use the Tech Discovered Intent
            val tag = intent.getParcelableExtra<Parcelable>(NfcAdapter.EXTRA_TAG) as Tag
            val techList = tag.techList
            val searchedTech = Ndef::class.java.name

            for (tech in techList) {
                if (searchedTech == tech) {
                    NdefReaderTask().execute(tag)
                    break
                }
            }
        }
    }

    private fun setupForegroundDispatch(activity: Activity, adapter: NfcAdapter) {
        val intent = Intent(activity.applicationContext, activity.javaClass)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP

        val pendingIntent = PendingIntent.getActivity(activity.applicationContext, 0, intent, 0)

        val filters = arrayOfNulls<IntentFilter>(1)
        val techList = arrayOf<Array<String>>()

        // Notice that this is the same filter as in our manifest.
        filters[0] = IntentFilter()
        filters[0]?.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED)
        filters[0]?.addCategory(Intent.CATEGORY_DEFAULT)
        try {
            filters[0]?.addDataType(MIME_TEXT_PLAIN)
        } catch (e: MalformedMimeTypeException) {
            throw RuntimeException("Check your mime type.")
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList)
    }

    private fun stopForegroundDispatch(activity: Activity, adapter: NfcAdapter) {
        adapter.disableForegroundDispatch(activity)
    }


    @SuppressLint("StaticFieldLeak")
    private inner class NdefReaderTask : AsyncTask<Tag, Void, String>() {

        override fun doInBackground(vararg params: Tag): String? {
            val tag = params[0]

            val ndef = Ndef.get(tag)
                ?: // NDEF is not supported by this Tag.
                return null

            val ndefMessage = ndef.cachedNdefMessage

            val records = ndefMessage.records
            for (ndefRecord in records) {
                if (ndefRecord.tnf == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(
                        ndefRecord.type,
                        NdefRecord.RTD_TEXT
                    )
                ) {
                    try {
                        return readText(ndefRecord)
                    } catch (e: UnsupportedEncodingException) {
                        Log.e(ContentValues.TAG, "Unsupported Encoding", e)
                    }

                }
            }

            return null
        }

        @Throws(UnsupportedEncodingException::class)
        private fun readText(record: NdefRecord): String {
            /*
             * See NFC forum specification for "Text Record Type Definition" at 3.2.1
             *
             * http://www.nfc-forum.org/specs/
             *
             * bit_7 defines encoding
             * bit_6 reserved for future use, must be 0
             * bit_5..0 length of IANA language code
             */

            val payload = record.payload

            // Get the Text Encoding
            val textEncoding = if ((payload[0] and 128.toByte()) == 0.toByte()) {
                "UTF-8"
            } else {
                "UTF-16"
            }

            val languageCodeLength = payload[0] and 51
            return String(payload, languageCodeLength + 1, payload.size - languageCodeLength - 1, charset(textEncoding))
        }

        override fun onPostExecute(result: String?) {
            if (result != null) {
                showAddProduct(result)
            }
        }

    }

    override fun onResume() {
        super.onResume()
        mNfcAdapter?.let { setupForegroundDispatch(this, it) }
    }

    override fun onPause() {
        mNfcAdapter?.let { stopForegroundDispatch(this, it) }
        super.onPause()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        handleIntent(intent!!)

    }

}