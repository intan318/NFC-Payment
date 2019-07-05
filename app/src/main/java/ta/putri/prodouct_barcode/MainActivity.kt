package ta.putri.prodouct_barcode

import android.app.PendingIntent
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NfcAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Html
import android.text.Spanned
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.add_product_layout.*
import ta.putri.prodouct_barcode.utlis.ButtonEventConfirmationDialogListener
import ta.putri.prodouct_barcode.utlis.DialogView
import android.nfc.NdefRecord
import android.R.attr.tag
import android.annotation.SuppressLint
import android.nfc.tech.Ndef
import android.os.Parcelable
import org.jetbrains.anko.toast
import ta.putri.prodouct_barcode.utlis.NFCUtils


class MainActivity : AppCompatActivity() {

    private lateinit var dialogView: DialogView
    private var mNfcAdapter: NfcAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dialogView = DialogView(this)

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this)
        tv_messages.text = NFCUtils.retrieveNFCMessage(this.intent)
        toast(NFCUtils.retrieveNFCMessage(this.intent))

        showAddProduct("Naufal,  461601053")

    }

    override fun onResume() {
        super.onResume()
        mNfcAdapter?.let {
            NFCUtils.enableNFCInForeground(it, this, javaClass)
        }
    }

    override fun onPause() {
        super.onPause()
        mNfcAdapter?.let {
            NFCUtils.disableNFCInForeground(it, this)
        }
    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val message = NFCUtils.retrieveNFCMessage(intent)
        showAddProduct(message)

    }

    private fun processNFCData(inputIntent: Intent) {
        val rawMessages = inputIntent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES)
        if (rawMessages != null && rawMessages.isNotEmpty()) {
            val messages = arrayOfNulls<NdefMessage>(rawMessages.size)
            for (i in rawMessages.indices) {
                messages[i] = rawMessages[i] as NdefMessage
            }

            val msg = rawMessages[0] as NdefMessage
            val payloadStringData = String(msg.records[0].payload)
            showAddProduct(payloadStringData)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showAddProduct(product: String) {

        val produk = product.split(",")
        val nama = produk[0]
        val harga = produk[1]

        tv_messages.text = "$nama, $harga"


        dialogView.showConfirmationDialog(nama, harga, object : ButtonEventConfirmationDialogListener {
            override fun onClickYa() {
            }
            override fun onClickTidak() {

            }

        })
    }


}
