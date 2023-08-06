package kr.co.toplink.keepcon.ui.add

import android.app.Activity
import android.content.ClipData
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.graphics.Rect
import android.net.Uri
import android.os.*
import android.provider.MediaStore.Images
import android.text.Editable
import android.text.TextWatcher
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
import kr.co.toplink.keepcon.viewmodel.GifticonItemViewModel
import kotlinx.coroutines.*
import kr.co.toplink.keepcon.ui.home.ProductBox
import kr.co.toplink.keepcon.util.BarcodeAndTextExtractor
import java.io.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

private const val TAG = "AddFragment"
class AddFragment : Fragment(), onItemClick{
    private lateinit var binding: FragmentAddBinding
    private lateinit var mainActivity: MainActivity
    //private val viewModel: AddViewModel by viewModels { ViewModelFactory(requireContext()) }

    private val viewModel : GifticonItemViewModel by viewModels { ViewModelFactory(requireContext()) }

    //새로 만들자
    private var gifticonItemList = ArrayList<GifticonItemList>()
    private val regex = "[^0-9,]".toRegex()
    private var gifticonImg : GifticonImg? = null

    private lateinit var keepTempImg : Uri

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


        Log.d(TAG, "=====> ${gifticonItemList.size}")

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
                seeCropImgDialog(gifticonItemList[imgNum], BARCODE)
            }
        })

        binding.btnOriginalSee.setOnClickListener {
            if (gifticonItemList.size != 0){
                gifticonImg?.let { it -> seeOriginalImgDialog(it) }
            }
        }

        binding.cbPrice.setOnClickListener{
            clickChkState(imgNum)
        }

        productChk()
        brandChk()
        brandBarcodeNum()
        dateFormat()
        setMemo()

        binding.btnRegi.setOnClickListener {
            Log.d(TAG,"=========> ${gifticonItemList[imgNum]}")

            viewModel.addGifticonItem(
                GifticonItem(
                    barcodeNum = gifticonItemList[imgNum].barcodeNum!!,
                    gifticon_filepath = gifticonItemList[imgNum].gifticon_filepath.toString(),
                    gifticon_file_width = gifticonItemList[imgNum].gifticon_file_width,
                    gifticon_file_height = gifticonItemList[imgNum].gifticon_file_height,
                    barcodePos = gifticonItemList[imgNum].barcodePos,
                    productName = gifticonItemList[imgNum].productName,
                    productPos = gifticonItemList[imgNum].productPos,
                    brand = gifticonItemList[imgNum].brand,
                    due = gifticonItemList[imgNum].due,
                    price = gifticonItemList[imgNum].price,
                    memo = gifticonItemList[imgNum].memo,
                    state = gifticonItemList[imgNum].state
                )
            )

            Log.d(TAG,"=====> ${viewModel.getGifticonItem()} ")

            gifticonItemList.removeAt(imgNum)

            if (gifticonItemList.size == 0){ // add탭 클릭 후 이미지 선택 안하고 뒤로가기 클릭 시
                mainActivity.changeFragment(HomeFragment())
                return@setOnClickListener
            }
            fillContent(imgNum)
            makeImgList()
        }
    }

    // 상품명 리스트에 저장
    private fun productChk(){
        binding.etProductName.addTextChangedListener (object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                val pLength = p0.toString().length
                if(pLength < 1){
                    binding.tilProductName.error = "상품명을 입력해주세요"
                } else{
                    binding.tilProductName.error = null
                    binding.tilProductName.isErrorEnabled = false
                    gifticonItemList[imgNum].productName = binding.etProductName.text.toString()
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }

    // 브랜드 존재여부 검사
    private fun brandChk() {
        binding.etProductBrand.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(p0: Editable?) {
                val pLength = p0.toString().length
                if(pLength < 1){
                    binding.tilProductBrand.error = "브랜드명을 입력해주세요"
                } else{
                    binding.tilProductBrand.error = null
                    binding.tilProductBrand.isErrorEnabled = false
                    gifticonItemList[imgNum].brand = binding.etProductBrand.text.toString()
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }

    // 바코드 번호 검사
    private fun brandBarcodeNum() {
        binding.etBarcode.addTextChangedListener(object : TextWatcher{

            override fun afterTextChanged(p0: Editable?) {
                val pLength = p0.toString().length
                if(pLength < 1){
                    binding.tilBarcode.error = "바코드를 입력해주세요"
                } else{
                    binding.tilBarcode.error = null
                    binding.tilBarcode.isErrorEnabled = false
                    gifticonItemList[imgNum].barcodeNum = binding.etBarcode.text.toString()
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }

    //유효기간 검사
    val dateArr = arrayOf(31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
    private fun dateFormat() {
        binding.etDate.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                val dateLength = binding.etDate.text!!.length
                val nowText = p0.toString()

                when (dateLength){
                    10 -> {
                        val newYear = nowText.substring(0, 4).toInt()
                        val newMonth = nowText.substring(5, 7).toInt()
                        val newDay = nowText.substring(8).toInt()

                        val nowYear = SimpleDateFormat("yyyy", Locale.getDefault()).format(System.currentTimeMillis()).toInt()
                        val nowDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(System.currentTimeMillis())
                        val nowDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(nowDateFormat)
                        var newDate = Date()
                        try {
                            newDate =  SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(p0.toString())!!
                        } catch (e: java.lang.Exception){
                            newDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(nowDateFormat)!!
                        }

                        val calDate = newDate.compareTo(nowDate)

                        if (newYear > 2100 || newYear.toString().length < 4){
                            binding.tilDate.error = "정확한 날짜를 입력해주세요"
                        } else if(newMonth < 1 || newMonth > 12){
                            binding.tilDate.error = "정확한 날짜를 입력해주세요"
                        } else if(newDay > dateArr[newMonth-1] || newDay == 0){
                            binding.tilDate.error = "정확한 날짜를 입력해주세요"
                        } else if (calDate < 0){
                            binding.tilDate.error = "이미 지난 날짜입니다"
                        } else{
                            binding.tilDate.error = null
                            binding.tilDate.isErrorEnabled = false
                            gifticonItemList[imgNum].due = nowText
                        }
                    }
                    else -> {
                        binding.tilDate.error = "정확한 날짜를 입력해주세요"
                    }
                }

                if (dateLength < 10){
                    binding.tilDate.error = "정확한 날짜를 입력해주세요"
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //p0: 현재 입력된 문자열, p1: 새로 추가될 문자열 위치, p2: 변경될 문자열의 수, p3: 새로 추가될 문자열 수
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //p0: 현재 입력된 문자열, p1: 새로 추가될 문자열 위치, p2: 삭제된 기존 문자열 수, p3: 새로 추가될 문자열 수
                val dateLength = binding.etDate.text!!.length
                if(dateLength==4 && p1!=4 || dateLength==7 && p1!=7){
                    val add = binding.etDate.text.toString() + "-"
                    binding.etDate.setText(add)
                    binding.etDate.setSelection(add.length)
                }
            }
        })
    }

    // memo를 리스트에 저장
    private fun setMemo(){
        binding.etWriteMemo.addTextChangedListener (object : TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                gifticonItemList[imgNum].memo = binding.etWriteMemo.text.toString()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
    }

    private val result_gallery =
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

                    }
                }

                Activity.RESULT_CANCELED -> {
                    if (gifticonItemList.size == 0){ // add탭 클릭 후 이미지 선택 안하고 뒤로가기 클릭 시
                        mainActivity.changeFragment(HomeFragment())
                    }
                }
            }
        }

    private val result_crop =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            when (it.resultCode) {
                Activity.RESULT_OK -> {
                    if (clickCv == PRODUCT){
                        //productImgUris[imgNum] = GifticonImg(Crop.getOutput(it.data))
                        //delImgUris.add(productImgUris[imgNum].imgUri)

                        gifticonItemList[imgNum].productName_bitmap = uriToBitmap(Crop.getOutput(it.data))

                    } else if (clickCv == BARCODE){
                        //barcodeImgUris[imgNum] = GifticonImg(Crop.getOutput(it.data))
                        //delImgUris.add(barcodeImgUris[imgNum].imgUri)

                        gifticonItemList[imgNum].barcode_bitmap = uriToBitmap(Crop.getOutput(it.data))
                    }

                    delCropImg(keepTempImg)

                    // updateGifticonInfo(imgNum)
                    fillContent(imgNum)
                }

                Activity.RESULT_CANCELED -> {
                    delCropImg(keepTempImg)
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
            var due : String? = null

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

                    for(text in texts) {
                        if(extractPatternDue(text) != "") {
                            due = extractPatternDue(text)

                            Log.d(TAG,"====> text ${extractPatternDue(text)}")
                        }

                        if(isResourceKeyExists(text) != "") {
                            brand = isResourceKeyExists(text)
                        }

                    }

                    productName = texts.get(0)

                    //카카오 설정
                    val kakao = texts.indexOf("kakaotalk선물하기")
                    if(kakao != -1) {
                      productName = texts.get(1)
                      brand = texts.get(0)
                      productPos = ProductBox.KAKAO.box
                    }
                }

                val imageSize = getBitmapSizeFromUri(requireContext(), originalImgUri)

                gifticonItemList.add(
                    GifticonItemList(
                        barcodeNum = barcodeNum,
                        barcodePos = barcodePos,
                        gifticon_filepath = originalImgUri,
                        gifticon_file_width = imageSize.first,
                        gifticon_file_height = imageSize.second,
                        barcode_bitmap = cropToBitmap(uriToBitmap(originalImgUri), barcodePos!!),
                        productName = productName,
                        productPos = productPos,
                        productName_bitmap = cropToBitmap(uriToBitmap(originalImgUri), productPos),
                        brand = brand,
                        due = due
                    )
                )

//                viewModel.addGifticonItemList(gifticonItemList)
//                viewModel.gifticonItemList.observe(viewLifecycleOwner,)

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

    // 이미지 크기 가져오기
    fun getBitmapSizeFromUri(context: Context, uri: Uri): Pair<Int, Int> {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true

        // 이미지 크기만 읽어옴
        val inputStream = context.contentResolver.openInputStream(uri)
        BitmapFactory.decodeStream(inputStream, null, options)
        inputStream?.close()

        val imageWidth = options.outWidth
        val imageHeight = options.outHeight

        return Pair(imageWidth, imageHeight)
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

        gifticonImg = GifticonImg(gifticonItemList[imgNum].gifticon_filepath)

        binding.ivCouponImg.setImageBitmap(gifticonItemList[imgNum].productName_bitmap)
        binding.ivBarcodeImg.setImageBitmap(gifticonItemList[imgNum].barcode_bitmap)

        binding.etBarcode.setText(gifticonItemList[imgNum].barcodeNum)

        binding.etProductName.setText(gifticonItemList[imgNum].productName)
        binding.etProductBrand.setText(gifticonItemList[imgNum].brand)
        binding.etDate.setText(gifticonItemList[imgNum].due)
        binding.etWriteMemo.setText(gifticonItemList[imgNum].memo)

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
        result_gallery.launch(intent)

        changeProgressDialogState(true)
    }

    // 체크박스 클릭 시 상태변화
    private fun clickChkState(idx: Int){
        val chkState = binding.cbPrice.isChecked
        if (!chkState){
            gifticonItemList[imgNum].price = -1
            binding.cbPrice.isChecked = false
            binding.lPrice.visibility = View.GONE
            binding.etPrice.setText("-1")
        } else{
            binding.cbPrice.isChecked = true
            binding.lPrice.visibility = View.VISIBLE
        }
    }

    // cardView를 클릭했을 때 나오는 갤러리
    fun openGallery(idx: Int, fromCv: String) {

        val bitmap = uriToBitmap(gifticonItemList[idx].gifticon_filepath )
        //val destination:Uri? = "".toUri()
/*
        if (fromCv == PRODUCT){
            destination = bitmap?.let { saveFile("popconImgProduct", it) }
        } else if (fromCv == BARCODE){
            destination = bitmap?.let { saveFile("popconImgBarcode", it) }
        }

 */

        keepTempImg = saveFile("keepTempImg", bitmap)!!

        val crop = Crop.of(gifticonItemList[idx].gifticon_filepath, keepTempImg)
        result_crop.launch(crop.getIntent(mainActivity))
    }

    // 크롭한 이미지 저장
    private fun saveFile(fileName:String, bitmap: Bitmap):Uri {
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
        return uri!!
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

    // 크롭되면서 새로 생성된 이미지 삭제
    fun delCropImg(delImgUri: Uri){
        val file = File(getPath(delImgUri))
        file.delete()
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

    fun extractPatternDue(inputString: String): String? {
        val regex_due = "20\\d{2}\\D+\\d{2}\\D+\\d{2}".toRegex()
        val regex_digit = "[^\\d]".toRegex()
        val regex_hyphen = "(\\d{4})(\\d{2})(\\d{2})".toRegex()
        val result_due = regex_due.find(inputString)?.value.toString()
        var result = regex_digit.replace(result_due, "")

        if(result.length >= 8) {
            result = result.replace(regex_hyphen, "$1-$2-$3")
        }
        return result
    }

    fun isResourceKeyExists(key: String): String {

        val wordsArray = key.split(" ").toTypedArray()
        var brand = ""
        val brandArray = resources.getStringArray(R.array.brand_array_name)

        wordsArray.forEach {
            if(brandArray.contains(it)) {
                brand = it
            }
        }

        return brand
/*
        for ((index, item) in brandArray.withIndex()) {

            Log.d(TAG, "=====>x $item")
            /*
            val resId: Int = requireContext().resources.getIdentifier(word, "brand", requireContext().packageName)
            if(resId != 0) {
                brand = word
            }
             */
        }
 */

    }

}