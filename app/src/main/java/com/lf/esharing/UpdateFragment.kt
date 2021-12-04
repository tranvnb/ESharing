package com.lf.esharing

import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.datepicker.MaterialDatePicker
import com.lf.esharing.database.purchase.PurchaseEntity
import com.lf.esharing.database.purchase.PurchaseViewModel
import com.lf.esharing.database.purchase.PurchasesRequest
import com.lf.esharing.database.user.UserViewModel
import com.lf.esharing.databinding.FragmentUpdateBinding
import com.lf.esharing.utils.DateConverter
import com.lf.esharing.utils.MoshiHelper
import com.lf.esharing.utils.Validator.inputCheck
import okhttp3.MediaType
import okhttp3.RequestBody
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

class UpdateFragment : Fragment() {

    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    val args: UpdateFragmentArgs by navArgs()

    private lateinit var mPurchaseViewModel: PurchaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
       // return inflater.inflate(R.layout.fragment_update, container, false)
        _binding = FragmentUpdateBinding.inflate(inflater, container, false)



        // Add menu
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mPurchaseViewModel = ViewModelProvider(this).get(PurchaseViewModel::class.java)

        binding.edPurchaseType.setText(args.selectedItem.itemspurchased)
        binding.edDate.setText(DateConverter.FORMATTER.format(args.selectedItem.purcdate))
        binding.edStoreName.setText(args.selectedItem.storename)
        binding.edStoreLocation.setText(args.selectedItem.storelocation)
        binding.edTotalCost.setText(args.selectedItem.totalcost.toString())

        binding.btnUpdateExpenses.setOnClickListener {
            updateItem()
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
    }

    private fun updateItem() {
        val purchaseType = binding.edPurchaseType.text.toString()
        val purchaseDate = binding.edDate.text.toString()
        val storeName = binding.edStoreName.text.toString()
        val storeLoc = binding.edStoreLocation.text.toString()
        val purTotal = binding.edTotalCost.text.toString().toDoubleOrNull()

        if(inputCheck(purchaseType,purchaseDate, storeName, storeLoc, purTotal)){
            val purchase = PurchaseEntity(storeName, storeLoc, purchaseType, purTotal!!, LocalDateTime.parse(purchaseDate))
            purchase.id = args.selectedItem.id
            val str = MoshiHelper.toJsonObject(PurchasesRequest::class.java, PurchasesRequest(UserViewModel.username, UserViewModel.password, purchase))
            val request = RequestBody.create(MediaType.parse("application/json"), str)
            mPurchaseViewModel.updatePurchase(request, purchase).observe(viewLifecycleOwner, Observer {
                Toast.makeText(requireContext(), "Successfully update!", Toast.LENGTH_LONG).show()
                findNavController().popBackStack()
            })

        }else{
            Toast.makeText(requireContext(), "Please fill out all fields!", Toast.LENGTH_LONG).show()
        }
    }

//    // Inflate the layout to our menu
//    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
//        inflater.inflate(R.menu.delete_menu, menu)
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}