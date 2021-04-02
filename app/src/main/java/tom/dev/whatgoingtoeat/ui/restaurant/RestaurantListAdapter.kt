package tom.dev.whatgoingtoeat.ui.restaurant

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import tom.dev.whatgoingtoeat.databinding.ItemRestaurantBinding
import tom.dev.whatgoingtoeat.dto.restaurant.RestaurantItem

class RestaurantListAdapter(
    private val restaurantClickListener: (RestaurantItem) -> Unit
) : RecyclerView.Adapter<RestaurantListAdapter.RestaurantViewHolder>() {

    private val restaurantList: ArrayList<RestaurantItem> = ArrayList()

    fun update(list: List<RestaurantItem>) {
        restaurantList.clear()
        restaurantList.addAll(list)

        notifyDataSetChanged()
    }

    inner class RestaurantViewHolder(val binding: ItemRestaurantBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RestaurantItem) {
            binding.tvItemSearchResultCategory.text = item.restaurant.category
            binding.tvItemSearchResultName.text = item.restaurant.restaurantName
            binding.tvItemSearchResultAddr.text = item.restaurant.roadAddress ?: item.restaurant.jibunAddress
            binding.tvItemSearchResultDistance.text = getDistanceStr(item.distance)
            binding.tvItemSearchResultReviewCnt.text = getReviewCountStr(item.review)
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
        holder.bind(restaurantList[position])
        holder.itemView.setOnClickListener {
            restaurantClickListener(restaurantList[position])
        }
    }

    override fun getItemCount(): Int = restaurantList.size
}