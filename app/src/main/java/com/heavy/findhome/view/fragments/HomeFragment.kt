package com.heavy.findhome.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.heavy.findhome.R
import com.heavy.findhome.databinding.FragmentHomeBinding
import com.heavy.findhome.model.ListFilterItem
import com.heavy.findhome.view.adapters.ListFilterAdapter

class HomeFragment : Fragment() {

    private var _binding:FragmentHomeBinding? = null;
    private val binding get() = _binding!!

    private lateinit var listFilter:ArrayList<ListFilterItem>
    private lateinit var obRecyclerView:RecyclerView
    private lateinit var obAdapter:ListFilterAdapter

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
        mLoadDataListFilter()
        mLoadRecycler()

        return view
    }

    private fun mChangeColorStatusBar(){
        activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(),R.color.colorBackground)
    }

    private fun mLoadDataListFilter(){
        listFilter = ArrayList()
        listFilter.add(ListFilterItem("Home", resources?.getDrawable(R.drawable.ic_filter_home, context?.theme), true))
        listFilter.add(ListFilterItem("Condominium", resources?.getDrawable(R.drawable.ic_filter_hotel, context?.theme), false))
        listFilter.add(ListFilterItem("Keys", resources?.getDrawable(R.drawable.ic_filter_key, context?.theme), false))
        listFilter.add(ListFilterItem("Percent", resources?.getDrawable(R.drawable.ic_filter_percent, context?.theme), false))

    }

    private fun mLoadRecycler(){
        obRecyclerView = binding.recyclerListFilters
        obRecyclerView.setHasFixedSize(true)
        val mLayoutManager = LinearLayoutManager(context)
        mLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        obRecyclerView.layoutManager =mLayoutManager
        obAdapter = ListFilterAdapter(listFilter, requireContext())
        obRecyclerView.adapter = obAdapter

    }

    override fun onResume() {
        super.onResume()
        //Hide bottom navigation bar moves up on keyboard
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
    }
}