package com.linha.expensetracker.ui.detail

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.todoapp.utils.DateConverter
import com.dicoding.todoapp.utils.DatePickerFragment
import com.linha.expensetracker.R
import com.linha.expensetracker.data.Expense
import com.linha.expensetracker.databinding.ActivityExpenseDetailBinding
import com.linha.expensetracker.ui.ViewModelFactory
import com.linha.expensetracker.utils.EXPENSE_ID
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ExpenseDetailActivity : AppCompatActivity(), DatePickerFragment.DialogDateListener {

    private var dueDateMillis: Long = System.currentTimeMillis()
    private lateinit var binding : ActivityExpenseDetailBinding
    private lateinit var expenseDetailViewModel: ExpenseDetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExpenseDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()

        val factory = ViewModelFactory.getInstance(this)
        expenseDetailViewModel = ViewModelProvider(this, factory)[ExpenseDetailViewModel::class.java]

        // Get task ID from intent
        val taskId = intent.getIntExtra(EXPENSE_ID, 1)

        expenseDetailViewModel.setTaskId(taskId)

        expenseDetailViewModel.expense.observe(this) { task ->
            task?.let {
                displayTaskDetails(it)
            }
        }

        binding.btnEditExpense.setOnClickListener {
            val title = binding.detailEdTitle.text.toString()
            val expense = binding.detailEdExpense.text.toString().toInt()

            val categoryChoosed = when(binding.autoCategoryTv.text.toString()) {
                "Transportasi" -> 0
                "Makanan" -> 1
                "Minuman" -> 2
                else -> -1 // Default case if category doesn't match
            }

            val dateStr = binding.detailEdDueDate.text.toString()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val date = dateFormat.parse(dateStr)?.time ?: System.currentTimeMillis()

            expenseDetailViewModel.updateExpense(taskId, title, expense, date, categoryChoosed)
            finish()
        }

        binding.btnDeleteExpense.setOnClickListener {
            expenseDetailViewModel.deleteTask()
            finish()
        }
    }
//s
    private fun displayTaskDetails(expense: Expense) {
        binding.detailEdTitle.setText(expense.title)
        binding.detailEdExpense.setText(expense.expense.toString())

        val date = DateConverter.convertMillisToString(expense.dueDateMillis)
        binding.detailEdDueDate.text = date

        val category = when(expense.category){
            0-> "Transportasi"
            1-> "Makanan"
            2 -> "Minuman"
            else -> "Lainnya"
        }
        binding.autoCategoryTv.setText(category)

        val categoryDropdown = binding.autoCategoryTv
        val items = resources.getStringArray(R.array.category_dropdown)
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, items)
        categoryDropdown.setAdapter(adapter)
    }

    fun showDatePicker(view: View) {
        val dialogFragment = DatePickerFragment()
        dialogFragment.show(supportFragmentManager, "datePicker")
    }

    override fun onDialogDateSet(tag: String?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(year, month, dayOfMonth)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        binding.detailEdDueDate.text = dateFormat.format(calendar.time)

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