package kr.co.toplink.keepcon.ui.add

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.fragment.app.DialogFragment
import kr.co.toplink.keepcon.R
import kr.co.toplink.keepcon.databinding.DialogAddCropChkBinding
import kr.co.toplink.keepcon.dto.GifticonImg
import kr.co.toplink.keepcon.dto.GifticonItemList

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

        val colorDrawable = ColorDrawable(Color.TRANSPARENT)
        binding.ivCropImg.background  = colorDrawable
        binding.gifticonItemList = _gifticonItemList


        binding.ivCropImg.setImageBitmap(_gifticonItemList.productName_bitmap)


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