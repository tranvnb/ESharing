package com.lf.esharing

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.datepicker.MaterialDatePicker
import com.lf.esharing.database.purchase.PurchaseEntity
import com.lf.esharing.database.purchase.PurchaseViewModel
import com.lf.esharing.database.user.UserViewModel
import com.lf.esharing.databinding.FragmentAddExpensesBinding
import com.lf.esharing.utils.DateConverter
import com.lf.esharing.utils.Validator.inputCheck
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.temporal.TemporalAccessor

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

        binding.btnChooseDate.setOnClickListener {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select date")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build()

                datePicker.show(requireActivity().supportFragmentManager, "DATEPICKER")
                datePicker.addOnPositiveButtonClickListener {
                    val local = LocalDateTime.ofInstant(Instant.ofEpochMilli(it), ZoneOffset.UTC)
                    binding.edDate.setText(DateConverter.FORMATTER.format(local))
                }
        }
        return binding.root
    }

    private fun insertDataToDatabase(){
        val purchaseType = binding.edPurchaseType.text.toString()
        val purchaseDate = binding.edDate.text.toString()
        val storeName = binding.edStoreName.text.toString()
        val storeLoc = binding.edStoreLocation.text.toString()
        val purTotal = binding.edTotalCost.text.toString().toDoubleOrNull()

        if(inputCheck(purchaseType,purchaseDate, storeName, storeLoc, purTotal)){
            val purchase = PurchaseEntity(storeName, storeLoc, purchaseType, purTotal!!, LocalDateTime.parse(purchaseDate))
            mPurchaseViewModel.addPurchase(purchase, UserViewModel.username, UserViewModel.password)

            Toast.makeText(requireContext(), "Successfully added!", Toast.LENGTH_LONG).show()

            findNavController().navigate(R.id.action_addexpensesFragment_to_displayexpenseFragment)

        }else{
            Toast.makeText(requireContext(), "Please fill out all fields!", Toast.LENGTH_LONG).show()
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}