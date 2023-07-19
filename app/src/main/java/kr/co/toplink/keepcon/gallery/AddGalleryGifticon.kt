package kr.co.toplink.keepcon.gallery

import android.annotation.SuppressLint
import android.content.*
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.loader.content.CursorLoader
import com.google.gson.Gson
import com.google.gson.JsonParser
import kr.co.toplink.keepcon.config.ApplicationClass
import kr.co.toplink.keepcon.databinding.DialogProgressBinding
import kr.co.toplink.keepcon.dto.*
import kr.co.toplink.keepcon.repository.add.AddRemoteDataSource
import kr.co.toplink.keepcon.repository.add.AddRepository
import kr.co.toplink.keepcon.ui.add.ProgressDialog
import kr.co.toplink.keepcon.ui.common.MainActivity
import kr.co.toplink.keepcon.util.RetrofitUtil
import kr.co.toplink.keepcon.util.SharedPreferencesUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink
import okio.source
import java.io.File
import java.io.FileOutputStream
import java.util.*
import kotlin.collections.ArrayList

private const val TAG = "AddGalleryGifticon"
class AddGalleryGifticon(
    private val mainActivity: MainActivity,
    private val mContext: Context,
    private val _contentResolver: ContentResolver
): Fragment() {
    private val sp = SharedPreferencesUtil(mContext)
    private val newImgUri = mutableListOf<Uri>()
    private lateinit var binding: DialogProgressBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogProgressBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}