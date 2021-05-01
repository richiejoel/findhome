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
import com.heavy.findhome.model.RentAparment
import com.heavy.findhome.view.adapters.ApartmentRecommendAdapter
import com.heavy.findhome.view.adapters.ListFilterAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null;
    private val binding get() = _binding!!

    private lateinit var listFilter: ArrayList<ListFilterItem>
    private lateinit var listApartementRecommended: ArrayList<RentAparment>
    private lateinit var obRecyclerView: RecyclerView
    private lateinit var obAdapter: ListFilterAdapter
    private lateinit var obRecyclerListApartments: RecyclerView
    private lateinit var obAdapterApartments: ApartmentRecommendAdapter

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
        mLoadPromoApartments()

        return view
    }

    private fun mChangeColorStatusBar() {
        activity?.window?.statusBarColor = ContextCompat.getColor(requireContext(), R.color.colorBackground)
    }

    private fun mLoadDataListFilter() {
        listFilter = ArrayList()
        listFilter.add(ListFilterItem("Home", resources?.getDrawable(R.drawable.ic_filter_home, context?.theme), true))
        listFilter.add(ListFilterItem("Condominium", resources?.getDrawable(R.drawable.ic_filter_hotel, context?.theme), false))
        listFilter.add(ListFilterItem("Keys", resources?.getDrawable(R.drawable.ic_filter_key, context?.theme), false))
        listFilter.add(ListFilterItem("Percent", resources?.getDrawable(R.drawable.ic_filter_percent, context?.theme), false))

        //List Apartement Recommend
        listApartementRecommended = ArrayList()
        listApartementRecommended.add(RentAparment("Special House mix", "Richard Garcia", 1100f, 4, 2, 3))
        listApartementRecommended.add(RentAparment("PentHouse Full", "Louis Tomlinson", 4500f, 6, 3, 8))
        listApartementRecommended.add(RentAparment("Apartment Basic", "Olivia Rodrigo", 400f, 1, 1, 0))
        listApartementRecommended.add(RentAparment("Special Apartment", "Taylor Switf", 1500f, 2, 4, 2))
        listApartementRecommended.add(RentAparment("House with Gym", "Conan Gray", 2500f, 5, 2, 3))
        listApartementRecommended.add(RentAparment("Apartment Pool", "Harry Styles", 3500.45f, 6, 4, 5))
        listApartementRecommended.add(RentAparment("Poor apartment", "Arianna Grande", 500f, 2, 1, 1))

    }

    private fun mLoadRecycler() {
        obRecyclerView = binding.recyclerListFilters
        obRecyclerView.setHasFixedSize(true)
        val mLayoutManager = LinearLayoutManager(context)
        mLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        obRecyclerView.layoutManager = mLayoutManager
        obAdapter = ListFilterAdapter(listFilter, requireContext())
        obRecyclerView.adapter = obAdapter



    }

    private fun mLoadPromoApartments() {
        obRecyclerListApartments = binding.recyclerListRent
        obRecyclerListApartments.setHasFixedSize(true)
        val mLayoutManagerRent = LinearLayoutManager(context)
        mLayoutManagerRent.orientation = LinearLayoutManager.VERTICAL
        obRecyclerListApartments.layoutManager = mLayoutManagerRent
        obAdapterApartments = ApartmentRecommendAdapter(listApartementRecommended, requireContext())
        obRecyclerListApartments.adapter = obAdapterApartments
    }

    override fun onResume() {
        super.onResume()
        //Hide bottom navigation bar moves up on keyboard
        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
    }
}