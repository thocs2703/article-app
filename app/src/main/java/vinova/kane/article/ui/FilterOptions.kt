@file:Suppress("DEPRECATION")

package vinova.kane.article.ui

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import fr.tvbarthel.lib.blurdialogfragment.BlurDialogEngine
import kotlinx.android.synthetic.main.filter_dialog.*
import vinova.kane.article.R
import vinova.kane.article.util.OnSaveFilterListener
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("ValidFragment")
class FilterOptions(
    private val mContext: Context,
    private val mDate: String?,
    private val sort: String?,
    private val newDesk: String?
) : DialogFragment() {
    private var listener: OnSaveFilterListener? = null
    private var myCalendar = Calendar.getInstance()
    private var mBlurEngine: BlurDialogEngine? = null
    private var date: DatePickerDialog.OnDateSetListener =
        DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel()
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.filter_dialog, container)

    }

    fun setSaveListener(clickListener: OnSaveFilterListener) {
        listener = clickListener
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (mDate != null && mDate != "") {
            val year = mDate.substring(0, 4)
            val month = mDate.substring(4, 6)
            val day = mDate.substring(6, 8)
            myCalendar.set(year.toInt(), month.toInt(), day.toInt())
            updateLabel()
        }
        if (sort == "Oldest") {
            sort_order_spinner.setSelection(0)
        } else {
            sort_order_spinner.setSelection(1)
        }
        if (newDesk != null) {
            if (newDesk.contains("Arts")) {
                news_desk_arts.isChecked = true
            }
            if (newDesk.contains("Fashion")) {
                news_desk_fashion.isChecked = true
            }
            if (newDesk.contains("Sports")) {
                news_desk_sports.isChecked = true
            }
        }

        date_picker_edit.setOnClickListener {
            DatePickerDialog(
                mContext
                , AlertDialog.THEME_HOLO_LIGHT, date, myCalendar
                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        save_button.setOnClickListener {
            val date = date_picker_edit.text.toString().replace("/", "")
            val sort = sort_order_spinner.selectedItem.toString().toLowerCase(Locale.ROOT)
            val newDesk = when {
                news_desk_arts.isChecked -> getString(R.string.arts)
                news_desk_fashion.isChecked -> getString(R.string.fashion_amp_style)
                news_desk_sports.isChecked -> getString(R.string.sports)
                else -> ""
            }
            listener?.onItemClick(date, sort, newDesk)
        }
        mBlurEngine = BlurDialogEngine(activity)
        mBlurEngine!!.setBlurRadius(4)

    }

    override fun onResume() {
        super.onResume()
        mBlurEngine!!.onResume(retainInstance)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        mBlurEngine!!.onDismiss()
    }

    override fun onDestroy() {
        super.onDestroy()
        mBlurEngine!!.onDetach()
    }

    override fun onDestroyView() {
        dialog?.setDismissMessage(null)
        super.onDestroyView()
    }

    private fun updateLabel() {
        val myFormat = "yyyy/MM/dd"
        val sdf = SimpleDateFormat(myFormat, Locale.US)

        date_picker_edit.setText(sdf.format(myCalendar.time))
    }

}