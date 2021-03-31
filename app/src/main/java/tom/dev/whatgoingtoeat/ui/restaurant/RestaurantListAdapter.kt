package tom.dev.whatgoingtoeat.ui.restaurant

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import tom.dev.whatgoingtoeat.databinding.ItemRestaurantBinding
import tom.dev.whatgoingtoeat.dto.restaurant.RestaurantItem

class RestaurantListAdapter: ListAdapter<RestaurantItem, RestaurantListAdapter.RestaurantViewHolder>(Companion) {

    companion object: DiffUtil.ItemCallback<RestaurantItem>() {
        override fun areItemsTheSame(oldItem: RestaurantItem, newItem: RestaurantItem) = oldItem.restaurant.id == newItem.restaurant.id
        override fun areContentsTheSame(oldItem: RestaurantItem, newItem: RestaurantItem) = oldItem == newItem
    }

    inner class RestaurantViewHolder(val binding: ItemRestaurantBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RestaurantItem) {
            binding.tvItemSearchResultCategory.text = item.restaurant.category
            binding.tvItemSearchResultName.text = item.restaurant.restaurantName
            binding.tvItemSearchResultDoro.text = item.restaurant.roadAddress ?: ""
            binding.tvItemSearchResultJibun.text = item.restaurant.jibunAddress ?: ""
            binding.tvItemSearchResultDistance.text = getDistanceStr(item.distance)
            binding.tvItemSearchResultReviewCnt.text  = getReviewCountStr(item.review)
            binding.tvItemSearchResultFavoriteCnt.text = getFavoriteCountStr(item.favorite)
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
        holder.bind(getItem(position))
    }
}