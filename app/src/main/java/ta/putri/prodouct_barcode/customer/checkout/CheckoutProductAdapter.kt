package ta.putri.prodouct_barcode.customer.checkout


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_profile.view.*
import kotlinx.android.synthetic.main.item_produk_transaksi.view.*
import ta.putri.prodouct_barcode.R
import ta.putri.prodouct_barcode.model.ProductModel

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

        fun binding(
            product: ProductModel
        ) {

            view.txt_nama.text = product.nama
            view.txt_harga.text = product.harga
            view.txt_jumlah.text = product.jumlah

            if (product.harga == null || product.jumlah == null) {
                view.txt_subtotal.text = 0.toString()
            } else {
                view.txt_subtotal.text = (product.harga!!.trim().toInt() * product.jumlah!!.trim().toInt()).toString()
            }


        }
    }
}