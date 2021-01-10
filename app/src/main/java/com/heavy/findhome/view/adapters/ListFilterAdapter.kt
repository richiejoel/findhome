package com.heavy.findhome.view.adapters

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.findhome.R
import com.example.findhome.databinding.ListFiltersHomeBinding
import com.heavy.findhome.model.ListFilterItem

class ListFilterAdapter(private val listFilter: ArrayList<ListFilterItem>, private val ctx: Context) : RecyclerView.Adapter<ListFilterAdapter.ListFilterViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListFilterViewHolder {

        val layoutInflater:LayoutInflater = LayoutInflater.from(parent.context)
        return ListFilterViewHolder(layoutInflater.inflate(R.layout.list_filters_home, parent, false))
    }

    override fun getItemCount(): Int {
        return listFilter.size
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ListFilterViewHolder, position: Int) {
       val item:ListFilterItem = listFilter[position]
        holder.bind(item, ctx)

    }

    class ListFilterViewHolder(view:View) : RecyclerView.ViewHolder(view),  View.OnClickListener {
        val binding = ListFiltersHomeBinding.bind(view)
        lateinit var ctx:Context
        lateinit var itemList:ListFilterItem

        @RequiresApi(Build.VERSION_CODES.M)
        fun bind(listFilter:ListFilterItem, pCtx: Context){
            ctx = pCtx
            itemList = listFilter
            binding.imgFilter.setImageDrawable(listFilter.iconFilter)
            binding.txtFilter.text = listFilter.titleFilter
            binding.imgFilter.setOnClickListener(this)
            binding.imgFilter.setColorFilter(ctx.resources.getColor(R.color.colorWhite, ctx.theme))

        }

        @RequiresApi(Build.VERSION_CODES.M)
        override fun onClick(view: View?) {
            when(view?.id){
                binding.imgFilter.id -> mChangeSelectedItem()
                else -> {
                    Log.i("ERROR", "Error not controlled")
                }
            }
        }

        @RequiresApi(Build.VERSION_CODES.M)
        fun mChangeSelectedItem(){
            /*if(itemList?.selected!!){
                itemList.selected = false
                binding.cardViewFilter.setBackgroundColor(ctx.resources.getColor(R.color.sky_primary, ctx.theme))
                binding.imgFilter.setColorFilter(ctx.resources.getColor(R.color.colorWhite, ctx.theme))
                /*val params: ViewGroup.LayoutParams = binding.cardViewFilter.layoutParams
                params.width = 100
                params.height = 130
                binding.cardViewFilter.layoutParams = params*/
            } else {
                itemList.selected = true
                binding.cardViewFilter.setBackgroundColor(ctx.resources.getColor(R.color.colorWhite, ctx.theme))
                binding.imgFilter.setColorFilter(ctx.resources.getColor(R.color.colorFilterNoSelected, ctx.theme))
                /*val params: ViewGroup.LayoutParams = binding.cardViewFilter.layoutParams
                params.width = 100
                params.height = 110
                binding.cardViewFilter.layoutParams = params*/
            }*/
        }

    }


}