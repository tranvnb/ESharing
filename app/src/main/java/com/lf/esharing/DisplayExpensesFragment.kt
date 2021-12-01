package com.lf.esharing

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.lf.esharing.database.purchase.PurchaseViewModel
import com.lf.esharing.database.user.UserViewModel
import com.lf.esharing.databinding.FragmentDisplayExpensesBinding

class DisplayExpensesFragment : Fragment(){

    private var _binding: FragmentDisplayExpensesBinding? = null
    private val binding get() = _binding!!

    private lateinit var mPurchaseViewModel: PurchaseViewModel
    lateinit var adapter: ListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private lateinit var viewModel: PurchaseViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDisplayExpensesBinding.inflate(inflater, container, false)

        adapter = ListAdapter(object: ListAdapter.ListViewItemClickListener {
            override fun onClick() {
                requireActivity().invalidateOptionsMenu()
            }
        })

        val recyclerView = binding.recyclerView
        val divider = DividerItemDecoration (context, LinearLayoutManager(context).orientation)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(divider)

        mPurchaseViewModel = ViewModelProvider(this).get(PurchaseViewModel::class.java)


        setHasOptionsMenu(true)

        return  binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPurchaseViewModel.getAllLocalData().observe(viewLifecycleOwner, Observer{
                purchase -> adapter.setData(purchase)
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        val menuId =
            if (this::adapter.isInitialized &&
                adapter.selectedItemCount() > 0
            ) {
                R.menu.delete_menu
            } else {
                R.menu.menu_main
            }
        inflater.inflate(menuId, menu)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete) {
            mPurchaseViewModel.deletePurchases(adapter.getSelectedList())
        }
        return super.onOptionsItemSelected(item)
    }

    // Implement logic to delete all users
    private fun deleteAllPurchase() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Yes") { _, _ ->
            mPurchaseViewModel.deleteAllPurchase(UserViewModel.username, UserViewModel.password)
            Toast.makeText(
                requireContext(),
                "Successfully removed everything",
                Toast.LENGTH_SHORT)
                .show()
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete everything ?")
        builder.setMessage("Are you sure to remove everything ?")
        builder.create().show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}