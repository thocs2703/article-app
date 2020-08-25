package vinova.kane.article.ui.filter

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import vinova.kane.article.PreferencesProvider

class FilterViewModel: ViewModel() {
    private lateinit var preferencesProvider: PreferencesProvider

//    private var _beginDate = MutableLiveData<String>()
//    var beginDate: LiveData<String> = _beginDate
//    private var _sortOrder = MutableLiveData<String>()
//    var sortOrder: LiveData<String> = _sortOrder
//    private var _newsDesk = MutableLiveData<String>()
//    var newsDesk: LiveData<String> = _newsDesk

    var beginDate: String = ""
    var sortOrder: String = ""
    var newsDesk: String = ""

    fun putData(context: Context){
        preferencesProvider = PreferencesProvider(context)
        preferencesProvider.putString(BEGIN_DATE, beginDate)
        preferencesProvider.putString(SORT_ORDER, sortOrder)
        preferencesProvider.putString(NEWS_DESK, newsDesk)
    }

    fun getData() {
        beginDate = preferencesProvider.getString(BEGIN_DATE) ?: ""
        sortOrder = preferencesProvider.getString(SORT_ORDER) ?: ""
        newsDesk = preferencesProvider.getString(NEWS_DESK) ?: ""
    }

    override fun onCleared() {
        super.onCleared()
        Log.i("FilterViewModel", "FilterViewModel destroyed!")
    }

    companion object{
        const val BEGIN_DATE = "BEGIN_DATE"
        const val SORT_ORDER = "SORT_ORDER"
        const val NEWS_DESK = "NEWS_DESK"
    }
}