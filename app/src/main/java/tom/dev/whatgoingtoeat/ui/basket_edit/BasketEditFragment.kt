package tom.dev.whatgoingtoeat.ui.basket_edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import tom.dev.whatgoingtoeat.R
import tom.dev.whatgoingtoeat.databinding.FragmentBasketEditBinding
import tom.dev.whatgoingtoeat.dto.order.OrderDetailMenu
import tom.dev.whatgoingtoeat.dto.restaurant.RestaurantMenu
import tom.dev.whatgoingtoeat.ui.restaurant_info.SelectedRestaurantMenu
import tom.dev.whatgoingtoeat.utils.showShortSnackBar

@AndroidEntryPoint
class BasketEditFragment : Fragment() {

    private val viewModel: BasketEditViewModel by viewModels()

    private var _binding: FragmentBasketEditBinding? = null
    private val binding get() = _binding!!

    private val order by lazy { BasketEditFragmentArgs.fromBundle(requireArguments()).order }

    private lateinit var basketEditMenuListAdapter: BasketEditMenuListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBasketEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onPause() {
        super.onPause()
        viewModel.selectedMenuList.clear()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRestaurantInfoView()
        initMenuAdapter()

        setEditCompleteButtonClickListener()

        observeEditResult()
    }

    private fun initRestaurantInfoView() {
        binding.tvBasketEditCategory.text = order.restaurant.category
        binding.tvBasketEditRestaurantName.text = order.restaurant.name
        binding.tvBasketEditAddr.text = order.restaurant.roadAddress ?: order.restaurant.jibunAddress

        // ViewModel list 초기화
        viewModel.selectedMenuList.addAll(order.orderDetailList.map {
            SelectedRestaurantMenu(it.menu, it.menuCount)
        })
    }

    private fun initMenuAdapter() {
        basketEditMenuListAdapter = BasketEditMenuListAdapter(viewModel, object : BasketEditMenuListAdapter.SelectedItemControlListeners {
            override fun onItemSelected(item: RestaurantMenu) {
                viewModel.selectMenu(item)
            }

            override fun onItemRemoved(item: RestaurantMenu) {
                viewModel.removeMenu(item)
            }
        })

        binding.recyclerviewBasketEditMenu.run {
            adapter = basketEditMenuListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        basketEditMenuListAdapter.submitList(order.restaurant.menuList)
    }

    private fun setEditCompleteButtonClickListener() {
        binding.btnBasketEditMenuSelect.setOnClickListener {
            if (viewModel.selectedMenuList.isEmpty()) {
                requireView().showShortSnackBar("메뉴를 선택해주세요.")
            } else {
                viewModel.editOrderDetail(order.orderId)
            }
        }
    }

    private fun observeEditResult() {
        viewModel.editOrderCompleteLiveData.observe(viewLifecycleOwner) {
            requireView().showShortSnackBar("수정에 성공하였습니다.")
            findNavController().navigate(R.id.action_basketEditFragment_pop)
        }

        viewModel.editOrderFailedLiveData.observe(viewLifecycleOwner) {
            requireView().showShortSnackBar(it)
        }
    }
}