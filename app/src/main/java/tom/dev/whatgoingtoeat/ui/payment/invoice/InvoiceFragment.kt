package tom.dev.whatgoingtoeat.ui.payment.invoice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import tom.dev.whatgoingtoeat.R
import tom.dev.whatgoingtoeat.databinding.FragmentInvoiceBinding
import tom.dev.whatgoingtoeat.dto.order.OrderBasketItem

@AndroidEntryPoint
class InvoiceFragment : Fragment() {

    private var _binding: FragmentInvoiceBinding? = null
    private val binding get() = _binding!!

    private val basketOrderList by lazy { InvoiceFragmentArgs.fromBundle(requireArguments()).basketItemList.asList() }

    private lateinit var invoiceOrderListAdapter: InvoiceOrderListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentInvoiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvInvoiceTotalPrice.text = getTotalPriceStr(basketOrderList)
        binding.radiogroupPaymentMethod.check(R.id.radiobutton_payment_now)

        initAdapter()

        setBasketButtonClickListener()
    }

    private fun getTotalPriceStr(list: List<OrderBasketItem>) = "${list.sumOf { it.totalPrice }} 원"

    private fun initAdapter() {
        invoiceOrderListAdapter = InvoiceOrderListAdapter(basketOrderList)
        binding.recyclerviewInvoice.apply {
            adapter = invoiceOrderListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setBasketButtonClickListener() {
        binding.btnInvoiceBasket.setOnClickListener {
            findNavController().navigate(R.id.action_invoiceFragment_pop)
        }
    }
}