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
import androidx.lifecycle.ViewModel
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.lf.esharing.database.user.UserViewModel
import com.lf.esharing.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater,container,false)
        //return inflater.inflate(R.layout.fragment_login, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var btnGoReg = binding.root.findViewById<Button>(R.id.btnGoReg)
        btnGoReg.setOnClickListener {
            it.findNavController().navigate(R.id.registrationFragment)
        }

        var btnLogin = binding.root.findViewById<Button>(R.id.btnLogin)
        btnLogin.setOnClickListener {
            userViewModel.login(binding.edtxtUser.text.toString(), binding.edtxtPass.text.toString())
                .observe(viewLifecycleOwner, Observer {
                    if (it == true) {
                        findNavController().navigate(R.id.dashboardFragment)
                    }else {
                        Toast.makeText(context, "Wrong username or password!", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}