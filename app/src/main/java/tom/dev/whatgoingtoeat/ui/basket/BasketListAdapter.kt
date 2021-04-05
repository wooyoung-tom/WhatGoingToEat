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
import tom.dev.whatgoingtoeat.dto.order.OrderDetailMenu

class BasketListAdapter : ListAdapter<OrderBasketItem, BasketListAdapter.BasketViewHolder>(Companion) {

    companion object : DiffUtil.ItemCallback<OrderBasketItem>() {
        override fun areItemsTheSame(oldItem: OrderBasketItem, newItem: OrderBasketItem) = oldItem.orderId == newItem.orderId
        override fun areContentsTheSame(oldItem: OrderBasketItem, newItem: OrderBasketItem) = oldItem == newItem
    }

    inner class BasketViewHolder(val binding: ItemBasketBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OrderBasketItem) {
            binding.apply {
                tvItemBasketRestaurantName.text = item.restaurantName
                tvItemBasketPrice.text = getTotalPriceStr(item.totalPrice)
                setMenuListTextView(item.orderDetailList)
            }
        }

        private fun getTotalPriceStr(price: Int) = "${price}원"

        private fun setMenuListTextView(menuList: List<OrderDetailMenu>) {
            var menuText = ""
            menuList.forEachIndexed { index, menu ->
                menuText += if (index != menuList.size - 1) {
                    "${getMenuDetailInfo(menu)}\n"
                } else getMenuDetailInfo(menu)
            }

            binding.tvItemBasketMenu.text = menuText
        }

        private fun getMenuDetailInfo(menu: OrderDetailMenu) = "${menu.menu.name} (${menu.menuCount})"
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