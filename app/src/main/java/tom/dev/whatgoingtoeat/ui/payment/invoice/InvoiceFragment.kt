package tom.dev.whatgoingtoeat.ui.payment.invoice

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
import tom.dev.whatgoingtoeat.databinding.FragmentInvoiceBinding
import tom.dev.whatgoingtoeat.dto.order.OrderBasketItem
import tom.dev.whatgoingtoeat.utils.hide
import tom.dev.whatgoingtoeat.utils.show
import tom.dev.whatgoingtoeat.utils.showShortSnackBar

@AndroidEntryPoint
class InvoiceFragment : Fragment() {

    private val viewModel: InvoiceViewModel by viewModels()

    private var _binding: FragmentInvoiceBinding? = null
    private val binding get() = _binding!!

    private val basketOrderList by lazy { InvoiceFragmentArgs.fromBundle(requireArguments()).basketItemList.asList() }
    private val type by lazy { InvoiceFragmentArgs.fromBundle(requireArguments()).type }

    private lateinit var invoiceOrderListAdapter: InvoiceOrderListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentInvoiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        when (type) {
            "normal" -> {
                // 결제수단 선택되어있음
                binding.radiogroupPaymentMethod.check(R.id.radiobutton_payment_now)
                binding.radiogroupPaymentNowMethod.check(R.id.radiobutton_payment_now_card)
            }
            else -> {
                binding.radiobuttonPaymentLater.hide()

                binding.radiogroupPaymentMethod.check(R.id.radiobutton_payment_now)
                binding.radiogroupPaymentNowMethod.check(R.id.radiobutton_payment_now_card)

                // 장바구니 버튼 hide
                binding.btnInvoiceBasket.hide()
                // 결제취소버튼 show
                binding.btnInvoiceCancel.show()
            }
        }

        binding.tvInvoiceTotalPrice.text = getTotalPriceStr(basketOrderList)

        initAdapter()

        setRadioButtonClickListener()
        setBasketButtonClickListener()
        setPaymentButtonClickListener()

        observePaymentResult()
    }

    private fun getTotalPriceStr(list: List<OrderBasketItem>) = "${list.sumOf { it.totalPrice }} 원"

    private fun initAdapter() {
        invoiceOrderListAdapter = InvoiceOrderListAdapter(basketOrderList)
        binding.recyclerviewInvoice.apply {
            adapter = invoiceOrderListAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

    private fun setRadioButtonClickListener() {
        binding.radiogroupPaymentMethod.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radiobutton_payment_now -> binding.radiogroupPaymentNowMethod.show()
                else -> binding.radiogroupPaymentNowMethod.hide()
            }
        }
    }

    private fun setBasketButtonClickListener() {
        binding.btnInvoiceBasket.setOnClickListener {
            findNavController().navigate(R.id.action_invoiceFragment_pop)
        }
    }

    private fun setPaymentButtonClickListener() {
        binding.btnPaymentReady.setOnClickListener {
            val method: String = when (binding.radiogroupPaymentMethod.checkedRadioButtonId) {
                R.id.radiobutton_payment_now -> {
                    when (binding.radiogroupPaymentNowMethod.checkedRadioButtonId) {
                        R.id.radiobutton_payment_now_card -> "카드결제"
                        R.id.radiobutton_payment_now_realtime -> "실시간 계좌이체"
                        else -> "가상계좌"
                    }
                }
                else -> "나중에 결제하기"
            }

            viewModel.orderPayment(basketOrderList.map { it.orderId }, method)
        }
    }

    private fun observePaymentResult() {
        viewModel.orderPaidSuccessLiveData.observe(viewLifecycleOwner) {
            val action = InvoiceFragmentDirections
                .actionInvoiceFragmentToPaidFragment(it, basketOrderList.toTypedArray())
            findNavController().navigate(action)
        }

        viewModel.orderPaidFailedLiveData.observe(viewLifecycleOwner) {
            requireView().showShortSnackBar(it)
        }
    }
}