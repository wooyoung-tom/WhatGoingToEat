package tom.dev.whatgoingtoeat.ui.select_menu

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import tom.dev.whatgoingtoeat.R
import tom.dev.whatgoingtoeat.databinding.FragmentSelectMenuBinding

@AndroidEntryPoint
class SelectMenuFragment: Fragment() {

    private val viewModel: SelectMenuViewModel by viewModels()

    private var _binding: FragmentSelectMenuBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoryListAdapter: CategoryListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSelectMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCategoryListAdapter()
        setCategoryList()
    }

    private fun getCategoryList() = resources.getStringArray(R.array.category).toList()

    private fun setCategoryListAdapter() {
        categoryListAdapter = CategoryListAdapter(getCategoryList()) {
            Log.d("Item Clicked", it)
        }
    }

    private fun setCategoryList() {
        binding.recyclerviewSelectMenuCategory.apply {
            adapter = categoryListAdapter
            layoutManager = GridLayoutManager(requireContext(), 4)
        }
    }
}