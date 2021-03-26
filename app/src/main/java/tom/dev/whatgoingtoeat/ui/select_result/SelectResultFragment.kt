package tom.dev.whatgoingtoeat.ui.select_result

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import tom.dev.whatgoingtoeat.R
import tom.dev.whatgoingtoeat.databinding.FragmentSelectResultBinding
import tom.dev.whatgoingtoeat.dto.history.History
import tom.dev.whatgoingtoeat.dto.history.HistoryCounter
import tom.dev.whatgoingtoeat.utils.LoadingDialog

@AndroidEntryPoint
class SelectResultFragment : Fragment() {

    private val viewModel: SelectResultViewModel by viewModels()

    private val user by lazy { SelectResultFragmentArgs.fromBundle(requireArguments()).user }

    private var _binding: FragmentSelectResultBinding? = null
    private val binding get() = _binding!!

    private lateinit var selectResultListAdapter: SelectResultListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSelectResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setProgressInit()

        viewModel.findHistoryResult(user.teamName)

        setTeamNameTextView()
        setSelectResultAdapter()

        observeSelectResult()
        observeLoading()
    }

    private fun setProgressInit() {
        binding.tvSelectResultProgressPercentage.text = "0%"
        binding.progressIndicatorSelectResult.progress = 0
    }

    private fun setTeamNameTextView() {
        binding.tvSelectResultTitle.text = user.teamName
    }

    private fun observeSelectResult() {
        viewModel.selectResultLiveData.observe(viewLifecycleOwner) {
            // 프로그레스 설정
            setProgressIndicatorView(it.meta.selectedMemberCount, it.meta.teamMemberCount)

            val categoryList = resources.getStringArray(R.array.category)
            val historyCounterList = categoryList.map { localCategory ->
                it.body?.find { historyResult ->
                    historyResult.category == localCategory
                }?.let { result ->
                    HistoryCounter(it.meta.selectedMemberCount, result.category, result.count)
                } ?: HistoryCounter(0, localCategory, 0)
            }

            selectResultListAdapter.submitList(historyCounterList.sortedBy { result -> result.count })
        }
    }

    private fun setProgressIndicatorView(selectedMemberCount: Long, teamMemberCount: Long) {
        val percentage = ((selectedMemberCount.toDouble() / teamMemberCount.toDouble())) * 100

        binding.tvSelectResultProgressPercentage.text = getProgressPercentageString(percentage.toInt())
        binding.progressIndicatorSelectResult.progress = percentage.toInt()

        binding.tvSelectResultTeamMemberCount.text = getMemberCountString(teamMemberCount)
        binding.tvSelectResultSelectedMemberCount.text = getMemberCountString(selectedMemberCount)
    }

    private fun getProgressPercentageString(percentage: Int): String = "${percentage}%"

    private fun getMemberCountString(count: Long) = "${count}명"

    private fun setSelectResultAdapter() {
        selectResultListAdapter = SelectResultListAdapter {
            val action = SelectResultFragmentDirections.actionSelectResultFragmentToSearchResultFragment(it.category)
            findNavController().navigate(action)
        }

        binding.recyclerviewSelectResult.apply {
            adapter = selectResultListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeLoading() {
        val loading = LoadingDialog(requireContext())
        viewModel.startLoadingDialogEvent.observe(viewLifecycleOwner) {
            loading.show()
        }
        viewModel.stopLoadingDialogEvent.observe(viewLifecycleOwner) {
            loading.dismiss()
        }
    }
}