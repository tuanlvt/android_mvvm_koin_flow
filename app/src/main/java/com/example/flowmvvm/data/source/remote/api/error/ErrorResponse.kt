package  com.example.flowmvvm.data.source.remote.api.error

import com.example.flowmvvm.utils.Constants
import com.example.flowmvvm.utils.LogUtils
import com.example.flowmvvm.utils.extension.notNull
import com.example.flowmvvm.utils.extension.toStringWithFormatPattern
import com.google.gson.Gson
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import okio.IOException
import retrofit2.HttpException

/**
 * Created by MyPC on 30/10/2017.
 */

class ErrorResponse {
    
    @Expose
    @SerializedName("message")
    val message: String? = null
    
    @Expose
    @SerializedName("error")
    val errorData: ErrorData? = null
    
    val error: ErrorData
        get() = errorData ?: ErrorData()
    
    inner class ErrorData {
        @Expose
        @SerializedName("code")
        val code: Int = 0
        
        @Expose
        @SerializedName("description")
        private val messages: List<String>? = null
        
        val message: String?
            get() = if (messages.isNullOrEmpty()) {
                null
            } else {
                messages.toStringWithFormatPattern(Constants.PATTERN_FORMAT.ENTER_SPACE)
            }
    }
    
    companion object {
        
        private const val TAG = "ErrorResponse"
        
        fun convertToRetrofitException(throwable: Throwable): RetrofitException {
            if (throwable is RetrofitException) {
                return throwable
            }
            
            // A network error happened
            if (throwable is IOException) {
                return RetrofitException.toNetworkError(throwable)
            }
            
            // We had non-200 http error
            if (throwable is HttpException) {
                val response = throwable.response() ?: return RetrofitException.toUnexpectedError(throwable)
                
                response.errorBody().notNull {
                    return try {
                        val errorResponse = Gson().fromJson(it.string(), ErrorResponse::class.java)
                        
                        if (errorResponse != null && !errorResponse.message.isNullOrEmpty()) {
                            RetrofitException.toServerError(errorResponse)
                        } else {
                            RetrofitException.toHttpError(response)
                        }
                        
                    } catch (e: IOException) {
                        LogUtils.e(TAG, e.message.toString())
                        RetrofitException.toUnexpectedError(throwable)
                    }
                }
                
                return RetrofitException.toHttpError(response)
            }
            
            // We don't know what happened. We need to simply convert to an unknown error
            return RetrofitException.toUnexpectedError(throwable)
        }
    }
}
