package com.linha.expensetracker.ui.home

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.paging.PagedList
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.linha.expensetracker.R
import com.linha.expensetracker.data.Expense
import com.linha.expensetracker.databinding.ActivityHomeBinding
import com.linha.expensetracker.ui.ViewModelFactory
import com.linha.expensetracker.ui.add.AddExpenseActivity
import com.linha.expensetracker.utils.Event
import com.linha.expensetracker.utils.ExpenseFilterType

class HomeActivity : AppCompatActivity() {

    private lateinit var binding : ActivityHomeBinding
    private lateinit var recycler: RecyclerView
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        setupView()

        binding.fab.setOnClickListener {
            val addIntent = Intent(this, AddExpenseActivity::class.java)
            startActivity(addIntent)
        }

        val layoutManager = LinearLayoutManager(this)
        val expenseRv: RecyclerView = findViewById(R.id.rv_expense)
        recycler = expenseRv
        recycler.layoutManager = layoutManager
        recycler.setHasFixedSize(true)

        initAction()

        val factory = ViewModelFactory.getInstance(this)
        homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]

        homeViewModel.expense.observe(this, Observer(this::showRecyclerView))

    }

    private fun showRecyclerView(expense: PagedList<Expense>) {
        val adapter = HomeAdapter()
        val rv: RecyclerView = findViewById(R.id.rv_expense)
        rv.adapter = adapter
        homeViewModel.expense.observe(this) {
            adapter.submitList(expense)
        }
    }

    private fun showSnackBar(eventMessage: Event<Int>) {
        val message = eventMessage.getContentIfNotHandled() ?: return
        Snackbar.make(
            findViewById(R.id.coordinator_layout),
            getString(message),
            Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_filter -> {
                showFilteringPopUpMenu()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showFilteringPopUpMenu() {
        val view = findViewById<View>(R.id.action_filter) ?: return
        PopupMenu(this, view).run {
            menuInflater.inflate(R.menu.filter_tasks, menu)

            setOnMenuItemClickListener {
                homeViewModel.filter(
                    when (it.itemId) {
                        R.id.category_transportasi -> ExpenseFilterType.TRANSPORTASI
                        R.id.category_makanan -> ExpenseFilterType.MAKANAN
                        R.id.category_minuman -> ExpenseFilterType.MINUMAN
                        R.id.category_semua -> ExpenseFilterType.ALL_CATEGORY
                        else -> ExpenseFilterType.ALL_CATEGORY
                    }
                )
                true
            }
            show()
        }
    }

    private fun initAction() {
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                return makeMovementFlags(0, ItemTouchHelper.RIGHT)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val task = (viewHolder as HomeAdapter.ExpenseViewHolder).getExpense
                homeViewModel.deleteTask(task)
            }

        })
        itemTouchHelper.attachToRecyclerView(recycler)
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }
}