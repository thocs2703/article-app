package vinova.kane.article.ui.filter

import android.app.DatePickerDialog
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.*

class FilterViewModel: ViewModel() {
    private val _beginDate = MutableLiveData<String>()
    val beginDate: LiveData<String>
    get() = _beginDate

    private val _sortOrder = MutableLiveData<String>()
    var sortOrder: LiveData<String> = _sortOrder

    private val _newsDesk = MutableLiveData<String>()
    var newsDesk: LiveData<String> = _newsDesk

    lateinit var calendar: Calendar
    lateinit var dateSetListener: DatePickerDialog.OnDateSetListener

    fun getBeginDate(formatDate: String) {
        calendar = Calendar.getInstance()
        dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            _beginDate.value = SimpleDateFormat(formatDate, Locale.US).format(calendar.time)
            Log.d("FilterViewModel", "Begin Date: ${beginDate.value}")
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("FilterViewModel", "FilterViewModel destroyed!")
    }

}