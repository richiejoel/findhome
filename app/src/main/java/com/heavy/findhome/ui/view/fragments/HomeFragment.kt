package com.heavy.findhome.ui.view.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.heavy.findhome.R
import com.heavy.findhome.databinding.FragmentHomeBinding
import com.heavy.findhome.data.model.ListFilterItem
import com.heavy.findhome.data.model.RentAparment
import com.heavy.findhome.ui.view.LoginActivity
import com.heavy.findhome.ui.view.adapters.ApartmentRecommendAdapter
import com.heavy.findhome.ui.view.adapters.ListFilterAdapter
import com.heavy.findhome.ui.viewModel.DashboardViewModel
import com.heavy.findhome.ui.viewModel.LoginViewModel
import com.heavy.findhome.ui.viewModel.RegisterViewModel

class HomeFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentHomeBinding? = null;
    private val binding get() = _binding!!

    //private val loginViewModel:LoginViewModel by activityViewModels()
    //private val registerViewModel:RegisterViewModel by viewModels()
    private val dashboardViewModel:DashboardViewModel by activityViewModels()

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

        binding.settings.setOnClickListener(this)


        /*loginViewModel.currentUser.observe(requireActivity(), Observer {
            binding.txtUsername.text = it?.email
        })*/


        /*registerViewModel.currentUser.observe(requireActivity(), Observer {
            binding.txtUsername.text = it.name
        })*/

        /*loginViewModel.snackBar.observe(viewLifecycleOwner, Observer{
            it?.let {
                Snackbar.make(binding.root, it!!, Snackbar.LENGTH_LONG).show()
                loginViewModel.onSnackBarShown()
            }
        })*/

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /*loginViewModel.currentUser.observe(requireActivity(), Observer {
            binding.txtUsername.text = it?.email
        })*/
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

    override fun onClick(view: View?) {
        when(view?.id){
            binding.settings.id -> mLogOutUser("EMAIL")
            else -> {
                Log.i("ERROR", "Error not controlled")
            }
        }
    }

    private fun mLogOutUser(provider: String?){
        dashboardViewModel.mLogOutUser(provider)
        mStartLoginActivity()
    }

    private fun mStartLoginActivity(){
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
        activity?.startActivity(intent)
        activity?.finish()
    }


}