package com.linha.expensetracker.ui.add

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.todoapp.utils.DatePickerFragment
import com.linha.expensetracker.R
import com.linha.expensetracker.databinding.ActivityAddExpenseBinding
import com.linha.expensetracker.ui.ViewModelFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddExpenseActivity : AppCompatActivity(), DatePickerFragment.DialogDateListener {

    private var dueDateMillis: Long = System.currentTimeMillis()
    private lateinit var binding: ActivityAddExpenseBinding
    private lateinit var viewModel: AddExpenseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        supportActionBar?.title = getString(R.string.add_expense)
        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[AddExpenseViewModel::class.java]

        val categoryDropdown = binding.autoCategoryTv
        val items = resources.getStringArray(R.array.category_dropdown)
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, items)
        categoryDropdown.setAdapter(adapter)

        binding.btnAddExpense.setOnClickListener {
            val title = binding.addEdTitle.text.toString()
            val expense = binding.addEdExpense.text.toString().toInt()

            val categoryChoosed = when(binding.autoCategoryTv.text.toString()) {
                "Transportasi" -> 0
                "Makanan" -> 1
                "Minuman" -> 2
                else -> -1 // Default case if category doesn't match
            }

            val dateStr = binding.addTvDueDate.text.toString()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = dateFormat.parse(dateStr)?.time ?: System.currentTimeMillis()

            viewModel.addExpense(title, expense, date, categoryChoosed)
            finish()
        }
    }

    fun showDatePicker(view: View) {
        val dialogFragment = DatePickerFragment()
        dialogFragment.show(supportFragmentManager, "datePicker")
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        findViewById<TextView>(R.id.add_tv_due_date).text = dateFormat.format(calendar.time)

        dueDateMillis = calendar.timeInMillis
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