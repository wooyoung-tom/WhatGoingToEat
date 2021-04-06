package tom.dev.whatgoingtoeat.ui.basket_edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import tom.dev.whatgoingtoeat.databinding.FragmentBasketEditBinding
import tom.dev.whatgoingtoeat.dto.order.OrderDetailMenu

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRestaurantInfoView()
        initMenuAdapter()
    }

    private fun initRestaurantInfoView() {
        binding.tvBasketEditCategory.text = order.restaurant.category
        binding.tvBasketEditRestaurantName.text = order.restaurant.name
        binding.tvBasketEditAddr.text = order.restaurant.roadAddress ?: order.restaurant.jibunAddress
    }

    private fun initMenuAdapter() {
        basketEditMenuListAdapter = BasketEditMenuListAdapter(object : BasketEditMenuListAdapter.SelectedItemControlListeners {
            override fun onItemSelected(item: OrderDetailMenu) {
                viewModel.selectMenu(item.menu)
            }

            override fun onItemRemoved(item: OrderDetailMenu) {
                viewModel.removeMenu(item.menu)
            }
        })

        binding.recyclerviewBasketEditMenu.run {
            adapter = basketEditMenuListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        basketEditMenuListAdapter.submitList(order.orderDetailList)
    }
}