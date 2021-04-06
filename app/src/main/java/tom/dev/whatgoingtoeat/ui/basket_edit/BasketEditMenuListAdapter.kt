package tom.dev.whatgoingtoeat.ui.basket_edit

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import tom.dev.whatgoingtoeat.databinding.ItemMenuBinding
import tom.dev.whatgoingtoeat.dto.order.OrderDetailMenu

class BasketEditMenuListAdapter(
    private val selectedItemControlListeners: SelectedItemControlListeners
) : ListAdapter<OrderDetailMenu, BasketEditMenuListAdapter.BasketEditMenuViewHolder>(Companion) {

    interface SelectedItemControlListeners {
        fun onItemSelected(item: OrderDetailMenu)
        fun onItemRemoved(item: OrderDetailMenu)
    }

    companion object : DiffUtil.ItemCallback<OrderDetailMenu>() {
        override fun areItemsTheSame(oldItem: OrderDetailMenu, newItem: OrderDetailMenu) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: OrderDetailMenu, newItem: OrderDetailMenu) = oldItem == newItem
    }

    inner class BasketEditMenuViewHolder(val binding: ItemMenuBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: OrderDetailMenu) {
            binding.tvItemMenuTitle.text = item.menu.name
            binding.tvItemMenuPrice.text = item.menu.price.toString()
            binding.tvItemMenuCounter.text = item.menuCount.toString()

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