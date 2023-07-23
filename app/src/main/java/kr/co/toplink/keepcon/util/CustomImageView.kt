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

    private val paint: Paint = Paint()
    private lateinit var bitmap: Bitmap

    // 사용자 정의 비트맵 설정 메서드
    fun setCustomBitmap(bitmap: Bitmap) {

        this.bitmap = bitmap

        invalidate() // View를 다시 그리도록 호출
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val aspectRatio = (bitmap.width.toFloat() / bitmap.height.toFloat())

        val targetWidth: Float
        val targetHeight: Float

        if (aspectRatio > 1.0f) {
            // 이미지가 가로로 넓은 경우
            targetWidth = width.toFloat()
            targetHeight = (width / aspectRatio)
        } else {
            // 이미지가 세로로 높은 경우
            targetHeight = height.toFloat()
            targetWidth = (height * aspectRatio)
        }

        Log.d(TAG, "==> 세로가로 높이 $aspectRatio")

        val left = (width - targetWidth) / 2
        val top = (height - targetHeight) / 2

        val rectF = RectF(left.toFloat(), top.toFloat(), (left + targetWidth).toFloat(), (top + targetHeight).toFloat())

        canvas.drawColor(Color.WHITE)
        canvas.drawBitmap(bitmap, null, rectF, paint)
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