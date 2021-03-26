package tom.dev.whatgoingtoeat.ui.select_menu

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StableIdKeyProvider
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import tom.dev.whatgoingtoeat.R
import tom.dev.whatgoingtoeat.databinding.FragmentSelectCategoryBinding
import tom.dev.whatgoingtoeat.utils.showShortSnackBar
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class SelectCategoryFragment : Fragment() {

    private val viewModel: SelectCategoryViewModel by viewModels()

    private var _binding: FragmentSelectCategoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var categoryListAdapter: CategoryListAdapter

    private val user by lazy { SelectCategoryFragmentArgs.fromBundle(requireArguments()).user }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSelectCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCategoryListAdapter()
        setCategoryList()
        setCategoryListSelectionTracker()

        setSelectCategoryButtonClickListener()

        observeSaveCategoryHistoryComplete()
    }

    private fun getCategoryList() = resources.getStringArray(R.array.category).toList()

    private fun setCategoryListAdapter() {
        categoryListAdapter = CategoryListAdapter(getCategoryList()) {
            viewModel.currentSelectedCategory = it
        }
    }

    private fun setCategoryList() {
        binding.recyclerviewSelectCategory.apply {
            adapter = categoryListAdapter
            layoutManager = GridLayoutManager(requireContext(), 4)
        }
    }

    private fun setCategoryListSelectionTracker() {
        categoryListAdapter.setSelectionTracker(getSelectionTrackerBuilder())
    }

    private fun getSelectionTrackerBuilder(): SelectionTracker<Long> {
        return SelectionTracker.Builder(
            "selection_id",
            binding.recyclerviewSelectCategory,
            StableIdKeyProvider(binding.recyclerviewSelectCategory),
            CategoryDetailsLookUp(binding.recyclerviewSelectCategory),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(SelectionPredicates.createSelectSingleAnything()).build()
    }

    private fun setSelectCategoryButtonClickListener() {
        binding.btnSelectCategory.setOnClickListener {
            if (viewModel.currentSelectedCategory == null) {
                requireView().showShortSnackBar("카테고리가 선택되지 않았습니다.")
            } else {
                val yesterdayStr = getYesterdayDateForString()

                viewModel.completeSelectCategory(user, yesterdayStr)
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getYesterdayDateForString(): String {
        return SimpleDateFormat("yyyy-MM-dd")
            .format(Calendar.getInstance().apply {
                time = Date()
                add(Calendar.DATE, -1)
            }.time).toString()
    }

    private fun observeSaveCategoryHistoryComplete() {
        viewModel.saveCategoryHistoryComplete.observe(viewLifecycleOwner) {
            val action = SelectCategoryFragmentDirections.actionSelectMenuFragmentToSelectResultFragment(user)
            findNavController().navigate(action)
        }
    }
}