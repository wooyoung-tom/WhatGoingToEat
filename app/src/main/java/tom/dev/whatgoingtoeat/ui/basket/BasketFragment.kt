package tom.dev.whatgoingtoeat.ui.basket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import tom.dev.whatgoingtoeat.databinding.FragmentBasketBinding
import tom.dev.whatgoingtoeat.dto.order.OrderBasketItem
import tom.dev.whatgoingtoeat.ui.MainViewModel
import tom.dev.whatgoingtoeat.utils.LoadingDialog
import tom.dev.whatgoingtoeat.utils.showShortSnackBar

@AndroidEntryPoint
class BasketFragment : Fragment() {

    private val viewModel: BasketViewModel by viewModels()
    private val activityViewModel: MainViewModel by activityViewModels()

    private var _binding: FragmentBasketBinding? = null
    private val binding get() = _binding!!

    private lateinit var basketListAdapter: BasketListAdapter

    override fun onPause() {
        super.onPause()
        viewModel.selectedOrderList.clear()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBasketBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setBasketListAdapter()

        viewModel.findReadyStateOrders(activityViewModel.userInstance?.userId)

        binding.tvBasketTotalPrice.text = "0 원"

        observeReadyStatusOrders()
        observeLoading()
        observeDeleteComplete()
        observeSelectedOrderList()

        setBasketOrderButtonClickListener()
    }

    private fun setBasketListAdapter() {
        basketListAdapter = BasketListAdapter(viewModel, object : BasketListAdapter.ClickListeners {
            override fun onDeleteButtonClickListener(item: OrderBasketItem, position: Int) {
                AlertDialog.Builder(requireContext()).apply {
                    setTitle("주문 삭제")
                    setMessage("해당 주문을 정말 삭제하시겠습니까?\n식당이름: ${item.restaurant.name}")
                    setPositiveButton("삭제") { dialog, _ ->
                        viewModel.deleteOrder(item.orderId, position)
                        dialog.dismiss()
                    }
                    setNegativeButton("취소") { dialog, _ ->
                        dialog.dismiss()
                    }
                    setCancelable(false)
                }.show()
            }

            override fun onEditButtonClickListener(item: OrderBasketItem) {
                val action = BasketFragmentDirections.actionBasketFragmentToBasketEditFragment(item)
                findNavController().navigate(action)
            }
        })
        binding.recyclerviewBasket.apply {
            adapter = basketListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun observeReadyStatusOrders() {
        viewModel.orderListLiveData.observe(viewLifecycleOwner) {
            it?.let {
                basketListAdapter.submitList(it.orders.reversed())
            }
        }
    }

    private fun getTotalPriceStr(menuList: List<OrderBasketItem>): String {
        val price = menuList.sumOf { it.totalPrice }
        return "$price 원"
    }

    private fun getButtonText(menuList: List<OrderBasketItem>): String = "${menuList.size}개 주문하기"

    private fun observeLoading() {
        val loading = LoadingDialog(requireContext())
        viewModel.startLoadingDialogEvent.observe(viewLifecycleOwner) {
            loading.show()
        }
        viewModel.stopLoadingDialogEvent.observe(viewLifecycleOwner) {
            loading.dismiss()
        }
    }

    private fun observeDeleteComplete() {
        viewModel.deleteCompleteLiveData.observe(viewLifecycleOwner) {
            viewModel.findReadyStateOrders(activityViewModel.userInstance?.userId)
        }

        viewModel.deleteFailedLiveData.observe(viewLifecycleOwner) {
            requireView().showShortSnackBar(it)
        }
    }

    private fun observeSelectedOrderList() {
        viewModel.selectedOrderListChangedLiveData.observe(viewLifecycleOwner) {
            binding.tvBasketTotalPrice.text = getTotalPriceStr(viewModel.selectedOrderList)

            if (viewModel.selectedOrderList.isEmpty()) {
                binding.btnBasketOrder.text = "주문하기"
                binding.btnBasketOrder.isEnabled = false
            } else {
                binding.btnBasketOrder.text = getButtonText(viewModel.selectedOrderList)
                binding.btnBasketOrder.isEnabled = true
            }
        }
    }

    private fun setBasketOrderButtonClickListener() {
        binding.btnBasketOrder.setOnClickListener {
            if (viewModel.selectedOrderList.isEmpty()) {
                requireView().showShortSnackBar("주문할 오더를 선택해주세요.")
            } else {
                val action = BasketFragmentDirections
                    .actionBasketFragmentToInvoiceFragment(viewModel.selectedOrderList.toTypedArray())
                findNavController().navigate(action)
            }
        }
    }
}