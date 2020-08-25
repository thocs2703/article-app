package vinova.kane.article.ui.filter

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.core.view.get
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import vinova.kane.article.R
import vinova.kane.article.databinding.FilterDialogBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

class FilterOptionsFragment : DialogFragment() {
    private lateinit var binding: FilterDialogBinding
    private var beginDate = ""
    private var sortOrder = ""
    private var newsDesk = ""
    private var newsDeskSet = HashSet<String>()
    private lateinit var sortOrderView: String
//    private lateinit var viewModel: FilterViewModel

    private lateinit var preferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        viewModel = ViewModelProviders.of(this).get(FilterViewModel::class.java)

        binding = FilterDialogBinding.inflate(inflater)

        initPrefs()
        showFilter()

        getBeginDate()
        getSortOrder()
        binding.saveButton.setOnClickListener {
            getNewsDesk()
            val bundle: Bundle = if (beginDate != "") {
                bundleOf("beginDate" to beginDate, "sortOrder" to sortOrder, "newsDesk" to newsDesk)
            } else {
                bundleOf("sortOrder" to sortOrder, "newsDesk" to newsDesk)
            }
            parentFragment?.findNavController()?.navigate(R.id.filter_nav_overview, bundle)
            Log.d("FilterOptionsFragment", "BEGIN DATE: $beginDate")
            Log.d("FilterOptionsFragment", "SORT ORDER: $sortOrder")
            Log.d("FilterOptionsFragment", "NEWS DESK VALUE: $newsDesk")
            dialog?.dismiss()
        }

        return binding.root
    }

    @SuppressLint("CommitPrefEdits")
    private fun initPrefs() {
        preferences = activity?.getSharedPreferences(PREF, Context.MODE_PRIVATE)!!
        editor = preferences.edit()
    }

    private fun showFilter() {
        binding.datePickerEdit.setText(preferences.getString(BEGIN_DATE, ""))

        sortOrderView = preferences.getString(SORT_ORDER, "").toString()

        val filterNewsDesk = preferences.getStringSet(NEWS_DESK, null)
        if (filterNewsDesk != null) {
            if (filterNewsDesk.contains(getString(R.string.arts_query))) {
                binding.newsDeskArts.isChecked = true
            }
            if (filterNewsDesk.contains(getString(R.string.fashion_amp_style_query))) {
                binding.newsDeskFashion.isChecked = true
            }
            if (filterNewsDesk.contains(getString(R.string.sports_query))) {
                binding.newsDeskSports.isChecked = true
            }
        }
    }

    private fun getBeginDate() {
        val calendar = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val queryFormat = "yyyyMMdd"
            val textFormat = "dd/MM/yyyy"
            beginDate = SimpleDateFormat(queryFormat, Locale.US).format(calendar.time)
            binding.datePickerEdit.setText(
                SimpleDateFormat(
                    textFormat,
                    Locale.US
                ).format(calendar.time)
            )

            editor.putString(BEGIN_DATE, binding.datePickerEdit.text.toString())
            editor.apply()

            Log.d("FilterOptionsFragment", "EDITOR: $editor")
        }

        binding.datePickerEdit.setOnClickListener {
            context?.let { it1 ->
                DatePickerDialog(
                    it1, dateSetListener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        }
    }

    private fun getSortOrder() {
        val spinnerValue = resources.getStringArray(R.array.sort_order_values)
        val adapter = context?.let {
            ArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, spinnerValue)
        }
        binding.sortOrderSpinner.adapter = adapter
        binding.sortOrderSpinner.setSelection(
            when (sortOrderView) {
                "newest" -> 0
                "oldest" -> 1
                else -> 0
            }
        )
        binding.sortOrderSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    sortOrder = if (position == 0) "newest" else "oldest"
                    editor.putString(SORT_ORDER, sortOrder)
                    editor.apply()
                }

            }
    }

    private fun getNewsDesk() {
        val filterNewsDesk = ArrayList<String>()
        val artsFilter: String
        val fashionFilter: String
        val sportsFilter: String
        if (binding.newsDeskArts.isChecked) {
            artsFilter = getString(R.string.arts_query)
            filterNewsDesk.add(artsFilter)
        }
        if (binding.newsDeskFashion.isChecked) {
            fashionFilter = getString(R.string.fashion_amp_style_query)
            filterNewsDesk.add(fashionFilter)
        }
        if (binding.newsDeskSports.isChecked) {
            sportsFilter = getString(R.string.sports_query)
            filterNewsDesk.add(sportsFilter)
        }
        newsDesk = ":($filterNewsDesk)"

        newsDeskSet.addAll(filterNewsDesk)
        editor.putStringSet(NEWS_DESK, newsDeskSet)
        editor.apply()
    }

    companion object {
        private const val PREF = "ARTICLE_SEARCH"
        private const val BEGIN_DATE = "BEGIN_DATE"
        private const val SORT_ORDER = "SORT_ORDER"
        private const val NEWS_DESK = "NEWS_DESK"
    }
}