package kr.co.toplink.keepcon.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView
import kr.co.toplink.keepcon.dto.GifticonItemList

class CustomImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : AppCompatImageView(context, attrs, defStyle) {

    private val TAG = "bitmap.canvas!!!!"
    private lateinit var _gifticonItemList : GifticonItemList

    private val rectPaint: Paint = Paint().apply {
        color = 0xFF00FF00.toInt()
        style = Paint.Style.STROKE
        strokeWidth = 5f
    }

    private val grayPaint: Paint = Paint().apply {
        color = Color.parseColor("#80808080") // 회색 투명처리 색상
        style = Paint.Style.FILL
    }

    private var startX: Float = 0f
    private var startY: Float = 0f
    private var rect: RectF? = null
    private var isDragging = false
    private var width: Int? = 0
    private var height: Int? = 0
    private var first: Int? = 0


    // 사용자 정의 비트맵 설정 메서드
    fun setCustomBitmap(gifticonItemList: GifticonItemList) {

        _gifticonItemList = gifticonItemList

        invalidate() // View를 다시 그리도록 호출
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        first = 1

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
                startY = event.y
                rect = RectF(startX, startY, startX, startY)
                invalidate()
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                rect?.right = event.x
                rect?.bottom = event.y
                isDragging = true
                invalidate()
                return true
            }
            MotionEvent.ACTION_UP -> {
                if (isDragging) {
                    isDragging = false
                } else {
                    rect = null
                }
                invalidate()
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if(first == 0) {
            val gap_width = canvas.width / _gifticonItemList.gifticon_file_width
            val gap_height = canvas.height / _gifticonItemList.gifticon_file_height

            val pos_crop = _gifticonItemList.productPos!!.split(",").toTypedArray()

            val left = pos_crop[0].toFloat() * gap_width
            val top =  pos_crop[1].toFloat()  * gap_height
            val right =  pos_crop[2].toFloat()  * gap_width
            val bottom =  pos_crop[3].toFloat() * gap_height

            rect = RectF(left,top,right,bottom )
            Log.d(TAG, "======>first $rect ${canvas.width} ${_gifticonItemList.gifticon_file_width}")
        }


        // 회색 투명처리
        rect?.let {

            if(isDragging == false) {

                //데이터 저장
                Log.d(TAG, "======> $rect")
            }
            canvas.drawRect(it, grayPaint)
            canvas.drawRect(it, rectPaint)
        }
    }

    /*
    private lateinit var customBitmap: Bitmap
    private val paint = Paint()
    private var customRect = Rect()

    init {
        //paint.color = Color.parseColor("#80000000") // 투명한 회색, 50% 투명도
        paint.color = Color.GRAY
        paint.isAntiAlias = true // 안티앨리어싱 활성화 (부드럽게 그리기 위함)
        paint.strokeWidth = 10f // 그리기 선의 두께 설정

        customRect.left = 0
        customRect.top = 0
        customRect.right = 100
        customRect.bottom = 100
    }

    // 사용자 정의 비트맵 설정 메서드
    fun setCustomBitmap(bitmap: Bitmap, rect: Rect) {
        customBitmap = bitmap
        customRect = rect
        invalidate() // View를 다시 그리도록 호출

    }


    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        // Canvas에 그리기
        canvas?.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)


        //val scaledBitmap = Bitmap.createScaledBitmap(customBitmap, width, height, true)
        // 화면 중앙에 비트맵을 그리기 위해 시작 위치를 계산합니다.
        val x = (width - customBitmap.width) / 2f
        val y = (height - customBitmap.height) / 2f
        canvas?.drawBitmap(customBitmap, x, y, null)
    }
     */
}