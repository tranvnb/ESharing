package com.lf.esharing

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.lf.esharing.database.purchase.PurchaseEntity
import com.lf.esharing.databinding.ListItemBinding
import java.time.format.DateTimeFormatter

class ListAdapter(purchase: List<PurchaseEntity>, private val mListenter: ListViewItemClickListener): RecyclerView.Adapter<ListAdapter.PurchaseItemViewHolder>() {

    private var purchaseList = purchase
    private var selectedList = mutableListOf<PurchaseEntity>()

    class PurchaseItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val binding = ListItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PurchaseItemViewHolder {
        val holder = PurchaseItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        )
//        holder.adapterPosition this for init onclick listener better than in onBindViewHolder
        return holder
    }

    override fun onBindViewHolder(holder: PurchaseItemViewHolder, position: Int) {

        val currentItem = purchaseList[position]
        with(holder.binding) {
            item.text = currentItem.itemspurchased
            date.text = currentItem.purcdate?.format(DateTimeFormatter.ISO_LOCAL_DATE)
            total.text = currentItem.totalcost.toString()

            imgCheck.setImageResource(
                if (selectedList.contains(currentItem)) {
                    R.drawable.ic_selected
                }else {
                    R.drawable.ic_unselected
                })

            imgCheck.setOnClickListener {
                if (selectedList.contains(currentItem)){
                    imgCheck.setImageResource(R.drawable.ic_unselected)
                    selectedList.remove(currentItem)
                } else {
                    imgCheck.setImageResource(R.drawable.ic_selected)
                    selectedList.add(currentItem)
                }

                mListenter.onClick()
            }

            root.setOnClickListener {
                val action = DisplayExpensesFragmentDirections.actionDisplayexpensesFragmentToUpdateexpensesFragment(currentItem)
                holder.itemView.findNavController().navigate(action)
            }

        }

    }

    override fun getItemCount(): Int {
        return purchaseList.size
    }


    fun setData(purchase: List<PurchaseEntity>) {
        this.purchaseList = purchase
        notifyDataSetChanged()
    }

    fun selectedItemCount(): Int {
        return selectedList.count()
    }

    fun getSelectedList(): List<PurchaseEntity> {
        return selectedList
    }

    interface ListViewItemClickListener{
        fun onClick()
    }

}


