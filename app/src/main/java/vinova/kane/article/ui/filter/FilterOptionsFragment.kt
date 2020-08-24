package vinova.kane.article.ui.filter

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import vinova.kane.article.R
import vinova.kane.article.databinding.FilterDialogBinding
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class FilterOptionsFragment : DialogFragment() {
    private lateinit var binding: FilterDialogBinding
    private var beginDate = ""
    private var sortOrder = ""
    private var newsDesk = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FilterDialogBinding.inflate(inflater)
        getBeginDate()
        getSortOrder()
        binding.saveButton.setOnClickListener {
            getNewsDesk()
            val bundle: Bundle = if (beginDate != ""){
                bundleOf("beginDate" to beginDate, "sortOrder" to sortOrder, "newsDesk" to newsDesk)
            } else{
                bundleOf("sortOrder" to sortOrder, "newsDesk" to newsDesk)
            }
            parentFragment?.findNavController()?.navigate(R.id.filter_nav_overview, bundle)
            Log.d("FilterOptionsFragment", "BEGIN DATE: $beginDate")
            Log.d("FilterOptionsFragment", "SORT ORDER: $sortOrder")
            Log.d("FilterOptionsFragment", "NEWS DESK VALUE: $newsDesk")
        }

        return binding.root
    }

    private fun getBeginDate() {
        val calendar = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val queryFormat = "yyyyMMdd"
            val textFormat = "dd/MM/yyyy"
            beginDate = SimpleDateFormat(queryFormat, Locale.US).format(calendar.time)
            binding.datePickerEdit.setText(SimpleDateFormat(textFormat, Locale.US).format(calendar.time))
        }

        binding.datePickerEdit.setOnClickListener {
            context?.let { it1 ->
                DatePickerDialog(
                    it1, dateSetListener,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)).show()
            }
        }
    }

    private fun getSortOrder(){
        val spinnerValue = resources.getStringArray(R.array.sort_order_values)
        val adapter = context?.let {
            ArrayAdapter(it, android.R.layout.simple_spinner_dropdown_item, spinnerValue)
        }
        binding.sortOrderSpinner.adapter = adapter
        binding.sortOrderSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                sortOrder = if (position == 0) "newest" else "oldest"
                Log.d("FilterOptionsFragment", "Sort Order: $sortOrder")
            }

        }
    }

    private fun getNewsDesk(){
        val filterNewsDesk = ArrayList<String>()
        var artsFilter = ""
        var fashionFilter = ""
        var sportsFilter = ""
        if (binding.newsDeskArts.isChecked){
            artsFilter = "\"${getString(R.string.arts)}\""
            filterNewsDesk.add(artsFilter)
        }
        if (binding.newsDeskFashion.isChecked){
            fashionFilter = "\"${getString(R.string.fashion_amp_style)}\""
            filterNewsDesk.add(fashionFilter)
        }
        if (binding.newsDeskSports.isChecked){
            sportsFilter = "\"${getString(R.string.sports)}\""
            filterNewsDesk.add(sportsFilter)
        }
        newsDesk = ":($filterNewsDesk)"
    }
}