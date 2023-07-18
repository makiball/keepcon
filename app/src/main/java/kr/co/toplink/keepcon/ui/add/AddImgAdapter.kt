package kr.co.toplink.keepcon.ui.add

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kr.co.toplink.keepcon.R
import kr.co.toplink.keepcon.databinding.ItemAddImgBinding
import kr.co.toplink.keepcon.dto.*
import kr.co.toplink.keepcon.ui.common.MainActivity
import kr.co.toplink.keepcon.ui.home.HomeFragment

class AddImgAdapter(
    var gifticonItemList: ArrayList<GifticonItemList>
    , _onItemClick: onItemClick
):
    RecyclerView.Adapter<AddImgAdapter.AddImgViewHolder>() {
    private lateinit var binding: ItemAddImgBinding
    private lateinit var mainActivity: MainActivity
    private val onItemClick = _onItemClick
    private var nowClick = 0
    private val positionSet = mutableSetOf<Int>()

    inner class AddImgViewHolder(private val binding: ItemAddImgBinding):
        RecyclerView.ViewHolder(binding.root){
        fun bind(gifticonImg: GifticonItemList){
            binding.ivChkClick.bringToFront()


            if (bindingAdapterPosition == 0){
                binding.ivChkClick.visibility = View.GONE
                positionSet.add(0)
            }

            /*
            if (bindingAdapterPosition == nowClick){
                binding.ivChkClick.visibility = View.GONE
                binding.cvCouponImg.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, R.color.keepcon_orange))
            } else{
                binding.cvCouponImg.backgroundTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(binding.root.context, R.color.transparent))
            }

            binding.cvCouponImg.setOnClickListener {
                onItemClick.onClick(bindingAdapterPosition)
                binding.ivChkClick.visibility = View.GONE

                notifyItemChanged(nowClick)
                notifyItemChanged(bindingAdapterPosition)
                nowClick = bindingAdapterPosition

                positionSet.add(bindingAdapterPosition)
                AddFragment.chkCnt = positionSet.size
            }

            binding.gifticonImg = gifticonImg
            binding.btnRemove.setOnClickListener {
                gifticonItemList.removeAt(bindingAdapterPosition)
                notifyItemRemoved(bindingAdapterPosition)

                if (bindingAdapterPosition == nowClick){
                    nowClick = bindingAdapterPosition+1
                }

                if (gifticonItemList.size == 0){
                    mainActivity.changeFragment(HomeFragment())
                } else{
                    onItemClick.onClick(nowClick)
                }
            }
             */
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddImgViewHolder {
        binding = ItemAddImgBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        mainActivity = parent.context as MainActivity

        return AddImgViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddImgViewHolder, position: Int) {
        holder.apply {
            bind(gifticonItemList[position])
        }
    }

    override fun getItemCount(): Int {
        return gifticonItemList.size
    }
}