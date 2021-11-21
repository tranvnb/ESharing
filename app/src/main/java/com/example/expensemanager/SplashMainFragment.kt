package com.example.expensemanager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import com.example.expensemanager.databinding.FragmentSplashMainBinding

class SplashMainFragment : Fragment() {

    private lateinit var binding: FragmentSplashMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSplashMainBinding.inflate(inflater,container,false)
        //val v = inflater.inflate(R.layout.fragment_splash_main, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var btnLetGo = binding.root.findViewById<Button>(R.id.btnLetGo)
        btnLetGo.setOnClickListener {
            it.findNavController().navigate(R.id.loginFragment)
        }
    }
}