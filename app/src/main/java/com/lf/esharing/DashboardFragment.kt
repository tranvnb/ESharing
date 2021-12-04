package com.lf.esharing

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.lf.esharing.database.purchase.PurchaseEntity
import com.lf.esharing.database.purchase.PurchaseViewModel
import com.lf.esharing.database.user.UserViewModel
import com.lf.esharing.databinding.FragmentDashboardBinding
import com.lf.esharing.network.SocketIOClient
import com.lf.esharing.network.SocketIOClient.APPROVED
import com.lf.esharing.network.SocketIOClient.FROM_OWNER
import com.lf.esharing.network.SocketIOClient.MESSAGE
import com.lf.esharing.network.SocketIOClient.TO_USER
import com.lf.esharing.utils.MoshiHelper

class DashboardFragment : Fragment() {

    private lateinit var binding: FragmentDashboardBinding
    private val userViewModel: UserViewModel by viewModels()
    private val purchaseViewModel: PurchaseViewModel by viewModels()
    private lateinit var alertDialog: AlertDialog.Builder
    private lateinit var myContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateMembers()

        updateLatestPurchases()

        val removeMemberDialog = AlertDialog.Builder(requireContext())
            .setTitle("Remove a member")
            .setView(layoutInflater.inflate(R.layout.dialog_remove_member,  null))
            .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface, i ->
                dialogInterface.dismiss()
            })
            .setPositiveButton("Remove", DialogInterface.OnClickListener { dialogInterface, i ->
                val member: String = (dialogInterface as AlertDialog).findViewById<EditText>(R.id.edtMemberName).text.toString()
                userViewModel.removeMember(member).observe(viewLifecycleOwner, Observer {
                    if (it != null && it.has("code") && it.getInt("code") == 200) {
                        // Update members
                        val newMembers = UserViewModel.currentMembers.value?.toMutableList()
                        newMembers?.remove(member)
                        UserViewModel.currentMembers.postValue(newMembers)
                        SocketIOClient.notifyRemovedMember(UserViewModel.username, member, "You were removed from our house")
                        updateMembers()
                    }
                    Toast.makeText(context, it?.getString("message"), Toast.LENGTH_SHORT).show()
                })
                dialogInterface.dismiss()
            })

        this.myContext = requireActivity().applicationContext
        alertDialog = AlertDialog.Builder(this.myContext)
        startSocketConnection()

        val btnAddExpenses = binding.btnAddExpenses
        btnAddExpenses.setOnClickListener {
            it.findNavController().navigate(R.id.action_dashboardFragment_to_addexpenseFragment)
        }

        val btnTrackExpenses = binding.btnTrackExpenses
        btnTrackExpenses.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_displayexpensesFragment)
        }

        val btnRequestJoinHouse = binding.btnRequestJoinHouse
        btnRequestJoinHouse.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_request_join_household, null)
            val alertDialog = AlertDialog.Builder(context)
                .setCancelable(true)
                .setView(dialogView)
                .setPositiveButton("Request", DialogInterface.OnClickListener { dialogInterface, i ->
                    val ownerName = dialogView.findViewById<EditText>(R.id.edtOwnerName).text.toString()
                    if (TextUtils.isEmpty(ownerName)) {
                        Toast.makeText(context, "Please enter owner name.", Toast.LENGTH_SHORT).show()
                    } else {
                        requestJoinHousehold(ownerName)
                        dialogInterface.dismiss()
                    }
                })
                .setNegativeButton("Cancel", DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.dismiss()
                })

            alertDialog.show()
        }

        val btnSignOut = binding.btnSignOut
        btnSignOut.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_loginFragment)
        }

        val btnShowMembers = binding.btnShowMembers
        btnShowMembers.setOnClickListener {
            Snackbar.make(view, "Your current members are: " + UserViewModel.currentMembers.value.toString(), Snackbar.LENGTH_LONG).show()
        }

        val btnRemoveMember = binding.btnRemoveMembers
        btnRemoveMember.setOnClickListener {
            removeMemberDialog.show()
        }
    }



    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    private fun requestJoinHousehold(ownerName: String) {
        SocketIOClient.requestJoinHousehold(requireContext(), ownerName, UserViewModel.username)
            .observe(viewLifecycleOwner, Observer {
                val message = it.getString(MESSAGE)
                Toast.makeText(
                    context,
                    message,
                    Toast.LENGTH_SHORT
                ).show()
                if (it.has(APPROVED) && it.getBoolean(APPROVED)) {
                    UserViewModel.currentMembers.postValue(emptyList())
                    UserViewModel.currentMembers.postValue(listOf(it.getString(FROM_OWNER)))
                    binding.btnRequestJoinHouse.visibility = GONE
                    binding.btnShowMembers.visibility = VISIBLE
                    binding.btnRemoveMembers.visibility = VISIBLE
                }
            })
    }

    private fun startSocketConnection() {
        SocketIOClient.registerOnJoinHouseholdRequest().observe(requireActivity() as LifecycleOwner, Observer {
            val people = it.getString(SocketIOClient.FROM_USER)
            val owner = it.getString(SocketIOClient.TO_OWNER)
            AlertDialog.Builder(requireContext())
                .setTitle("Join Household Request")
                .setMessage(people + " want to join your household")
                .setNegativeButton("Reject", DialogInterface.OnClickListener { dialogInterface, i ->
                    SocketIOClient.responseJoinHouseholdRequest(owner, people, "Sorry, I dont want.", false)
                    dialogInterface.dismiss()
                })
                .setPositiveButton("Approve", DialogInterface.OnClickListener { dialogInterface, i ->
                    SocketIOClient.responseJoinHouseholdRequest(owner, people, "Okay let be together", true)
                    userViewModel.addMember(people).observe(viewLifecycleOwner, Observer {
                        if (it != null && it.has("code") && it.getInt("code") == 200) {
                            // Update members
                            val newMembers = UserViewModel.currentMembers.value?.toMutableList()
                            newMembers?.add(people)
                            UserViewModel.currentMembers.postValue(newMembers)
                            binding.btnRequestJoinHouse.visibility = GONE
                            binding.btnShowMembers.visibility = VISIBLE
                            binding.btnRemoveMembers.visibility = VISIBLE
                        }
                        Toast.makeText(context, it?.getString("message"), Toast.LENGTH_SHORT).show()
                    })
                    dialogInterface.dismiss()
                }).show()
        })
        SocketIOClient.registerOnBeingRemovedFromHouseHold().observe(requireActivity() as LifecycleOwner, Observer {
            val people = it.getString(TO_USER)
            val owner = it.getString(FROM_OWNER)
            val message = it.getString(MESSAGE)
            Toast.makeText(context, "You ware removed from $owner household", Toast.LENGTH_LONG).show()
            updateMembers()
        })
    }

    private fun updateLatestPurchases() {
        userViewModel.getPurchases(UserViewModel.username).observe(viewLifecycleOwner, Observer {
            purchaseViewModel.deleteAllLocalPurchase()
            if (it != null) {
                purchaseViewModel.insertLocalPurchase(it)
            }
        })
    }

    private fun updateMembers() {
        userViewModel.getMembers(UserViewModel.username).observe(viewLifecycleOwner, Observer {
            if (it != null && it.count() > 0) {
                UserViewModel.currentMembers.postValue(emptyList())
                UserViewModel.currentMembers.postValue(it)
                binding.btnRequestJoinHouse.visibility = GONE
                binding.btnShowMembers.visibility = VISIBLE
                binding.btnRemoveMembers.visibility = VISIBLE
            } else {
                UserViewModel.currentMembers.postValue(emptyList())
                binding.btnRequestJoinHouse.visibility = VISIBLE
                binding.btnShowMembers.visibility = GONE
                binding.btnRemoveMembers.visibility = GONE
            }
        })
    }

}