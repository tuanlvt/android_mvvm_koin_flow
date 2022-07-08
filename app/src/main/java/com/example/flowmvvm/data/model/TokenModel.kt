package  com.example.flowmvvm.data.model

import com.google.gson.annotations.Expose

class TokenModel {
    @Expose
    var tokenType: String? = null

    @Expose
    var accessToken: String? = null

    @Expose
    var refreshToken: String? = null
}
