package com.example.expensemanager

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.expensemanager.database.PurchaseEntity
import com.example.expensemanager.database.PurchaseViewModel
import com.example.expensemanager.databinding.FragmentAddExpensesBinding

class AddExpensesFragment : Fragment() {

    private var _binding: FragmentAddExpensesBinding? = null
    private val binding get() = _binding!!

    private lateinit var  mPurchaseViewModel : PurchaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_add_expenses, container, false)

        _binding = FragmentAddExpensesBinding.inflate(inflater, container, false)

        mPurchaseViewModel = ViewModelProvider(this).get(PurchaseViewModel::class.java)

        binding.btnSaveExpenses.setOnClickListener {
            insertDataToDatabase()
        }
        return binding.root
    }

    private fun insertDataToDatabase(){
        val purchaseId = binding.edPurchaseId.text.toString()
        val purchaseType = binding.edPurchaseType.text.toString()
        val purchaseDate = binding.edDate.text.toString()
        val storeName = binding.edStoreName.text.toString()
        val storeLoc = binding.edStoreLocation.text.toString()
        val itemsPur = binding.edItemsPurchased.text.toString()
        val purTotal = binding.edTotalCost.text.toString()

        if(inputCheck(purchaseId.toInt(), purchaseType,purchaseDate, storeName,storeLoc, itemsPur, purTotal)){
            val purchase = PurchaseEntity(purchaseId.toInt(), purchaseType, purchaseDate, storeName, storeLoc, itemsPur, purTotal)
            mPurchaseViewModel.addPurchase(purchase)

            Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_LONG).show()

            findNavController().navigate(R.id.action_addexpensesFragment_to_displayexpenseFragment)

        }else{
            Toast.makeText(requireContext(), "Please fill out all fields!", Toast.LENGTH_LONG).show()
        }
    }

    private fun inputCheck(
        purchaseId: Int, purchaseType: String,
        purchaseDate: String, storeName: String,
        storeLoc: String,
        itemsPur: String, purTotal: String): Boolean{
        return !(TextUtils.isEmpty(purchaseId.toString()) && TextUtils.isEmpty(purchaseType) && TextUtils.isEmpty(purchaseDate) && TextUtils.isEmpty(storeName)
                && TextUtils.isEmpty(storeLoc) && TextUtils.isEmpty(itemsPur) && TextUtils.isEmpty(purTotal))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}