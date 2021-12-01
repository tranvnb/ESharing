package com.lf.esharing

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.lf.esharing.database.user.UserEntity
import com.lf.esharing.database.user.UserViewModel
import com.lf.esharing.databinding.FragmentRegistrationBinding
import com.lf.esharing.utils.Validator

class RegistrationFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationBinding
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistrationBinding.inflate(inflater,container,false)
        // Inflate the layout for this fragment
        //val v = inflater.inflate(R.layout.fragment_registration, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var btnRegSub = binding.root.findViewById<Button>(R.id.btnRegSub)
        btnRegSub.setOnClickListener {
            val username = binding.edtxtEmail.text.toString()
            val password = binding.edtxtRegPass.text.toString()
            val confirm = binding.edtxtConPass.text.toString()
            val firstname = binding.edtxtName.text.toString()
            val lastname = binding.edtxtLstName.text.toString()

            if (Validator.registrationInputCheck(username, password, confirm, firstname, lastname)) {
                val user = UserEntity(username, password, firstname, lastname, true)
                userViewModel.signup(user).observe(viewLifecycleOwner, Observer {
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                })
            } else {
                Toast.makeText(context, "Please input valid values.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}