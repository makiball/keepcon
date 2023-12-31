package kr.co.toplink.keepcon.config

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.room.Room
import com.google.gson.GsonBuilder
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import kr.co.toplink.keepcon.BuildConfig
import kr.co.toplink.keepcon.database.AppDatabase
import kr.co.toplink.keepcon.repository.gifticonitem.GifticonItemDataSource
import kr.co.toplink.keepcon.repository.gifticonitem.GifticonItemLocalDataSource
import kr.co.toplink.keepcon.repository.gifticonitem.GifticonItemRepository
import kr.co.toplink.keepcon.repository.mms.MMSLocalDataSource
import kr.co.toplink.keepcon.repository.mms.MMSRepository
import kr.co.toplink.keepcon.util.AuthInterceptor
import kr.co.toplink.keepcon.util.SharedPreferencesUtil
import kr.co.toplink.keepcon.util.TokenAuthenticator
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

class ApplicationClass : Application() {

    private val TAG = this.javaClass.simpleName

    companion object {
        const val SERVER_URL = BuildConfig.BASE_URL

        lateinit var sharedPreferencesUtil: SharedPreferencesUtil
        lateinit var retrofit: Retrofit
        lateinit var refreshRetrofit: Retrofit

        fun makeRetrofit(url: String): Retrofit {
            val okHttpClient = OkHttpClient.Builder()
                .readTimeout(10000, TimeUnit.MILLISECONDS)
                .connectTimeout(10000, TimeUnit.MILLISECONDS)
                // 로그캣에 okhttp.OkHttpClient로 검색하면 http 통신 내용을 보여줍니다.
                .authenticator(TokenAuthenticator())
                .addInterceptor(AuthInterceptor())
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()

            retrofit = Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
                .client(okHttpClient)
                .build()

            return retrofit
        }

        fun makeRefreshRetrofit(url: String): Retrofit {
            val okHttpClient = OkHttpClient.Builder()
                .readTimeout(5000, TimeUnit.MILLISECONDS)
                .connectTimeout(5000, TimeUnit.MILLISECONDS)
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()

            refreshRetrofit = Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
                .client(okHttpClient)
                .build()

            return refreshRetrofit
        }
    }

    private var database: AppDatabase? = null
    fun provideDatabase(applicaContext: Context): AppDatabase {
        return database ?: kotlin.run {
            Room.databaseBuilder(
                applicaContext,
                AppDatabase::class.java,
                "popcon-local"
            ).fallbackToDestructiveMigration()
                .build().also {
                database = it
            }
        }
    }

    private var mmsRepository: MMSRepository? = null
    fun provideMMSRepository(context: Context): MMSRepository {
        return mmsRepository ?: kotlin.run {
            val dao = provideDatabase(context.applicationContext).mmsDao()
            MMSRepository(MMSLocalDataSource(dao)).also {
                mmsRepository = it
            }
        }
    }

    private var gifticonItemRepository: GifticonItemRepository? = null
    fun provideGifticonItemRepository(context: Context): GifticonItemRepository {
        return gifticonItemRepository ?: kotlin.run {
            val dao = provideDatabase(context.applicationContext).mmsDao()
            GifticonItemRepository(GifticonItemLocalDataSource(dao)).also{
                gifticonItemRepository = it
            }
        }
    }

    private fun kakaoLoginState() {
        KakaoSdk.init(applicationContext, BuildConfig.KAKAO_API_KEY)

        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null) {
                // 동의화면에서 동의 누르기 전에 뜸
                Log.d(TAG, "init_error: ${error}")
                if (tokenInfo == null) {
                    // 디비에 값 저장
                    Log.d(TAG, "kakaoLoginState: ")
                }
            } else if (tokenInfo != null) {
                // 로그인 되어있는 상태
                Log.d(TAG, "init_tokenInfo: ${tokenInfo}")
            }
        }
    }

    private fun setNaverModule(context: Context) {
        NaverIdLoginSDK.initialize(
            context,
            BuildConfig.naverClientID,
            BuildConfig.naverClientSecret,
            "POPCON"
        )
    }

    override fun onCreate() {
        sharedPreferencesUtil = SharedPreferencesUtil(applicationContext)

        super.onCreate()

        makeRetrofit(SERVER_URL)
        kakaoLoginState()
        makeRefreshRetrofit(SERVER_URL)
        setNaverModule(applicationContext)
    }
}