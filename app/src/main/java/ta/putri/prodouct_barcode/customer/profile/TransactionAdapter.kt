package ta.putri.prodouct_barcode.customer.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card_product.view.*
import kotlinx.android.synthetic.main.card_transaksi.view.*
import kotlinx.android.synthetic.main.card_transaksi.view.txt_hargaBarang
import kotlinx.android.synthetic.main.card_transaksi.view.txt_jumlahBarang
import kotlinx.android.synthetic.main.card_transaksi.view.txt_namaBarang
import ta.putri.prodouct_barcode.R
import ta.putri.prodouct_barcode.model.ProductModel
import ta.putri.prodouct_barcode.model.TransactionModel

class TransactionAdapter(
    private val transactions: MutableList<TransactionModel>
) :
    RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_transaksi, parent, false))
    }

    override fun getItemCount(): Int {
        return transactions.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding(transactions[position], position)
    }


    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        fun binding(
            transaksi: TransactionModel,
            position: Int
        ) {

            view.txt_namaBarang.text = transaksi.nama_per_barang
            view.txt_hargaBarang.text = transaksi.harga_per_barang
            view.txt_jumlahBarang.text = transaksi.jumlah_per_barang
            view.txt_tanggal.text = transaksi.tanggal
            view.txt_total.text = transaksi.total_harga


        }
    }
}