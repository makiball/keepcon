package kr.co.toplink.keepcon.ui.add

import android.app.Activity
import android.content.ClipData
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.Rect
import android.net.Uri
import android.os.*
import android.provider.MediaStore.Images
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.loader.content.CursorLoader
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.soundcloud.android.crop.Crop

import kr.co.toplink.keepcon.R
import kr.co.toplink.keepcon.config.ApplicationClass
import kr.co.toplink.keepcon.databinding.FragmentAddBinding
import kr.co.toplink.keepcon.dto.*
import kr.co.toplink.keepcon.ui.common.MainActivity
import kr.co.toplink.keepcon.ui.common.onSingleClickListener
import kr.co.toplink.keepcon.ui.home.HomeFragment
import kr.co.toplink.keepcon.ui.popup.GifticonDialogFragment.Companion.isShow
import kr.co.toplink.keepcon.viewmodel.AddViewModel
import kr.co.toplink.keepcon.viewmodel.ViewModelFactory
import kotlinx.coroutines.*
import kr.co.toplink.keepcon.ui.home.ProductBox
import kr.co.toplink.keepcon.util.BarcodeAndTextExtractor
import java.io.*
import kotlin.collections.ArrayList

private const val TAG = "AddFragment"
class AddFragment : Fragment(), onItemClick{
    private lateinit var binding: FragmentAddBinding
    private lateinit var mainActivity: MainActivity
    private val viewModel: AddViewModel by viewModels { ViewModelFactory(requireContext()) }

    //새로 만들자
    private var gifticonItemList = ArrayList<GifticonItemList>()
    private val regex = "[^0-9,]".toRegex()
    private var gifticonImg : GifticonImg? = null

    private var loadingDialog = ProgressDialog(false)
    private lateinit var addImgAdapter: AddImgAdapter
    val user = ApplicationClass.sharedPreferencesUtil.getUser()
    var imgNum = 0
    var clickCv = ""

    val PRODUCT = "Product"
    val BARCODE = "Barcode"

    companion object{
        var chkCnt = 1
        var clickItemPos = 0
    }

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
        binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chkCnt = 1
        openGalleryFirst()

        binding.cvAddCoupon.setOnClickListener {
            makeProgressDialogOnBackPressed()
            openGalleryFirst()
        }

        binding.cvProductImg.setOnClickListener(object : onSingleClickListener(){
            override fun onSingleClick(v: View) {
                clickCv = PRODUCT
                seeCropImgDialog(gifticonItemList[imgNum], PRODUCT)
            }
        })

        binding.cvBarcodeImg.setOnClickListener(object : onSingleClickListener(){
            override fun onSingleClick(v: View) {
                clickCv = BARCODE
            }
        })

        binding.btnOriginalSee.setOnClickListener {
            if (gifticonItemList.size != 0){
                gifticonImg?.let { it -> seeOriginalImgDialog(it) }
            }
        }

        binding.cbPrice.setOnClickListener{

        }

