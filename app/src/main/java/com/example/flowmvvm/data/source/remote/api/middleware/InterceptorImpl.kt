package  com.example.flowmvvm.data.source.remote.api.middleware

import androidx.annotation.NonNull
import com.example.flowmvvm.data.source.repositories.TokenRepository
import com.example.flowmvvm.utils.extension.notNull
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.net.HttpURLConnection

class InterceptorImpl(private var tokenRepository: TokenRepository?) : Interceptor {

    companion object {
        private const val TAG = "InterceptorImpl"
        private const val TOKEN_TYPE = "Bearer "
        private const val KEY_TOKEN = "Authorization"
    }

    private var mIsRefreshToken: Boolean = false

    @Throws(IOException::class)
    override fun intercept(@NonNull chain: Interceptor.Chain): Response {
        //TODO check connection

        val builder = initializeHeader(chain)
        var request = builder.build()
        var response = chain.proceed(request)

        if (!mIsRefreshToken && response.code == HttpURLConnection.HTTP_UNAUTHORIZED) {

            //TODO refresh Token or return exception

            builder.removeHeader(KEY_TOKEN)

            tokenRepository?.getToken()?.accessToken.notNull { accessToken ->
                builder.addHeader(KEY_TOKEN, TOKEN_TYPE + accessToken)
                request = builder.build()
                response = chain.proceed(request)
            }
        }

        return response
    }

    private fun initializeHeader(chain: Interceptor.Chain): Request.Builder {
        val originRequest = chain.request()
        val builder = originRequest.newBuilder()
            .header("Accept", "application/json")
            .addHeader("Cache-Control", "no-cache")
            .addHeader("Cache-Control", "no-store")
            .method(originRequest.method, originRequest.body)

        tokenRepository?.getToken()?.accessToken.notNull { accessToken ->
            builder.addHeader(KEY_TOKEN, TOKEN_TYPE + accessToken)
        }

        return builder
    }
}