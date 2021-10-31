package com.danielmilano.countries.fragment.countries

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.danielmilano.countries.databinding.ItemCountryBinding
import com.danielmilano.domain.entities.Country

class CountryAdapter(private val listener: OnItemClickListener) : RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

    private val mDiffer: AsyncListDiffer<Country> = AsyncListDiffer(this, object : DiffUtil.ItemCallback<Country>() {
        override fun areItemsTheSame(oldItem: Country, newItem: Country): Boolean {
            return oldItem.code == newItem.code
        }

        override fun areContentsTheSame(oldItem: Country, newItem: Country): Boolean {
            return oldItem == newItem
        }
    })

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        return CountryViewHolder(ItemCountryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(mDiffer.currentList[position])
    }

    override fun getItemCount(): Int {
        return mDiffer.currentList.size
    }

    inner class CountryViewHolder(private val binding: ItemCountryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Country) {
            binding.root.setOnClickListener {
                listener.onItemClick(item)
            }
            binding.name.text = item.name
            binding.flag.load(item.flag)
        }
    }

    fun setData(list: List<Country>) {
        mDiffer.submitList(list)
    }

    interface OnItemClickListener {
        fun onItemClick(country: Country)
    }
}

