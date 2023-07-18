package kr.co.toplink.keepcon.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.korean.KoreanTextRecognizerOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.IOException


class BarcodeAndTextExtractor(private val context: Context) {

    suspend fun extractBarcodeAndText(uri: Uri): Pair<List<Barcode>, List<String>> =
        withContext(Dispatchers.Default) {

            val bitmap = uriToBitmap(uri)
            val image = InputImage.fromBitmap(bitmap, 0) //

            // 바코드 스캔
            val barcodes = scanBarcode(image)

            // 텍스트 인식
            val texts = recognizeText(image)

            Pair(barcodes, texts)
        }


    // uri -> bitmap
    private fun uriToBitmap(uri:Uri): Bitmap{
        lateinit var bitmap:Bitmap
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P){
            bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(context.contentResolver, uri))
        } else{
            bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        }

        return bitmap
    }


    private suspend fun scanBarcode(image: InputImage): List<Barcode> =
        withContext(Dispatchers.IO) {
            val options = BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS) // 바코드 포맷 설정
                .build()

            val scanner = BarcodeScanning.getClient(options)

            try {
                val result = scanner.process(image).await()
                result ?: emptyList()
            } catch (e: Exception) {
                emptyList()
            }
        }

    private suspend fun recognizeText(image: InputImage): List<String> =
        withContext(Dispatchers.IO) {
            val recognizer = TextRecognition.getClient(KoreanTextRecognizerOptions.Builder().build())

            try {
                val result = recognizer.process(image).await()
                result?.textBlocks?.map { it.text } ?: emptyList()
            } catch (e: Exception) {
                emptyList()
            }
        }
}
