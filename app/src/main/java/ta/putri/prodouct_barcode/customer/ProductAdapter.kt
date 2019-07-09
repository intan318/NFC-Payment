package ta.putri.prodouct_barcode.customer


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card_product.view.*
import ta.putri.prodouct_barcode.R
import ta.putri.prodouct_barcode.model.ProductModel

class ProductAdapter(
    private val produks: MutableList<ProductModel>,
    private val subTotals : MutableList<Int>,
    private val editListener: (Int) -> Unit,
    private val deleteListener: (Int) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.card_product, parent, false))
    }

    override fun getItemCount(): Int {
        return produks.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding(produks[position], position,subTotals[position], editListener, deleteListener)
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
            product: ProductModel,
            position: Int,
            subTotals: Int,
            editListener: (Int) -> Unit,
            deleteListener: (Int) -> Unit
        ) {

            view.txt_namaBarang.text = product.nama
            view.txt_hargaBarang.text = product.harga
            view.txt_jumlahBarang.text = product.jumlah

            if (product.harga == null || product.jumlah == null) {
                view.txt_subtotal.text = 0.toString()
            } else {
                view.txt_subtotal.text = subTotals.toString()
            }


            view.btn_delete.setOnClickListener { deleteListener(position) }
            view.btn_edit.setOnClickListener { editListener(position) }

        }
    }
}