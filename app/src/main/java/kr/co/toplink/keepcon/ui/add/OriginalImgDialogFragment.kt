package kr.co.toplink.keepcon.ui.add

import android.app.AlertDialog
import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import kr.co.toplink.keepcon.R
import kr.co.toplink.keepcon.databinding.DialogAddOriginalBinding
import kr.co.toplink.keepcon.dto.GifticonImg

class OriginalImgDialogFragment(_gifticonImg:GifticonImg): DialogFragment() {
    private lateinit var binding:DialogAddOriginalBinding

    private var gifticonImg:GifticonImg

    init {
        gifticonImg = _gifticonImg
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogAddOriginalBinding.inflate(layoutInflater)

        val builder = AlertDialog.Builder(context, R.style.WrapContentDialog)
        builder.setView(binding.root)

        binding.gifticonImg = gifticonImg
        binding.btnCancel.setOnClickListener {
            dismiss()
        }

        return builder.create()
    }
}