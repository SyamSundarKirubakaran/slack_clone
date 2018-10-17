package com.bugscript.slackup.Services

import android.content.Context
import android.content.Intent
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bugscript.slackup.Utilities.*
import org.json.JSONException
import org.json.JSONObject

object AuthService {

    var isLoggedIn = false
    var userEmail = ""
    var authToken = ""

    fun regsiterUser(context: Context, email:String, password:String,complete: (Boolean) -> Unit){
        val requestBody = buildupJSONObject(email,password)

        val registerRequest = object : StringRequest(Method.POST, URL_REGISTER, Response.Listener {response ->
            println(response)
            isLoggedIn = true
            complete(true)
        }, Response.ErrorListener {error ->
            Log.d("ERROR","Couln't register user: ${error}")
            complete(false)
        }){
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }

        Volley.newRequestQueue(context).add(registerRequest)
    }

    fun loginUser(context: Context, email: String, password: String,complete: (Boolean) -> Unit){
        val requestBody = buildupJSONObject(email,password)

        val loginRequest = object : JsonObjectRequest(Method.POST, URL_LOGIN, null, Response.Listener{ response ->
            println(response)
            try {
                userEmail = response.getString("user")
                authToken = response.getString("token")
                isLoggedIn = true
                complete(true)
            }catch (e : JSONException){
                Log.d("ERROR", "JSONException ${e.localizedMessage}")
            }
        }, Response.ErrorListener { error ->
            Log.d("ERROR","Couldn't login user: ${error}")
            complete(false)
        }){
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }
        }

        Volley.newRequestQueue(context).add(loginRequest)
    }


    fun createUser(context: Context, name: String, email: String, avatarName: String, avatarColor : String, complete: (Boolean) -> Unit){
        val requestBody = buildUpJSONCreateUser(name,email,avatarName,avatarColor)

        val createRequest = object : JsonObjectRequest(Method.POST, URL_CREATE_USER, null, Response.Listener {  response ->
            try{
                UserDataService.name = response.getString("name")
                UserDataService.email = response.getString("email")
                UserDataService.avatarColor = response.getString("avatarColor")
                UserDataService.avatarName = response.getString("avatarName")
                UserDataService.id = response.getString("_id")
                isLoggedIn = true
                complete(true)
            }catch (e : JSONException){
                Log.d("ERROR","error: ${e.localizedMessage}")
            }
        }, Response.ErrorListener { error ->
            Log.d("ERROR","Couldn't add user: ${error}")
            complete(false)
        }){
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return requestBody.toByteArray()
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String,String>()
                headers.put("Authorization","Bearer ${authToken}")
                return headers
            }
        }

        Volley.newRequestQueue(context).add(createRequest)
    }

    fun findUserByEmail(context: Context, complete: (Boolean) -> Unit){
        val findUserRequest = object : JsonObjectRequest(Method.GET, "$URL_GET_USER$userEmail", null, Response.Listener { response ->
            try{
                UserDataService.name = response.getString("name")
                UserDataService.email = response.getString("email")
                UserDataService.avatarName = response.getString("avatarName")
                UserDataService.avatarColor = response.getString("avatarColor")
                UserDataService.id = response.getString("_id")

                val userDataChange = Intent(BROADCAST_USER_DATA_CHNAGE)
                LocalBroadcastManager.getInstance(context).sendBroadcast(userDataChange)
                complete(true)

            }catch (e : JSONException){
                Log.d("JSON",e.localizedMessage)
            }
        }, Response.ErrorListener { error ->
            Log.d("ERROR","Couldn't find user")
            complete(false)
        }){
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String,String>()
                headers.put("Authorization","Bearer ${authToken}")
                return headers
            }
        }

        Volley.newRequestQueue(context).add(findUserRequest)
    }

    fun buildupJSONObject(email:String, password: String): String{
        val jsonBody = JSONObject()
        jsonBody.put("email",email)
        jsonBody.put("password",password)
        return jsonBody.toString()
    }

    fun buildUpJSONCreateUser(name: String, email: String, avatarName: String, avatarColor: String): String{
        val jsonBody = JSONObject()
        jsonBody.put("name",name)
        jsonBody.put("email",email)
        jsonBody.put("avatarName",avatarName)
        jsonBody.put("avatarColor",avatarColor)
        return jsonBody.toString()
    }

}