package kr.co.toplink.keepcon.ui.add

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.TextUtils.split
import android.util.Log
import android.view.Window
import androidx.fragment.app.DialogFragment
import kr.co.toplink.keepcon.R
import kr.co.toplink.keepcon.databinding.DialogAddCropChkBinding
import kr.co.toplink.keepcon.dto.GifticonImg
import kr.co.toplink.keepcon.dto.GifticonItemList
import kr.co.toplink.keepcon.util.CustomImageView

class CropImgDialogFragment(gifticonItemList: GifticonItemList, private val clickFromCv:String): DialogFragment() {
    private lateinit var binding:DialogAddCropChkBinding

    private var _gifticonItemList : GifticonItemList

    init {
        _gifticonItemList = gifticonItemList
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogAddCropChkBinding.inflate(layoutInflater)

        val builder = AlertDialog.Builder(context, R.style.WrapContentDialog)
        builder.setView(binding.root)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        binding.ivCropImg.clipToOutline = true

        binding.gifticonItemList = _gifticonItemList


        //binding.ivProduceImg.setImageBitmap(_gifticonItemList.productName_bitmap)
        /*
        val productBitmap = _gifticonItemList.productName_bitmap!!
        val pos_crop = _gifticonItemList.productPos!!.split(",").toTypedArray()
        val left = pos_crop[0].toInt()
        val top =  pos_crop[1].toInt()
        val right =  pos_crop[2].toInt()
        val bottom =  pos_crop[3].toInt()
        binding.ivCropImg.setCustomBitmap(productBitmap, Rect(left,top,right,bottom))
         */

        binding.btnCancel.setOnClickListener{
            dismiss()
        }
        binding.btnRecrop.setOnClickListener {
            onClickListener.onClicked(clickFromCv)
            dismiss()
        }

        return builder.create()
    }

    interface BtnClickListener{
        fun onClicked(fromCv:String)
    }

    private lateinit var onClickListener: BtnClickListener

    fun setOnClickListener(listener: BtnClickListener){
        onClickListener = listener
    }
}