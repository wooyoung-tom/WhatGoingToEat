package tom.dev.whatgoingtoeat.ui.payment.paid

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import tom.dev.whatgoingtoeat.R
import tom.dev.whatgoingtoeat.databinding.FragmentPaidBinding
import tom.dev.whatgoingtoeat.ui.MainViewModel
import tom.dev.whatgoingtoeat.utils.show
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Period
import java.time.Period.between
import java.time.temporal.ChronoUnit

@AndroidEntryPoint
class PaidFragment : Fragment() {

    private val activityViewModel: MainViewModel by activityViewModels()

    private var _binding: FragmentPaidBinding? = null
    private val binding get() = _binding!!

    private val paidResult by lazy { PaidFragmentArgs.fromBundle(requireArguments()).paidResult }
    private val paidOrders by lazy { PaidFragmentArgs.fromBundle(requireArguments()).paidOrderList }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentPaidBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("Payment Status", "${paidResult.status == "Later"}")

        val user = activityViewModel.userInstance
        if (user != null) binding.tvPaidName.text = getNameStr(user.username, user.userId)

        when (paidResult.status) {
            "Later" -> setPaymentLaterView()
            else -> setPaidSuccessView()
        }

        setButtonClickListeners()
    }

    private fun setPaidSuccessView() {
        binding.tvPaidTitle.text = "런치마켓 결제완료"
        binding.tvPaidMenu.text = getMenuStr()
        binding.tvPaidMethod.text = paidResult.method
        binding.tvPaidPrice.text = getPriceStr()
        binding.tvPaidDate.text = getDateStr()
    }

    private fun setPaymentLaterView() {
        binding.tvPaidTitle.text = "런치마켓 나중에 결제하기"
        binding.tvPaidMenu.text = getMenuStr()
        binding.tvPaidMethod.text = "나중에 결제하기"
        binding.tvPaidPrice.text = getPriceStr()
        binding.tvPaidDate.text = "1주 이내에 결제를 완료해주세요."
        binding.tvPaidDateExpire.show()
        binding.tvPaidDateExpire.text = getDateExpired()
    }

    private fun getNameStr(username: String, userId: String) = "${username}(${userId})님의 결제내역입니다."
    private fun getMenuStr() = "${paidOrders[0].restaurant.name} ${if (paidOrders.size == 1) "" else "외 ${paidOrders.size - 1}개"}"
    private fun getPriceStr() = "${paidOrders.sumOf { it.totalPrice }} 원"
    private fun getDateStr(): String {
        val date = paidResult.datetime.split("T")[0]
        val year = date.split("-")[0]
        val month = date.split("-")[1]
        val day = date.split("-")[2]
        return "${year}년 ${month}월 ${day}일"
    }

    private fun getDateExpired(): String {
        val nowDateTime = LocalDate.now()
        val dateTimeStr = paidResult.datetime.split("T")[0]
        val expireDateTime = LocalDate.parse(dateTimeStr)

        val betweenDateTime = between(nowDateTime, expireDateTime)
        return "결제 기한까지 ${betweenDateTime.days}일 남았습니다."
    }

    private fun setButtonClickListeners() {
        binding.btnPaidGoMain.setOnClickListener {
            findNavController().navigate(R.id.action_paidFragment_pop_including_basketFragment)
        }
        binding.btnPaidBasket.setOnClickListener {
            findNavController().navigate(R.id.action_paidFragment_pop)
        }
    }
}