package kr.co.toplink.keepcon.mms

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.loader.content.CursorLoader
import com.google.gson.Gson
import com.google.gson.JsonParser
import kr.co.toplink.keepcon.R
import kr.co.toplink.keepcon.config.ApplicationClass
import kr.co.toplink.keepcon.databinding.DialogMmsBinding
import kr.co.toplink.keepcon.dto.*
import kr.co.toplink.keepcon.repository.add.AddRemoteDataSource
import kr.co.toplink.keepcon.repository.add.AddRepository
import kr.co.toplink.keepcon.ui.add.ProgressDialog
import kr.co.toplink.keepcon.ui.common.MainActivity
import kr.co.toplink.keepcon.ui.home.HomeFragment
import kr.co.toplink.keepcon.util.RetrofitUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink
import okio.source
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

private const val TAG = "MMSDialog"
class MMSDialog(
    private val mainActivity: MainActivity
): DialogFragment() {
    private lateinit var binding: DialogMmsBinding
    private lateinit var mContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogMmsBinding.inflate(layoutInflater)
        mContext = requireContext()

        val builder = AlertDialog.Builder(context, R.style.WrapContentDialog)
        builder.setView(binding.root)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        isCancelable = false

        binding.lMms.visibility = View.VISIBLE
        binding.btnCancel.setOnClickListener {
            MainActivity.fromMMSReceiver = null
            dismiss()
        }

        binding.btnGoToAdd.setOnClickListener {

        }

        return builder.create()
    }

    val user = ApplicationClass.sharedPreferencesUtil.getUser()
    var imgNum = 0
    val PRODUCT = "Product"
    val BARCODE = "Barcode"


}