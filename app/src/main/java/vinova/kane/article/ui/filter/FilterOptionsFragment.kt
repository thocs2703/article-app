package vinova.kane.article.ui.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import vinova.kane.article.R
import vinova.kane.article.databinding.FilterDialogBinding

class FilterOptionsFragment : DialogFragment() {
    private lateinit var binding: FilterDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FilterDialogBinding.inflate(inflater)

        binding.saveButton.setOnClickListener {
            parentFragment?.findNavController()?.navigate(R.id.filter_nav_overview)
        }

        return binding.root
    }
}