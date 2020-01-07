package com.example.truyenqq.module.adapters

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.truyenqq.R
import com.example.truyenqq.module.models.Category
import com.example.truyenqq.ui.activities.newactivity.ActivityNewUpdate
import com.example.truyenqq.ui.fragment.category.FragmentCategory


class AdapterCategory(
    val context: FragmentCategory,
    var list: MutableList<Category>
) : RecyclerView.Adapter<AdapterCategory.BaseItem>(), Filterable {

    private var searchList: List<Category>? = null

    init {
        this.searchList = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseItem {
        val view: View = LayoutInflater.from(context.context).inflate(R.layout.item_category, parent, false)
        return BaseItem(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: BaseItem, position: Int) {
        val p0 = list[position]
        holder.name.text = p0.name
        holder.itemView.setOnClickListener {
            val intent = Intent(context.context, ActivityNewUpdate::class.java)
            val bundle = Bundle()
            bundle.putString("name", "Truyện ${p0.name}")
            bundle.putString("category", p0.id)
            bundle.putString("col", "modified")
            intent.putExtra("kind", bundle)
            context.startActivity(intent)
        }
        holder.itemView.setOnLongClickListener {
            hideKeyBoard(context)
            var dialog = androidx.appcompat.app.AlertDialog.Builder(context.context!!)
            val view: View = LayoutInflater.from(context.context!!).inflate(R.layout.item_category_longclick, null)
            val _info = view.findViewById<TextView>(R.id.tvCategoryInfo)
            _info.text = p0.info
            dialog.setView(view)
            val dislay = dialog.create()
            dislay.setTitle(p0.name)
            dislay.show()
            true
        }
    }

    fun hideKeyBoard(context: FragmentCategory) {
        val imm = context.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(context.activity?.currentFocus?.windowToken, 0)
    }

    fun updateData(items: MutableList<Category>) {
        list = items
    }

    inner class BaseItem(view: View) : RecyclerView.ViewHolder(view) {
        var name = view.findViewById<TextView>(R.id.tvCategoryName)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                if (charString.isEmpty()) {
                    searchList = list
                } else {
                    val filteredList = ArrayList<Category>()
                    for (row in list) {
                        if (row.name.toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row)
                        }
                    }
                    searchList = filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = searchList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                list = filterResults.values as ArrayList<Category>
                notifyDataSetChanged()
            }
        }
    }

}


