package com.lf.esharing

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.lf.esharing.database.purchase.PurchaseEntity
import com.lf.esharing.database.purchase.PurchaseViewModel
import com.lf.esharing.database.user.UserEntity
import com.lf.esharing.database.user.UserViewModel
import com.lf.esharing.databinding.FragmentDashboardBinding
import com.lf.esharing.network.SocketIOClient
import com.lf.esharing.network.SocketIOClient.FROM_OWNER
import com.lf.esharing.network.SocketIOClient.JOIN_HOUSEHOLD_REQUEST
import com.lf.esharing.network.SocketIOClient.MESSAGE
import com.lf.esharing.network.SocketIOClient.TO_USER
import com.lf.esharing.utils.MoshiHelper
import io.socket.emitter.Emitter
import org.json.JSONObject

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
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_dashboard, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        userViewModel.getUsers().observe(viewLifecycleOwner, Observer {
//            println(MoshiHelper.toJson(UserEntity::class.java, it))
//        })
//        userViewModel.getMembers("username").observe(viewLifecycleOwner, Observer {
//            println(MoshiHelper.toJson(String::class.java, it))
//        })
//
//        userViewModel.getPurchases("username").observe(viewLifecycleOwner, Observer {
//            // synchronize with remote database
//            if (it !== null) {
//                // delete first then add later - FOLLOW ORDER
//                purchaseViewModel.deleteAllLocalPurchase()
//                purchaseViewModel.insertLocalPurchase(it)
//            }
//            println(MoshiHelper.toJson(PurchaseEntity::class.java, it))
//        })


        alertDialog = AlertDialog.Builder(requireContext()).setTitle("Join Household Request")
//            .setMessage(people + " want to join your household")
//            .setNegativeButton("Reject", DialogInterface.OnClickListener { dialogInterface, i ->
//                SocketIOClient.rejectJoinHouseholdRequest(owner, people, "Sorry, I dont want.")
//                dialogInterface.dismiss()
//            })
//            .setPositiveButton("Approve", DialogInterface.OnClickListener { dialogInterface, i ->
//                SocketIOClient.rejectJoinHouseholdRequest(owner, people, "Okay let be together")
//                // TODO: add new member to household
//                dialogInterface.dismiss()
//            })
        this.myContext = requireActivity().applicationContext
        alertDialog = AlertDialog.Builder(this.myContext)
        startSocketConnection(this.myContext)

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
                if (it.has(TO_USER)) {
                    val user = it.getString(TO_USER)
                    val fromOwner = it.getString(FROM_OWNER)
                    val message = it.getString(MESSAGE)
                    Toast.makeText(
                        context,
                        "Reply from $fromOwner to your request: $message",
                        Toast.LENGTH_SHORT
                    ).show()
                }else{
                    val message = it.getString(MESSAGE)
                    Toast.makeText(
                        context,
                        message,
                        Toast.LENGTH_SHORT
                    ).show()

//                                    alertDialog = AlertDialog.Builder(requireContext())
//                                        .setTitle("Join Household Request")
//                                    alertDialog.setMessage("people" + " want to join your household")
//                                        .setNegativeButton("Reject", DialogInterface.OnClickListener { dialogInterface, i ->
////                                            SocketIOClient.rejectJoinHouseholdRequest(owner, people, "Sorry, I dont want.")
//                                            dialogInterface.dismiss()
//                                        })
//                                        .setPositiveButton("Approve", DialogInterface.OnClickListener { dialogInterface, i ->
////                                            SocketIOClient.rejectJoinHouseholdRequest(owner, people, "Okay let be together")
//                                            // TODO: add new member to household
//                                            dialogInterface.dismiss()
//                                        })
//
//                                    alertDialog.show()
                }
            })
    }

    private fun startSocketConnection(mContext: Context) {
        SocketIOClient.registerOnJoinHouseholdRequest(mContext).observe(viewLifecycleOwner, Observer {
            val people = it.getString(SocketIOClient.FROM_USER)
            val owner = it.getString(SocketIOClient.TO_OWNER)
            AlertDialog.Builder(requireContext())
                .setTitle("Join Household Request")
                .setMessage(people + " want to join your household")
                .setNegativeButton("Reject", DialogInterface.OnClickListener { dialogInterface, i ->
                    SocketIOClient.rejectJoinHouseholdRequest(owner, people, "Sorry, I dont want.")
                    dialogInterface.dismiss()
                })
                .setPositiveButton("Approve", DialogInterface.OnClickListener { dialogInterface, i ->
                    SocketIOClient.rejectJoinHouseholdRequest(owner, people, "Okay let be together")
                    // TODO: add new member to household
                    dialogInterface.dismiss()
                }).show()
        })
    }
}