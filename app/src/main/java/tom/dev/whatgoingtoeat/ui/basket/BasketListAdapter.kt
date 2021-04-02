package tom.dev.whatgoingtoeat.ui.basket

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import tom.dev.whatgoingtoeat.databinding.ItemBasketBinding
import tom.dev.whatgoingtoeat.dto.order.OrderBasketItem
import tom.dev.whatgoingtoeat.dto.order.OrderBasketResponse

class BasketListAdapter : ListAdapter<OrderBasketResponse, BasketListAdapter.BasketViewHolder>(Companion) {

    companion object : DiffUtil.ItemCallback<OrderBasketResponse>() {
        override fun areItemsTheSame(oldItem: OrderBasketResponse, newItem: OrderBasketResponse) = oldItem.restaurantName == newItem.restaurantName
        override fun areContentsTheSame(oldItem: OrderBasketResponse, newItem: OrderBasketResponse) = oldItem == newItem
    }

    inner class BasketViewHolder(val binding: ItemBasketBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OrderBasketResponse) {
            binding.apply {
                tvItemBasketRestaurantName.text = item.restaurantName
                tvItemBasketPrice.text = getTotalPriceStr(item.totalPrice)
                setMenuListTextView(item.menuList)
            }
        }

        private fun getTotalPriceStr(price: Int) = "${price}원"

        private fun setMenuListTextView(menuList: List<OrderBasketItem>) {
            menuList.forEach {
                val menuTextView = TextView(binding.root.context).apply {
                    text = getMenuDetailInfo(it)
                    setTextColor(Color.BLACK)
                }

                binding.linearlayoutItemBasketMenu.addView(menuTextView)
            }
        }

        private fun getMenuDetailInfo(menu: OrderBasketItem) = "${menu.orderDetailMarketMenu.name} (${menu.menuCount})"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketViewHolder {
        return BasketViewHolder(
            ItemBasketBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: BasketViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}