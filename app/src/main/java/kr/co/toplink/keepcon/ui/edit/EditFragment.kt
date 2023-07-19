package kr.co.toplink.keepcon.ui.edit

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import kr.co.toplink.keepcon.config.ApplicationClass
import kr.co.toplink.keepcon.databinding.FragmentEditBinding
import kr.co.toplink.keepcon.dto.*
import kr.co.toplink.keepcon.ui.common.MainActivity
import kr.co.toplink.keepcon.ui.common.PopconSnackBar
import kr.co.toplink.keepcon.ui.home.HomeFragment
import kr.co.toplink.keepcon.ui.popup.GifticonDialogFragment.Companion.isShow
import kr.co.toplink.keepcon.ui.popup.ImageDialogFragment
import kr.co.toplink.keepcon.util.SharedPreferencesUtil
import kr.co.toplink.keepcon.util.Utils
import kr.co.toplink.keepcon.viewmodel.GifticonViewModel
import kr.co.toplink.keepcon.viewmodel.ViewModelFactory
import kotlinx.coroutines.*
import java.text.SimpleDateFormat
import java.util.*

private const val TAG = "EditFragment"

class EditFragment : Fragment() {
    private lateinit var binding: FragmentEditBinding
    private lateinit var mainActivity: MainActivity
    private lateinit var barNum: String
    private lateinit var gifticon: Gifticon
    private var gifticonInfo = AddInfo()
    private var gifticonEffectiveness = AddInfoNoImgBoolean()

    private val editViewModel: EditViewModel by activityViewModels { ViewModelFactory(requireContext()) }
    private val viewModel: GifticonViewModel by activityViewModels { ViewModelFactory(requireContext()) }
    val user = ApplicationClass.sharedPreferencesUtil.getUser()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onStart() {
        super.onStart()
        mainActivity.hideBottomNav(true)
        isShow = true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditBinding.inflate(inflater, container, false)

        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editViewModel.barNum.observe(viewLifecycleOwner) {
            barNum = it
        }

        binding.cbPrice.setOnClickListener {

        }
    }

    override fun onDestroy() {
        super.onDestroy()

        mainActivity.hideBottomNav(false)
        isShow = false
    }
}