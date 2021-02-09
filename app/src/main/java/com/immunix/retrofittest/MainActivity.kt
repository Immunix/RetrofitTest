package com.immunix.retrofittest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.immunix.retrofittest.databinding.ActivityMainBinding
import retrofit2.HttpException
import java.io.IOException

const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var todoAdapter: TodoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initRecycler()

        lifecycleScope.launchWhenCreated {
            binding.progressBar.isVisible = true
            val response = try {
                RetrofitInstance.api.getTodos()
            } catch (e: IOException) {
                binding.progressBar.isVisible = false
                Log.e(TAG, "Check internet connectivity")
                return@launchWhenCreated
            } catch (e: HttpException) {
                binding.progressBar.isVisible = false
                Log.e(TAG, "Bad response")
                return@launchWhenCreated
            }
            if (response.isSuccessful && response.body() != null) {
                todoAdapter.todoList = response.body()!!
            } else {
                Log.e(TAG, "no response")
            }
            binding.progressBar.isVisible = false
        }
    }

    private fun initRecycler() = binding.recycler.apply {
        todoAdapter = TodoAdapter()
        layoutManager = LinearLayoutManager(this@MainActivity)
        adapter = todoAdapter
    }
}