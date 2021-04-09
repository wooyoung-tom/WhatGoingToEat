package tom.dev.whatgoingtoeat.ui.payment.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import tom.dev.whatgoingtoeat.databinding.FragmentPaymentsBinding
import tom.dev.whatgoingtoeat.ui.MainViewModel
import tom.dev.whatgoingtoeat.utils.LoadingDialog

@AndroidEntryPoint
class PaymentHistoryFragment : Fragment() {

    private val viewModel: PaymentHistoryViewModel by viewModels()
    private val activityViewModel: MainViewModel by activityViewModels()

    private var _binding: FragmentPaymentsBinding? = null
    private val binding get() = _binding!!

    private lateinit var paymentHistoryListAdapter: PaymentHistoryListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPaymentsBinding.inflate(inflater, container, false)

        val userId = activityViewModel.userInstance?.id ?: 0
        viewModel.findPaymentHistory(userId)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (activityViewModel.userInstance != null) {
            binding.tvPaymentTitle.text = getUserNameText(activityViewModel.userInstance?.username)
        }

        initAdapter()

        observeHistory()
        observeLoading()
    }

    private fun getUserNameText(username: String?) = "${username}님의 결제 히스토리입니다."

    private fun initAdapter() {
        paymentHistoryListAdapter = PaymentHistoryListAdapter()
        binding.recyclerviewPayment.apply {
            adapter = paymentHistoryListAdapter
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

    private fun observeHistory() {
        viewModel.paymentHistoryLiveData.observe(viewLifecycleOwner) {
            paymentHistoryListAdapter.submitList(it.reversed())
        }
    }
}