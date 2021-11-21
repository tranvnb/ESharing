package com.example.expensemanager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.expensemanager.database.PurchaseEntity
import com.example.expensemanager.databinding.FragmentDisplayExpensesBinding
import com.example.expensemanager.databinding.ListItemBinding

class ListAdapter(): RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    private var purchaseList = emptyList<PurchaseEntity>()

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
//        val binding = ListItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int){

        val currentItem = purchaseList[position]

        holder.itemView.findViewById<TextView>(R.id.id_txt).text =
            "Purchase ID: " + currentItem.purchaseId.toString() + "\n" + "Purchase Type: " + currentItem.purchaseType + "\n" +
        "Purchase Date: " + currentItem.purcdate + "\n" + "Store Name: " + currentItem.storename + "\n" +  "Store Location: " +
                    currentItem.storelocation + "\n" +  "Items/no of items: " + currentItem.itemspurchased + "\n" +
                    "Total cost: " + currentItem.totalcost

        holder.itemView.findViewById<ConstraintLayout>(R.id.rowLayout).setOnClickListener {
            val action = DisplayExpensesFragmentDirections.actionDisplayexpensesFragmentToUpdateexpensesFragment(currentItem)
            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return purchaseList.size
    }


    fun setData(purchase: List<PurchaseEntity>) {
        this.purchaseList = purchase
        notifyDataSetChanged()
    }

}


