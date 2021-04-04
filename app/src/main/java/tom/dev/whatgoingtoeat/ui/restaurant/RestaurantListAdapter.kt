package tom.dev.whatgoingtoeat.ui.restaurant

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import tom.dev.whatgoingtoeat.databinding.ItemRestaurantBinding
import tom.dev.whatgoingtoeat.dto.restaurant.Restaurant

class RestaurantListAdapter(
    private val restaurantClickListener: (Restaurant) -> Unit
) : PagingDataAdapter<Restaurant, RestaurantListAdapter.RestaurantViewHolder>(Companion) {

    companion object: DiffUtil.ItemCallback<Restaurant>() {
        override fun areItemsTheSame(oldItem: Restaurant, newItem: Restaurant) = oldItem.restaurantId == newItem.restaurantId
        override fun areContentsTheSame(oldItem: Restaurant, newItem: Restaurant) = oldItem == newItem
    }

    inner class RestaurantViewHolder(val binding: ItemRestaurantBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Restaurant) {
            binding.tvItemSearchResultCategory.text = item.category
            binding.tvItemSearchResultName.text = item.restaurantName
            binding.tvItemSearchResultAddr.text = item.roadAddress ?: item.jibunAddress
            binding.tvItemSearchResultDistance.text = getDistanceStr(item.distance)
            binding.tvItemSearchResultReviewCnt.text = getReviewCountStr(item.reviewCount)
            binding.tvItemSearchResultFavoriteCnt.text = getFavoriteCountStr(item.favoriteCount)
        }

        private fun getDistanceStr(distance: Int) = "${distance}m"
        private fun getReviewCountStr(review: Int) = "$review"
        private fun getFavoriteCountStr(favorite: Int) = "$favorite"
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantViewHolder {
        return RestaurantViewHolder(
            ItemRestaurantBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: RestaurantViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null) {
            holder.bind(item)
            holder.itemView.setOnClickListener {
                restaurantClickListener(item)
            }
        }
    }
}