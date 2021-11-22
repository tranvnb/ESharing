package com.lf.esharing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.lf.esharing.database.user.UserEntity
import com.lf.esharing.database.user.UserViewModel
import com.lf.esharing.databinding.FragmentDashboardBinding
import com.lf.esharing.utils.MoshiHelper

class DashboardFragment : Fragment() {

    private lateinit var binding: FragmentDashboardBinding
    private val userViewModel: UserViewModel by viewModels()

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
        userViewModel.getUsers().observe(viewLifecycleOwner, Observer {
            println(MoshiHelper.toJson(UserEntity::class.java, it))
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var btnAddExpenses = binding.root.findViewById<Button>(R.id.btnAddExpenses)
        btnAddExpenses.setOnClickListener {
            it.findNavController().navigate(R.id.addexpensesFragment)
        }
    }
}