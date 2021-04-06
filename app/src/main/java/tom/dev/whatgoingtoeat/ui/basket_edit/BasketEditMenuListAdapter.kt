package tom.dev.whatgoingtoeat.ui.basket_edit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import tom.dev.whatgoingtoeat.databinding.ItemMenuBinding
import tom.dev.whatgoingtoeat.dto.order.OrderDetailMenu
import tom.dev.whatgoingtoeat.dto.restaurant.RestaurantMenu

class BasketEditMenuListAdapter(
    private val viewModel: BasketEditViewModel,
    private val selectedItemControlListeners: SelectedItemControlListeners
) : ListAdapter<RestaurantMenu, BasketEditMenuListAdapter.BasketEditMenuViewHolder>(Companion) {

    interface SelectedItemControlListeners {
        fun onItemSelected(item: RestaurantMenu)
        fun onItemRemoved(item: RestaurantMenu)
    }

    companion object : DiffUtil.ItemCallback<RestaurantMenu>() {
        override fun areItemsTheSame(oldItem: RestaurantMenu, newItem: RestaurantMenu) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: RestaurantMenu, newItem: RestaurantMenu) = oldItem == newItem
    }

    inner class BasketEditMenuViewHolder(val binding: ItemMenuBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RestaurantMenu) {
            binding.tvItemMenuTitle.text = item.name
            binding.tvItemMenuPrice.text = item.price.toString()
            binding.tvItemMenuCounter.text = (viewModel.selectedMenuList.find {
                it.menu.id == item.id
            }?.count ?: 0).toString()

            binding.btnItemMenuMinus.setOnClickListener {
                val currentCounter = binding.tvItemMenuCounter.text.toString().toInt()
                if (currentCounter != 0) {
                    // 메뉴 뺐을 때
                    selectedItemControlListeners.onItemRemoved(item)
                    binding.tvItemMenuCounter.text = (currentCounter - 1).toString()
                }
            }

            binding.btnItemMenuPlus.setOnClickListener {
                val currentCounter = binding.tvItemMenuCounter.text.toString().toInt()
                if (currentCounter != 5) {
                    // 메뉴 더했을 때
                    selectedItemControlListeners.onItemSelected(item)
                    binding.tvItemMenuCounter.text = (currentCounter + 1).toString()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketEditMenuViewHolder {
        return BasketEditMenuViewHolder(
            ItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: BasketEditMenuViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}