package kr.co.toplink.keepcon.ui.map

import android.location.LocationManager
import android.os.Build
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.co.toplink.keepcon.databinding.ItemMapGiftconBinding
import kr.co.toplink.keepcon.dto.DonateRequest
import kr.co.toplink.keepcon.dto.Gifticon
import kr.co.toplink.keepcon.dto.User
import kr.co.toplink.keepcon.ui.common.DragListener
import kr.co.toplink.keepcon.ui.common.DragShadowBuilder
import kr.co.toplink.keepcon.util.Utils
import kr.co.toplink.keepcon.viewmodel.MapViewModel

private const val TAG = "GifticonMap_싸피"

class MapGifticonAdpater(
    val target: View,
    val viewModel: MapViewModel,
    val user: User,
    val lm: LocationManager
) :
    ListAdapter<Gifticon, MapGifticonAdpater.GifticonMapViewHolder>(BannerDiffCallback()) {
    private lateinit var binding: ItemMapGiftconBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GifticonMapViewHolder {
        binding = ItemMapGiftconBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GifticonMapViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: GifticonMapViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.itemView.setOnDragListener(
            DragListener(
                target,
                getItem(position).barcodeNum,
                viewModel,
                user,
                lm
            )
        )
        holder.itemView.setOnLongClickListener { v ->
            longClickListener.onLongClick(v, getItem(position))
            true
        }
    }

    inner class GifticonMapViewHolder(private val binding: ItemMapGiftconBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(gifticon: Gifticon) {
            binding.gifticon = gifticon
            binding.badge = Utils.calDday(gifticon)
            binding.ivProductPreviewMap.clipToOutline = true
            binding.executePendingBindings()
        }
    }

    private lateinit var longClickListener: OnLongClickListener

    interface OnLongClickListener {
        fun onLongClick(v: View, gifticon: Gifticon)
    }

    fun setOnLongClickListener(onLongClickListener: OnLongClickListener) {
        this.longClickListener = onLongClickListener
    }

    private lateinit var dragListener: DragListener

    fun setOnDragListener(onDragListener: DragListener) {
        this.dragListener = onDragListener
    }
}

class BannerDiffCallback : DiffUtil.ItemCallback<Gifticon>() {
    override fun areItemsTheSame(oldItem: Gifticon, newItem: Gifticon): Boolean {
        return oldItem.barcodeNum == newItem.barcodeNum
    }

    override fun areContentsTheSame(oldItem: Gifticon, newItem: Gifticon): Boolean {
        return oldItem == newItem
    }
}
