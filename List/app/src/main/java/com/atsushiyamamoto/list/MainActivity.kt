package com.atsushiyamamoto.list

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.KeyEvent
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnKeyListener {

    private val items = mutableListOf("Fizz", "Buzz")
    private lateinit var adapter: ItemsRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val linearLayoutManager = LinearLayoutManager(this)
        mainRecyclerView.layoutManager = linearLayoutManager

        adapter = ItemsRecyclerViewAdapter(items)
        mainRecyclerView.adapter = adapter

        val dividerItemDecoration = DividerItemDecoration(mainRecyclerView.context, linearLayoutManager.orientation)
        mainRecyclerView.addItemDecoration(dividerItemDecoration)

        mainEditText.setOnKeyListener(this)
    }

    override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
        if(event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
            if (v == mainEditText && !mainEditText.text.isBlank()) {
                adapter.addItemAt(adapter.itemCount, mainEditText.text.toString())
                mainEditText.setText("")
            }

            return true;
        }

        return false;
    }
}
