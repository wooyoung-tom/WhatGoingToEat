package tom.dev.whatgoingtoeat.ui.basket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import tom.dev.whatgoingtoeat.databinding.FragmentBasketBinding
import tom.dev.whatgoingtoeat.dto.order.OrderBasketResponse
import tom.dev.whatgoingtoeat.ui.MainViewModel
import tom.dev.whatgoingtoeat.utils.LoadingDialog

@AndroidEntryPoint
class BasketFragment : Fragment() {

    private val viewModel: BasketViewModel by viewModels()
    private val activityViewModel: MainViewModel by activityViewModels()

    private var _binding: FragmentBasketBinding? = null
    private val binding get() = _binding!!

    private lateinit var basketListAdapter: BasketListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBasketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBasketListAdapter()

        viewModel.findReadyStateOrders(activityViewModel.userInstance?.id)

        observeReadyStatusOrders()
        observeLoading()
    }

    private fun setBasketListAdapter() {
        basketListAdapter = BasketListAdapter()
        binding.recyclerviewBasket.apply {
            adapter = basketListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeReadyStatusOrders() {
        viewModel.orderListLiveData.observe(viewLifecycleOwner) {
            it?.let {
                basketListAdapter.submitList(it.reversed())
            }
            binding.tvBasketTotalPrice.text = getTotalPriceStr(it)
        }
    }

    private fun getTotalPriceStr(menuList: List<OrderBasketResponse>): String {
        val price = menuList.sumOf { it.totalPrice }
        return "${price}원"
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