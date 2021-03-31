package tom.dev.whatgoingtoeat.ui.restaurant_info

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import tom.dev.whatgoingtoeat.databinding.ItemMenuBinding
import tom.dev.whatgoingtoeat.dto.restaurant.RestaurantMenu
import tom.dev.whatgoingtoeat.utils.hide
import tom.dev.whatgoingtoeat.utils.show

class RestaurantMenuListAdapter(
    private val selectedItemControlListener: SelectedItemControlListener
) : ListAdapter<RestaurantMenu, RestaurantMenuListAdapter.RestaurantMenuViewHolder>(Companion) {

    interface SelectedItemControlListener {
        fun onItemRemoved(item: RestaurantMenu)
        fun onItemSelected(item: RestaurantMenu)
    }

    companion object : DiffUtil.ItemCallback<RestaurantMenu>() {
        override fun areItemsTheSame(oldItem: RestaurantMenu, newItem: RestaurantMenu) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: RestaurantMenu, newItem: RestaurantMenu) = oldItem == newItem
    }

    inner class RestaurantMenuViewHolder(val binding: ItemMenuBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RestaurantMenu) {
            binding.tvItemMenuTitle.text = item.name
            binding.tvItemMenuPrice.text = getPriceStr(item.price)
            binding.tvItemMenuCounter.text = "0"

            itemView.setOnClickListener {
                when (binding.linearlayoutItemMenuCounter.visibility) {
                    View.GONE, View.INVISIBLE -> binding.linearlayoutItemMenuCounter.show()
                    View.VISIBLE -> binding.linearlayoutItemMenuCounter.hide()
                }
            }

            binding.btnItemMenuMinus.setOnClickListener {
                val currentCounter = binding.tvItemMenuCounter.text.toString().toInt()
                if (currentCounter != 0) {
                    // 메뉴 뺐을 때
                    selectedItemControlListener.onItemRemoved(item)
                    binding.tvItemMenuCounter.text = (currentCounter - 1).toString()
                }
            }

            binding.btnItemMenuPlus.setOnClickListener {
                val currentCounter = binding.tvItemMenuCounter.text.toString().toInt()
                if (currentCounter != 5) {
                    // 메뉴 더했을 때
                    selectedItemControlListener.onItemSelected(item)
                    binding.tvItemMenuCounter.text = (currentCounter + 1).toString()
                }
            }
        }

        private fun getPriceStr(price: Int) = "${price}원"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantMenuViewHolder {
        return RestaurantMenuViewHolder(
            ItemMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RestaurantMenuViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}