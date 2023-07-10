package kr.co.toplink.keepcon.ui.common

import android.graphics.Bitmap
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.shape.CornerFamily
import com.google.android.material.shape.MaterialShapeDrawable
import com.kakao.sdk.common.util.Utility
import kr.co.toplink.keepcon.R
import kr.co.toplink.keepcon.databinding.ActivityMainBinding
import kr.co.toplink.keepcon.ui.util.CheckPermission
import kr.co.toplink.keepcon.ui.util.SharedPreferencesUtil

import com.google.android.gms.wearable.*
import com.google.android.gms.wearable.Wearable
import kr.co.toplink.keepcon.ui.util.ShakeDetector

class MainActivity : AppCompatActivity() {

    private val TAG = this.javaClass.simpleName

    private lateinit var binding: ActivityMainBinding
    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private lateinit var checkPermission: CheckPermission
    private var permissionGranted = false
    lateinit var bottomNav: BottomNavigationView

    val PERMISSION_REQUEST_CODE = 8

    init {
        instance = this
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bottomNav = binding.bottomNav
        Log.d(TAG, "keyhash : ${Utility.getKeyHash(this)}")

        setNavBar()
        checkPermissions()
        callMMSReceiver()
        chkNewMMSImg()

        //자동로그인
        if (SharedPreferencesUtil(this).getUser().email != "") {
            Log.d(TAG, "onCreate: 로그인됨")
            sendUserData()
            changeFragment(HomeFragment())
            makeGalleryDialogFragment(applicationContext, contentResolver)
        } else {
            Log.d(TAG, "onCreate: 로그인 필요")
            changeFragment(LoginFragment())
        }
    }

    //navigation bar 설정
    private fun setNavBar() {
        window.navigationBarColor = Color.WHITE;

        val radius = resources.getDimension(R.dimen.radius_small)
        val bottomNavigationViewBackground = binding.bottomNav.background as MaterialShapeDrawable
        bottomNavigationViewBackground.shapeAppearanceModel =
            bottomNavigationViewBackground.shapeAppearanceModel.toBuilder()
                .setTopRightCorner(CornerFamily.ROUNDED, radius)
                .setTopLeftCorner(CornerFamily.ROUNDED, radius)
                .build()

        binding.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    true
                }
                R.id.addFragment -> {
                    true
                }
                R.id.mapFragment -> {
                    true
                }
                R.id.settingsFragment -> {
                    true
                }
                else -> false
            }
        }

        //재선택 방지
        binding.bottomNav.setOnItemReselectedListener { menuItem ->
            when (menuItem.itemId) {

            }
        }
    }

    companion object {
        var shakeDetector = ShakeDetector()
        var fromMMSReceiver: Bitmap? = null
        const val channel_id = "keepcon_user"

        private var instance: MainActivity? = null
        fun getInstance(): MainActivity? {
            return instance
        }
    }
}