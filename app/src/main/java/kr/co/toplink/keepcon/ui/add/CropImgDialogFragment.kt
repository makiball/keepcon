package kr.co.toplink.keepcon.ui.add

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.fragment.app.DialogFragment
import kr.co.toplink.keepcon.R
import kr.co.toplink.keepcon.databinding.DialogAddCropChkBinding
import kr.co.toplink.keepcon.dto.GifticonItemList

class CropImgDialogFragment(gifticonItemList: GifticonItemList, private val clickFromCv:String): DialogFragment() {

    private val TAG = "bitmap.canvas!!!!"
    private lateinit var binding:DialogAddCropChkBinding
    private var _gifticonItemList : GifticonItemList

    val PRODUCT = "Product"
    val BARCODE = "Barcode"

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

        if(clickFromCv == PRODUCT) {
            _gifticonItemList.productName_bitmap?.let { binding.ivCropImg.setCustomBitmap(it) }
        }

        if(clickFromCv == BARCODE) {
            _gifticonItemList.barcode_bitmap?.let { binding.ivCropImg.setCustomBitmap(it) }
        }

        /*
        // Sizing the Dialog
        val displayRectangle = Rect()
        val xxxxx = displayRectangle.width() * 0.9f
        Log.d(TAG, "======>x $xxxxx")
         */

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

    override fun onStart() {
        super.onStart()

        // DialogFragment의 가로 화면 크기 구하기
        val dialog = dialog
        val window = dialog?.window
        val width = window?.decorView?.width


        Log.d(TAG, "X======> $width")

    }

    fun setOnClickListener(listener: BtnClickListener){
        onClickListener = listener
    }
}