        binding.btnRegi.setOnClickListener {

        }
    }

    private val result =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { it ->
            when (it.resultCode) {
                Activity.RESULT_OK -> {
                    val clipData = it.data!!.clipData

                    if (clipData != null) {  //첫 add
                        gifticonItemList.clear()

                        runBlocking {
                            supervisorScope {
                                gallyadd(clipData)
                            }
                        }

                    } else {  //수동 크롭
                        if (clickCv == PRODUCT){
                            //productImgUris[imgNum] = GifticonImg(Crop.getOutput(it.data))
                            //delImgUris.add(productImgUris[imgNum].imgUri)
                        } else if (clickCv == BARCODE){
                            //barcodeImgUris[imgNum] = GifticonImg(Crop.getOutput(it.data))
                            //delImgUris.add(barcodeImgUris[imgNum].imgUri)
                        }

                        Log.d(TAG,"=====> 크롭으로 자른후 !! ${Crop.getOutput(it.data)}")

                        // updateGifticonInfo(imgNum)
                        fillContent(imgNum)
                    }
                }

                Activity.RESULT_CANCELED -> {
                    if (gifticonItemList.size == 0){ // add탭 클릭 후 이미지 선택 안하고 뒤로가기 클릭 시
                        mainActivity.changeFragment(HomeFragment())
                    }
                }
            }
        }


    //리스트 이미지 등록
    private suspend fun gallyadd(clipData: ClipData){

        gifticonItemList.clear()

        for (i in 0 until clipData.itemCount) {
            val originalImgUri = clipData.getItemAt(i).uri
            if (!getFileSize(originalImgUri)) {
                continue
            }

            val extractor = BarcodeAndTextExtractor(requireContext())
            val (barcodes, texts) = extractor.extractBarcodeAndText(originalImgUri)

            var barcodeNum : String? = null
            var barcodePos : String? = null

            var productName : String? = null
            var brand : String? = null
            var productPos = ProductBox.ETC.box

            // 바코드 정보 활용
            for (barcode in barcodes) {
                Log.d(TAG,"=====> ${barcode.boundingBox}")
                barcodeNum = barcode.rawValue
                barcodePos = barcode.boundingBox.toString().replace("-",",")
                barcodePos = barcodePos.replace(regex,"")
            }
            //Log.d(TAG,"=====> $barcodePos $productPos")

            //바코드 있는것만
            if(barcodeNum != null) {
                if(texts.size > 0) {

                    productName = texts.get(0)

                    //카카오 설정
                    val kakao = texts.indexOf("kakaotalk선물하기")
                    if(kakao != -1) {
                      productName = texts.get(1)
                      brand = texts.get(0)
                      productPos = ProductBox.KAKAO.box
                    }
                }

                gifticonItemList.add(
                    GifticonItemList(
                        barcodeNum = barcodeNum,
                        barcodePos = barcodePos,
                        barcode_filepath = originalImgUri,
                        barcode_bitmap = cropToBitmap(uriToBitmap(originalImgUri), barcodePos!!),
                        productName = productName,
                        productPos = productPos,
                        productName_bitmap = cropToBitmap(uriToBitmap(originalImgUri), productPos),
                        brand = brand
                    )
                )
            }
        }

        Log.d(TAG,"=====> $gifticonItemList")
        fillContent(0)
        makeImgList()
        changeProgressDialogState(false)
    }

    // get img size
    private fun getFileSize(imgUri: Uri): Boolean{
        val file = File(getPath(imgUri))
        val fileSize = Integer.parseInt((file.length()).toString())
        if(fileSize > 1040000){
            return false
        }
        return true
    }

    // 이미지 절대경로 가져오기
    private fun getPath(uri: Uri):String{
        val data:Array<String> = arrayOf(Images.Media.DATA)
        val cursorLoader = CursorLoader(requireContext(), uri, data, null, null, null)
        val cursor = cursorLoader.loadInBackground()!!
        val idx = cursor.getColumnIndexOrThrow(Images.Media.DATA)
        cursor.moveToFirst()
        return cursor.getString(idx)
    }


    // 사진추가 버튼 클릭 시 뒤로가기
    private fun makeProgressDialogOnBackPressed(){
        loadingDialog = ProgressDialog(true)
    }

    // 상태에 따라 로딩화면 만들기/없애기
    private fun changeProgressDialogState(state: Boolean){
        if (state){
            loadingDialog.show(mainActivity.supportFragmentManager, null)
        } else{
            loadingDialog.dismiss()
        }
    }

    // View 값 채우기
    private fun fillContent(idx: Int){
        imgNum = idx

        gifticonImg = GifticonImg(gifticonItemList[imgNum].barcode_filepath)

        binding.ivCouponImg.setImageBitmap(gifticonItemList[idx].productName_bitmap)
        binding.ivBarcodeImg.setImageBitmap(gifticonItemList[idx].barcode_bitmap)

        binding.etBarcode.setText(gifticonItemList[idx].barcodeNum)

        binding.etProductName.setText(gifticonItemList[idx].productName)
        binding.etProductBrand.setText(gifticonItemList[idx].brand)

        binding.cbPrice.isChecked = false
        binding.lPrice.visibility = View.GONE

        binding.ivCouponImgPlus.visibility = View.GONE
        binding.ivBarcodeImgPlus.visibility = View.GONE
        changeProgressDialogState(false)
    }

    override fun onClick(idx: Int) {

        fillContent(idx)

        /*
        updateGifticonInfo(idx)
        fillContent(idx)

        productChk()
        brandChk()
        brandBarcodeNum()
        dateFormat()
        setMemo()
         */
    }

    // add탭 클릭하자마자 나오는 갤러리
    private fun openGalleryFirst() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.setDataAndType(Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        result.launch(intent)

        changeProgressDialogState(true)
    }

    // cardView를 클릭했을 때 나오는 갤러리
    fun openGallery(idx: Int, fromCv: String) {
        val bitmap = gifticonItemList[idx].barcode_filepath?.let { uriToBitmap(it) }
        var destination:Uri? = "".toUri()

        if (fromCv == PRODUCT){
            destination = bitmap?.let { saveFile("popconImgProduct", it) }
        } else if (fromCv == BARCODE){
            destination = bitmap?.let { saveFile("popconImgBarcode", it) }
        }
        val crop = Crop.of(gifticonItemList[idx].barcode_filepath, destination).withAspect(10, 10)
        result.launch(crop.getIntent(mainActivity))
    }

    // 크롭한 이미지 저장
    private fun saveFile(fileName:String, bitmap: Bitmap):Uri?{
        val values = ContentValues()
        values.put(Images.Media.DISPLAY_NAME, fileName)
        values.put(Images.Media.MIME_TYPE, "image/jpeg")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(Images.Media.IS_PENDING, 1)
        }

        val uri = requireContext().contentResolver.insert(Images.Media.EXTERNAL_CONTENT_URI, values)
        if (uri != null) {
            val descriptor = requireContext().contentResolver.openFileDescriptor(uri, "w")

            if (descriptor != null) {
                val fos = FileOutputStream(descriptor.fileDescriptor)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
                fos.close()
                descriptor.close()

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    values.clear()
                    values.put(Images.Media.IS_PENDING, 0)
                    requireContext().contentResolver.update(uri, values, null, null)
                }
            }
        }
        return uri
    }

    // uri -> bitmap
    private fun uriToBitmap(uri:Uri): Bitmap{
        lateinit var bitmap:Bitmap
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireContext().contentResolver, uri))
        } else{
            bitmap = Images.Media.getBitmap(requireContext().contentResolver, uri)
        }

        return bitmap
    }

    private fun bitmapToUri(bitmap: Bitmap): Uri{
        bitmap.compress(
            Bitmap.CompressFormat.JPEG, 100, ByteArrayOutputStream()
        )

        val path = Images.Media.insertImage(
            requireContext().contentResolver, bitmap, "mmsBitmapToUri", null
        )

        return Uri.parse(path)
    }

    // 상단 리사이클러뷰 만들기
    private fun makeImgList(){
        addImgAdapter = AddImgAdapter(
            gifticonItemList,
            this
        )
        binding.rvCouponList.apply {
            adapter = addImgAdapter
            layoutManager = LinearLayoutManager(this.context, RecyclerView.HORIZONTAL, false)
            adapter!!.stateRestorationPolicy =
                RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        }
    }

    // 크롭된 이미지 다이얼로그
    private fun seeCropImgDialog(gifticonItemList: GifticonItemList, clickFromCv:String){
        val dialog = CropImgDialogFragment(gifticonItemList, clickFromCv)
        dialog.show(childFragmentManager, "CropDialog")
        dialog.setOnClickListener(object: CropImgDialogFragment.BtnClickListener{
            override fun onClicked(fromCv: String) {

                if (fromCv == PRODUCT){
                    openGallery(imgNum, PRODUCT)
                } else if (fromCv == BARCODE){
                    openGallery(imgNum, BARCODE)
                }

            }
        })
    }

    // 이미지 원본보기
    private fun seeOriginalImgDialog(gifticonImg: GifticonImg){
        OriginalImgDialogFragment(gifticonImg).show(
            childFragmentManager, "OriginalDialog"
        )
    }

    //크롭후 비트맵으로 저장
    private fun cropToBitmap(bitmap: Bitmap, pos : String) : Bitmap? {

        val pos_crop = pos.split(",").toTypedArray()
        if(pos_crop.size < 4) {
            return bitmap
        }

        val left = pos_crop[0].toInt()
        val top =  pos_crop[1].toInt()
        val right =  pos_crop[2].toInt()
        val bottom =  pos_crop[3].toInt()
        val cropRect = Rect(left, top, right, bottom)
        val croppedBitmap = Bitmap.createBitmap(bitmap, cropRect.left,cropRect.top, cropRect.width(),cropRect.height())

        Log.d(TAG,"=====> $right, $bottom")

        return croppedBitmap
    }

    override fun onDestroyView() {
        super.onDestroyView()
        changeProgressDialogState(false)

        mainActivity.bottomNav.menu
            .findItem(R.id.homeFragment).isChecked = true
    }

    override fun onDestroy() {
        super.onDestroy()

        mainActivity.hideBottomNav(false)
        isShow = false
    }
}