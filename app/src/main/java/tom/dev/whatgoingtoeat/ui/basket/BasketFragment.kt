package tom.dev.whatgoingtoeat.ui.basket

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dagger.hilt.android.AndroidEntryPoint
import tom.dev.whatgoingtoeat.databinding.FragmentBasketBinding

@AndroidEntryPoint
class BasketFragment : Fragment() {

    private var _binding: FragmentBasketBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBasketBinding.inflate(inflater, container, false)
        return binding.root
    }
}