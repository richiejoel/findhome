package com.heavy.findhome.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.findhome.R
import com.example.findhome.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding:FragmentHomeBinding? = null;
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        /*homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)*/
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        //val textView: TextView = root.findViewById(R.id.text_home)
       /* homeViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })*/
        mChangeColorStatusBar()
        return view
    }

    private fun mChangeColorStatusBar(){
        activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(),R.color.colorBackground)
    }
}