package kr.co.toplink.keepcon.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.toplink.keepcon.config.ApplicationClass
import kr.co.toplink.keepcon.databinding.FragmentHomeBinding
import kr.co.toplink.keepcon.dto.Badge
import kr.co.toplink.keepcon.dto.Brand
import kr.co.toplink.keepcon.dto.Gifticon
import kr.co.toplink.keepcon.ui.common.EventObserver
import kr.co.toplink.keepcon.ui.brandtab.BrandTabFragment
import kr.co.toplink.keepcon.ui.common.MainActivity
import kr.co.toplink.keepcon.ui.common.PopconSnackBar
import kr.co.toplink.keepcon.ui.history.HistoryDialogFragment
import kr.co.toplink.keepcon.ui.history.HistoryFragment
import kr.co.toplink.keepcon.ui.popup.GifticonDialogFragment
import kr.co.toplink.keepcon.ui.popup.GifticonDialogFragment.Companion.isShow
import kr.co.toplink.keepcon.ui.popup.GifticonViewAdapter
import kr.co.toplink.keepcon.ui.settings.SettingsFragment
import kr.co.toplink.keepcon.util.ShakeDetector
import kr.co.toplink.keepcon.util.SharedPreferencesUtil
import kr.co.toplink.keepcon.viewmodel.GifticonViewModel
import kr.co.toplink.keepcon.viewmodel.ViewModelFactory

class HomeFragment : Fragment() {

    private val TAG = this.javaClass.simpleName

    private lateinit var binding: FragmentHomeBinding
    private lateinit var shakeDetector: ShakeDetector
    lateinit var gifticonAdapter: GiftconAdapter
    private val viewModel: GifticonViewModel by activityViewModels { ViewModelFactory(requireContext()) }
    private lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        isShow = false
        mainActivity = activity as MainActivity
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: ")
        setSensor()
        mainActivity.hideBottomNav(false)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        mainActivity.updateStatusBarColor("#FFFFFF")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d(TAG, "onViewCreated: ")
        binding.user = SharedPreferencesUtil(requireContext()).getUser()
        binding.viewModel = viewModel

        setGifticonAdapter()
        openGifticonDialog()

        binding.btnHistory.setOnClickListener {
            mainActivity.addFragment(HistoryFragment())
        }
    }

    private fun openGifticonDialog() {
        viewModel.openGifticonDialogEvent.observe(viewLifecycleOwner, EventObserver {
            Log.d(TAG, "openGifticonDialog: $it")
            val args = Bundle()
            args.putSerializable("gifticon", it)
            val dialogFragment = HomeDialogFragment()
            dialogFragment.arguments = args
            dialogFragment.show(childFragmentManager, "popup")
        })
    }


    //홈 기프티콘 어댑터 설정
    private fun setGifticonAdapter() {
        gifticonAdapter = GiftconAdapter(viewModel)
        viewModel.getGifticonByUser(SharedPreferencesUtil(requireContext()).getUser())
        viewModel.gifticons.observe(viewLifecycleOwner) {
            binding.tvNoGifticon.isVisible = it.isEmpty()

            binding.rvGifticon.apply {
                adapter = gifticonAdapter
                layoutManager = GridLayoutManager(context, 2)
                adapter!!.stateRestorationPolicy =
                    RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
            }

            gifticonAdapter.apply {
                submitList(it)
            }
        }
    }

    //홈화면 켜지면 센서 설정
    private fun setSensor() {
        shakeDetector = ShakeDetector()
        shakeDetector.setOnShakeListener(object : ShakeDetector.OnShakeListener {
            override fun onShake(count: Int) {
                if (!isShow) {
                    activity?.let {
                        GifticonDialogFragment().show(it.supportFragmentManager, "popup")
                    }
                }
            }
        })

        MainActivity().setShakeSensor(requireContext(), shakeDetector)
    }
}
