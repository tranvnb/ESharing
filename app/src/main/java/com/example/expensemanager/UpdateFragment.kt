package com.example.expensemanager

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.expensemanager.database.PurchaseEntity
import com.example.expensemanager.database.PurchaseViewModel
import com.example.expensemanager.databinding.FragmentUpdateBinding

class UpdateFragment : Fragment() {

    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<UpdateFragmentArgs>()

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

        mPurchaseViewModel = ViewModelProvider(this).get(PurchaseViewModel::class.java)

        binding.updateedPurchaseId.setText(args.currentPurchase.purchaseId.toString())
        binding.updateedPurchaseType.setText(args.currentPurchase.purchaseType)
        binding.updateedDate.setText(args.currentPurchase.purcdate)
        binding.updateedStoreName.setText(args.currentPurchase.storename)
        binding.updateedStoreLocation.setText(args.currentPurchase.storelocation)
        binding.updateedItemsPurchased.setText(args.currentPurchase.itemspurchased)
        binding.updateedTotalCost.setText(args.currentPurchase.totalcost)

        binding.btnUpdateExpenses.setOnClickListener {
            updateItem()
        }

        // Add menu
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun updateItem() {
        val purchaseId = Integer.parseInt(binding.updateedPurchaseId.text.toString())
        val purchaseType = binding.updateedPurchaseType.text.toString()
        val purchaseDate = binding.updateedDate.text.toString()
        val storeName = binding.updateedStoreName.text.toString()
        val storeLoc = binding.updateedStoreLocation.text.toString()
        val itemsPur = binding.updateedItemsPurchased.text.toString()
        val purTotal = binding.updateedTotalCost.text.toString()

        //val age = Integer.parseInt(binding.updateAgeEt.text.toString()) // Parses a string returns an integer.

        if (inputCheck(purchaseId, purchaseType, purchaseDate, storeName, storeLoc, itemsPur, purTotal)) {
            // Create User Object
            val updatedPurchase = PurchaseEntity(args.currentPurchase.purchaseId, purchaseType, purchaseDate, storeName, storeLoc
            , itemsPur, purTotal)

            // Update Current User
            mPurchaseViewModel.updatePurchase(updatedPurchase)
            Toast.makeText(requireContext(), "Updated Successfully !", Toast.LENGTH_SHORT).show()

            // Navigate back to List Fragment
            findNavController().navigate(R.id.action_updateFragment_to_displayexpenseFragment)
        } else {
            Toast.makeText(requireContext(), "Please fill all fields !", Toast.LENGTH_SHORT).show()
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

    // Inflate the layout to our menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.delete_menu, menu)
    }

    // Handle clicks on menu items
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete) {
            deletePurchase()
        }
        return super.onOptionsItemSelected(item)
    }

    // Implement logic to delete a user
    private fun deletePurchase() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->     // Make a "Yes" option and set action if the user selects "Yes"
           // mPurchaseViewModel.deletePurchase(args.currentPurchase)    // Execute : delete user
            Toast.makeText(                                // Notification if a user is deleted successfully
                requireContext(),
                "Successfully removed ${args.currentPurchase.purchaseType}",
                Toast.LENGTH_SHORT)
                .show()
            findNavController().navigate(R.id.action_updateFragment_to_displayexpenseFragment) // Navigate to List Fragment after deleting a user
        }
        builder.setNegativeButton("No") { _, _ -> }    // Make a "No" option and set action if the user selects "No"
        builder.setTitle("Delete ${args.currentPurchase.purchaseType} ?")  // Set the title of the prompt with a sentence saying the first name of the user inside the app (using template string)
        builder.setMessage("Are you sure to remove ${args.currentPurchase.purchaseType} ?")  // Set the message of the prompt with a sentence saying the first name of the user inside the app (using template string)
        builder.create().show()  // Create a prompt with the configuration above to ask the user (the real app user which is human)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // <- whenever we destroy our fragment, _binding is set to null. Hence it will avoid memory leaks.
    }
}