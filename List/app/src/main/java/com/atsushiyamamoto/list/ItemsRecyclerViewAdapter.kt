package com.atsushiyamamoto.list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.row_main.view.*

/**
 * Created by atsushiyamamoto on 2018/01/25.
 */
class ItemsRecyclerViewAdapter(private val items: MutableList<String>): RecyclerView.Adapter<ItemsRecyclerViewAdapter.ItemHolder>() {

    override fun onBindViewHolder(holder: ItemsRecyclerViewAdapter.ItemHolder, position: Int) {
        println("onBindViewHolder")
        val item = items[position]
        holder.bindItem(position, item)
    }

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsRecyclerViewAdapter.ItemHolder {
        println("onCreateViewHolder")
        val inflater = LayoutInflater.from(parent.context)
        val inflateView = inflater.inflate(R.layout.row_main, parent, false)
        return ItemHolder(inflateView)
    }

    fun removeItemAt(position: Int) {
        items.removeAt(position)

        // Without two lines below, removed items don't disappear immediately
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    fun addItemAt(position: Int, item: String) {
        items.add(position, item)

        // Without two lines below, added items don't show up immediately
        notifyItemInserted(position)
        notifyItemRangeChanged(position, itemCount)
    }

    inner class ItemHolder(v: View): RecyclerView.ViewHolder(v), View.OnClickListener {

        private val view = v
        private var item = ""
        private var itemPosition = 0

        init {
            v.setOnClickListener(this)
            v.rowMainButtonDelete.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            if (v == view.rowMainButtonDelete) {
                removeItemAt(this.itemPosition)

            } else {
                val context = itemView.context
                //            TODO: Intent
            }
        }

        fun bindItem(position: Int, item: String) {
            this.item = item
            this.itemPosition = position
            view.rowMainTextView.text = item
        }
    }

    companion object {
        private val ITEM_KEY = "ITEM" // This is for Intent.
    }

}