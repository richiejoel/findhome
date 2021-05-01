package com.heavy.findhome.view.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.heavy.findhome.R
import com.heavy.findhome.databinding.ListSearchHomeBinding
import com.heavy.findhome.model.RentAparment

class ApartmentRecommendAdapter(private val listRentApartment: ArrayList<RentAparment>, private val ctx:Context ) : RecyclerView.Adapter<ApartmentRecommendAdapter.ApartementRecommendHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ApartementRecommendHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        return ApartementRecommendHolder(layoutInflater.inflate(R.layout.list_search_home, parent, false))
    }

    override fun onBindViewHolder(holder: ApartementRecommendHolder, position: Int) {
        val item:RentAparment = listRentApartment[position]
        holder.bind(item, ctx)
    }

    override fun getItemCount(): Int {
       return listRentApartment.size
    }

    class ApartementRecommendHolder(view: View): RecyclerView.ViewHolder(view),  View.OnClickListener {
        val binding = ListSearchHomeBinding.bind(view)
        lateinit var gobContext: Context
        lateinit var gobApartementRent:RentAparment

        fun bind(apartement: RentAparment, ctx:Context){
            gobContext = ctx
            gobApartementRent =  apartement
            binding.titleHome.text = apartement.nameAparment
            binding.nameLessor.text = apartement.nameOwner
            binding.numBathroom.text = apartement.numberBathroom.toString()
            binding.numBedroom.text = apartement.numberBedroom.toString()
            binding.numBell.text = apartement.numberBell.toString()
            binding.amountRent.text = apartement.amount.toString()
        }

        override fun onClick(view: View?) {
            Log.i("INFO", "Error not controlled")
        }

    }

}