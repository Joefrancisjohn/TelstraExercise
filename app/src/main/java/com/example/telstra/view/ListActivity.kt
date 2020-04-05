package com.example.telstra.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.telstra.R
import com.example.telstra.databinding.ActivityMainBinding
import com.example.telstra.viewModel.ListActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class ListActivity : AppCompatActivity() {


    private val listActivityViewModel by lazy {
        ViewModelProvider(this).get(ListActivityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setSupportActionBar(toolbar)
        binding.lifecycleOwner = this
        binding.viewModel = listActivityViewModel

        val rvAdapter = FactsListAdaper()
        rv_fact_list.layoutManager = LinearLayoutManager(this)
        rv_fact_list.adapter = rvAdapter
        rv_fact_list.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        listActivityViewModel.getFactsFromRepo()

        listActivityViewModel.factsToList.observe(this , Observer {
            //Display the detials if facts data is not null display empty list if data is null
            it?.let{
                title = it.title
                rvAdapter.swapData(it.rows)
            }?: kotlin.run {
                title = "Error"
                rvAdapter.swapData(emptyList())
            }
        })

        binding.fab.setOnClickListener {
            listActivityViewModel.getFactsFromRepo()
        }

    }
}
