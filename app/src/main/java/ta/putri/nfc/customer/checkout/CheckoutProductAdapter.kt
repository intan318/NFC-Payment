package ta.putri.nfc.customer.checkout


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_produk_transaksi.view.*
import ta.putri.nfc.R
import ta.putri.nfc.model.ProductModel

class CheckoutProductAdapter(
    private val produks: MutableList<ProductModel>
) :
    RecyclerView.Adapter<CheckoutProductAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_produk_transaksi, parent, false))
    }

    override fun getItemCount(): Int {
        return produks.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding(produks[position])
    }

    /*fun removeAt(position: Int) {
        produks.removeAt(position)
        notifyItemRemoved(position)
        notifyItemChanged(position, produks.size)
    }

    fun editAt(jumlah: Int, position: Int) {
        produks[position].jumlah = jumlah.toString()
        notifyItemChanged(position)
    }

    fun addItem(product: ProductModel) {
        produks.add(product)
        notifyDataSetChanged()
    }*/

    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        @SuppressLint("SetTextI18n")
        fun binding(
            product: ProductModel
        ) {

            view.txt_namaBarang.text = product.nama
            view.txt_harga.text = "Rp.${product.harga}"
            view.txt_jumlahBarang.text = product.jumlah

            if (product.harga == null || product.jumlah == null) {
                view.txt_subtotal.text = 0.toString()
            } else {
                view.txt_subtotal.text = (product.harga!!.trim().toInt() * product.jumlah!!.trim().toInt()).toString()
            }


        }
    }
}