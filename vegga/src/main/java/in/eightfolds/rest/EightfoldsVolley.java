package in.eightfolds.rest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.eightfolds.utils.EightfoldsUtils;
import in.eightfolds.winga.R;
import in.eightfolds.winga.interfaces.VolleyResultCallBack;
import in.eightfolds.winga.model.UserDeviceDetail;
import in.eightfolds.winga.utils.Api;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.Logg;
import in.eightfolds.winga.utils.MyDialog;
import in.eightfolds.winga.utils.Utils;
import in.eightfolds.winga.v2.model.CommonServerResponse;


/**
 * Created by Sanjay on 6/3/2017.
 */

public class EightfoldsVolley {
    public final static String APPLICATION_JSON_VALUE = "application/json";
    public final static String COOKIE = "COOKIE";
    public static final String ACCESS_TOKEN = "ACCESS_TOKEN";
    public static String TEMP_ACCESS_TOKEN;
    private static EightfoldsVolley volley;
    private static String mUsername;
    private static String mPassword;
    private Context context;
    private RequestQueue requestQueue;
    private String TAG = "DEFAULT";
    private ProgressDialog dialog;


    private EightfoldsVolley() {
    }

    // Init at application
    public void init(Context context) {
        this.context = context;
    }

    public static EightfoldsVolley getInstance() {
        mUsername = null;
        mPassword = null;
        if (volley == null) {
            volley = new EightfoldsVolley();
        }
        return volley;
    }

    public static EightfoldsVolley getInstance(String username, String password) {
        mUsername = username;
        mPassword = password;
        if (volley == null) {
            volley = new EightfoldsVolley();
        }
        return volley;
    }

    public static EightfoldsVolley getInstance(String accessToken) {
        TEMP_ACCESS_TOKEN = accessToken;
        if (volley == null) {
            volley = new EightfoldsVolley();
        }
        return volley;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request, String tag) {
        request.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequestQueue().add(request);
    }

    public <T> void addToRequestQueue(Request<T> request) {
        request.setRetryPolicy(new DefaultRetryPolicy(60 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        getRequestQueue().add(request);
    }

    public void cancelPendingRequest(String tag) {
        if (getRequestQueue() != null) {
            getRequestQueue().cancelAll(tag);
        }
    }

    public void showProgress(Context context) {
        if (context instanceof Activity && (dialog == null || !dialog.isShowing())) {
            dialog = MyDialog.showProgress(context);
        }
    }

    public void dismissProgress() {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void makingJsonRequestUsingJsonObject(final VolleyResultCallBack resultCallBack, final Class classz, int requestMethod, final String url, Object body) {

        JSONObject jsonObject = null;
        try {
            String content = Api.toJson(body);
            Logg.v(TAG, "***URL >> " + url);
            Logg.v(TAG, "***Body >> " + content);
            jsonObject = new JSONObject(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (requestMethod, url, jsonObject, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        dismissProgress();
                        if (response != null) {
                            try {
                                Logg.v(TAG, "Response >> " + response);

                                if(classz==CommonServerResponse.class){
                                    resultCallBack.onVolleyResultListener(Api.fromJson(response.toString(), CommonServerResponse.class), url);
                                }else{
                                    JSONObject object = response.getJSONObject("response");

                                    resultCallBack.onVolleyResultListener(Api.fromJson(object.getJSONObject("user").toString(), classz), url);
                                }



                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleErrorResponse(resultCallBack, error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return setHeaders();
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                getHeader(response);
                getHeaderNewAccessToken(response);
                return super.parseNetworkResponse(response);
            }
        };
        addToRequestQueue(jsObjRequest);
    }

    public void makingJsonObjectRequestForRedeemption(final VolleyResultCallBack resultCallBack, final Class classz, int requestMethod, final String url, Object body){
        JSONObject jsonObject = null;
        try {
            String content = Api.toJson(body);
            Logg.v(TAG, "***URL >> " + url);
            Logg.v(TAG, "***Body >> " + content);
            jsonObject = new JSONObject(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (requestMethod, url, jsonObject, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        dismissProgress();
                        if (response != null) {
                            try {
                                Logg.v(TAG, "Response >> " + response);
                                resultCallBack.onVolleyResultListener(Api.fromJson(response.toString(), CommonServerResponse.class), url);




                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleErrorResponse(resultCallBack, error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return setHeaders();
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                getHeader(response);
                getHeaderNewAccessToken(response);
                return super.parseNetworkResponse(response);
            }
        };
        addToRequestQueue(jsObjRequest);
    }

    public void makingJsonRequest(final VolleyResultCallBack resultCallBack, final Class classz, int requestMethod, final String url, Object body) {
        JSONObject jsonObject = null;
        try {
            String content = Api.toJson(body);
            Logg.v(TAG, "***URL >> " + url);
            Logg.v(TAG, "***Body >> " + content);
            jsonObject = new JSONObject(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (requestMethod, url, jsonObject, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        dismissProgress();
                        if (response != null) {
                            try {
                                Logg.v(TAG, "Response >> " + response);
                                resultCallBack.onVolleyResultListener(Api.fromJson(response.toString(), classz), url);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleErrorResponse(resultCallBack, error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return setHeaders();
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                getHeader(response);
                return super.parseNetworkResponse(response);
            }
        };
        addToRequestQueue(jsObjRequest);
    }

    public void makingUserDeviceJsonRequest(final VolleyResultCallBack resultCallBack, final Class classz, int requestMethod, final String url, Object body) {
        JSONObject jsonObject = null;
        try {
            String content = Api.toJson(body);
            Logg.v(TAG, "***URL >> " + url);
            Logg.v(TAG, "***Body >> " + content);
            jsonObject = new JSONObject(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (requestMethod, url, jsonObject, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        dismissProgress();
                        if (response != null) {
                            try {
                                Logg.v(TAG, "Response >> " + response);
                                resultCallBack.onVolleyResultListener(Api.fromJson(response.toString(), CommonServerResponse.class), url);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleErrorResponse(resultCallBack, error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return setHeaders();
            }

            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                getHeader(response);
                return super.parseNetworkResponse(response);
            }
        };
        addToRequestQueue(jsObjRequest);
    }


    private void handleErrorResponse(VolleyResultCallBack resultCallBack, VolleyError volleyError) {
        dismissProgress();

        if (volleyError.networkResponse != null && volleyError.networkResponse.data != null) {
            Logg.v(TAG, "***Error Response >> " + new String(volleyError.networkResponse.data));
            if (HttpStatus.valueOf(volleyError.networkResponse.statusCode).equals(HttpStatus.UNAUTHORIZED)) {
                String errorResponse = new String(volleyError.networkResponse.data);
                try {
                    CommonResponse commonResponse = (CommonResponse) Api.fromJson(errorResponse, CommonResponse.class);
                    //resultCallBack.onVolleyErrorListener(commonResponse);

                    if (commonResponse.getCode() == Constants.DEACTIVATED_CODE_FROM_BACKEND || commonResponse.getCode() == Constants.DEACTIVATED_CODE || commonResponse.getCode() == Constants.MOBILE_NOT_REGISTERED) {
                        resultCallBack.onVolleyErrorListener(commonResponse);
                    } else {
                        resultCallBack.onVolleyErrorListener(context.getString(R.string.not_a_valid_user));

                    }
                } catch (Exception e) {
                    if (resultCallBack != null) {
                        resultCallBack.onVolleyErrorListener(context.getString(R.string.not_a_valid_user));

                    }
                    e.printStackTrace();
                }
                Utils.goForLogIn(context);


            } else {
                String errorResponse = new String(volleyError.networkResponse.data);
                Logg.v(TAG, "***Error Response >> " + errorResponse);

                try {
                    CommonResponse commonResponse = (CommonResponse) Api.fromJson(errorResponse, CommonResponse.class);
                    resultCallBack.onVolleyErrorListener(commonResponse);
                } catch (Exception e) {
                    if (resultCallBack != null) {
                        if (!TextUtils.isEmpty(volleyError.getMessage()))
                            Logg.v(TAG, "Volley error>  " + volleyError.getMessage());
                        resultCallBack.onVolleyErrorListener(context.getResources().getString(R.string.unable_to_connect_try_again));
                        //resultCallBack.onVolleyErrorListener(TextUtils.isEmpty(volleyError.getMessage()) ? context.getResources().getString(R.string.unable_to_connect_try_again) : volleyError.getMessage());
                    }
                    e.printStackTrace();
                }
            }
        } else {
            if (resultCallBack != null) {
                if (!TextUtils.isEmpty(volleyError.getMessage()))
                    Logg.v(TAG, "Volley error>  " + volleyError.getMessage());
                resultCallBack.onVolleyErrorListener(context.getResources().getString(R.string.unable_to_connect_try_again));
                //resultCallBack.onVolleyErrorListener(TextUtils.isEmpty(volleyError.getMessage()) ? context.getResources().getString(R.string.unable_to_connect_try_again) : volleyError.getMessage());
            }
        }
    }

    public void makingJsonArrayRequest(VolleyResultCallBack resultCallBack, Class classz, int requestMethod, String url) {
        makingJsonArrayRequest(resultCallBack, classz, requestMethod, url, null);
    }

    public void makingJsonArrayRequest(final VolleyResultCallBack resultCallBack, final Class classz, int requestMethod, final String url, Object body) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(Api.toJson(body));
        } catch (Exception e) {
            e.printStackTrace();
        }
        JsonArrayRequest jsObjRequest = new JsonArrayRequest
                (requestMethod, url, jsonArray, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        dismissProgress();
                        if (response != null) {
                            try {
                                Object[] objects = (Object[]) Api.fromJson(response.toString(), classz);
                                List<Object> list = new ArrayList<>();
                                for (Object object : objects) {
                                    list.add(object);
                                }

                                resultCallBack.onVolleyResultListener(list, url);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleErrorResponse(resultCallBack, error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return setHeaders();
            }

            @Override
            protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                getHeader(response);
                return super.parseNetworkResponse(response);
            }
        };
        addToRequestQueue(jsObjRequest);
    }

    public void makingStringRequestWithBody(final String requestBody, final VolleyResultCallBack resultCallBack, final Class classz, int requestMethod, final String url) {
        Logg.v(TAG, "***Request>> " + url);

        StringRequest jsObjRequest = new StringRequest
                (requestMethod, url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        dismissProgress();
                        if (response != null) {
                            Logg.v(TAG, "***Response>> " + response);
                            try {

                                resultCallBack.onVolleyResultListener(Api.fromJson(response, classz), url);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleErrorResponse(resultCallBack, error);
                    }
                }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    Logg.v(TAG, String.format("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8"));
                    return null;
                }
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return setHeaders();
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
//                if (params != null) {
//                    return params;
//                }
                return super.getParams();
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                getHeader(response);
                return super.parseNetworkResponse(response);
            }
        };
        addToRequestQueue(jsObjRequest);
    }

    public void makingStringRequest(final Map<String, String> params, final VolleyResultCallBack resultCallBack, final Class classz, int requestMethod, final String url) {
        Logg.v(TAG, "***Request>> " + url);

        StringRequest jsObjRequest = new StringRequest
                (requestMethod, url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        dismissProgress();
                        if (response != null) {
                            Logg.v(TAG, "***Response>> " + response);
                            try {
                                if (classz == String.class) {
                                    resultCallBack.onVolleyResultListener(response, url);
                                } else {
                                    resultCallBack.onVolleyResultListener(Api.fromJson(response, classz), url);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleErrorResponse(resultCallBack, error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return setHeaders();
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if (params != null) {
                    return params;
                }
                return super.getParams();
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                getHeader(response);
                return super.parseNetworkResponse(response);
            }
        };
        addToRequestQueue(jsObjRequest);
    }

    public void makingStringRequestForCommonResponse(final VolleyResultCallBack resultCallBack, final Class classz, int requestMethod, final String url) {
        makingStringRequestForCommonResponse(null, resultCallBack, classz, requestMethod, url);
    }


    public void makingStringRequestForCommonResponse(final Map<String, String> params, final VolleyResultCallBack resultCallBack, final Class classz, int requestMethod, final String url) {
        Logg.v(TAG, "***Request>> " + url);

        StringRequest jsObjRequest = new StringRequest
                (requestMethod, url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        dismissProgress();
                        if (response != null) {
                            Logg.v(TAG, "***Response>> " + response);
                            try {
                                if (classz == String.class) {
                                    resultCallBack.onVolleyResultListener(response, url);
                                } else {

                                    JSONObject object = new JSONObject(response);
                                    CommonServerResponse commonResponse=(CommonServerResponse)Api.fromJson(object.toString(),CommonServerResponse.class);
                                    if(commonResponse.isStatus() && commonResponse.getResponse()!=null){
                                        resultCallBack.onVolleyResultListener(Api.fromJson(object.getJSONObject("response").toString(), classz), url);
                                    }else{
                                        resultCallBack.onVolleyResultListener(Api.fromJson(object.toString(),CommonServerResponse.class), url);
                                    }

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleErrorResponse(resultCallBack, error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return setHeaders();
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if (params != null) {
                    return params;
                }
                return super.getParams();
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                getHeader(response);
                getHeaderNewAccessToken(response);
                return super.parseNetworkResponse(response);
            }
        };
        addToRequestQueue(jsObjRequest);
    }


    public void makingStringRequestForImage(final VolleyResultCallBack resultCallBack, final Class classz, int requestMethod, final String url) {
        makingStringRequestForImage(null, resultCallBack, classz, requestMethod, url);
    }

    public void makingStringRequestForImage(final Map<String, String> params, final VolleyResultCallBack resultCallBack, final Class classz, int requestMethod, final String url) {
        Logg.v(TAG, "***Request>> " + url);
        final String[] imageUrl = {""};
        StringRequest jsObjRequest = new StringRequest
                (requestMethod, url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        dismissProgress();
                        if (response != null) {
                            Logg.v(TAG, "***Response>> " + response);
                            try {
                                if (classz == String.class) {

                                    if (!imageUrl[0].isEmpty()) {
                                        resultCallBack.onVolleyResultListener(imageUrl[0], url);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error.networkResponse.statusCode==301){
                            dismissProgress();


                                resultCallBack.onVolleyResultListener( getImageUrl(error.networkResponse), url);

                        }else{
                            handleErrorResponse(resultCallBack, error);
                        }

                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return setHeaders();
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                if (params != null) {
                    return params;
                }
                return super.getParams();
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {

                getHeaderNewAccessToken(response);
                imageUrl[0] = getImageUrl(response);
                return super.parseNetworkResponse(response);
            }
        };
        addToRequestQueue(jsObjRequest);
    }

    private String getImageUrl(NetworkResponse response) {

        JSONObject headers = new JSONObject(response.headers);
        String token = "";
        if (headers != null && headers.has("Location")) {
            try {
                token = headers.getString("Location");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return token;
    }


    public void makingStringRequest(final VolleyResultCallBack resultCallBack, final Class classz, int requestMethod, final String url) {
        makingStringRequest(null, resultCallBack, classz, requestMethod, url);
    }


    private Map<String, String> setHeaders() {
        String cookie = EightfoldsUtils.getInstance().getFromSharedPreference(context, COOKIE);
        String accessToken = EightfoldsUtils.getInstance().getFromSharedPreference(context, ACCESS_TOKEN);
        HashMap<String, String> headers = new HashMap<String, String>();
        //headers.put("Content-Type", APPLICATION_JSON_VALUE);
        headers.put("Accept", APPLICATION_JSON_VALUE);
        if (!TextUtils.isEmpty(accessToken)) {
            Logg.v(TAG, "accessToken>> " + accessToken);
            headers.put("Authorization", "Bearer " + accessToken);
        } else if (!TextUtils.isEmpty(TEMP_ACCESS_TOKEN)) {
            headers.put("Authorization", "Bearer " + TEMP_ACCESS_TOKEN);
        }
        if (mUsername != null && mPassword != null) {
            headers.put("Authorization", ("Basic " + Base64.encodeToString((mUsername + ":" + mPassword).getBytes(), Base64.NO_WRAP)));
        }
        if (!TextUtils.isEmpty(cookie)) {
            headers.put("Cookie", cookie);
        }
        return headers;
    }


    private void getHeaderNewAccessToken(NetworkResponse response) {

        if (response.headers != null && response.headers.containsKey("Jwt-Token")) {

            String token = response.headers.get("Jwt-Token");
            EightfoldsUtils.getInstance().saveToSharedPreference(context, ACCESS_TOKEN, token);

        }
    }

    private void getHeader(NetworkResponse response) {
        JSONObject headers = new JSONObject(response.headers);
        if (headers != null && headers.has("set-cookie")) {
            try {
                String cookie = headers.getString("set-cookie");
                EightfoldsUtils.getInstance().saveToSharedPreference(context, COOKIE, cookie);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



     /*public void makingMultipartRequest(final VolleyResultCallBack resultCallBack, final Class classz, int requestMethod, String url) {
        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST, url, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                String resultResponse = new String(response.data);
                try {
                    JSONObject result = new JSONObject(resultResponse);
                    String status = result.getString("status");
                    String message = result.getString("message");

                    if (status.equals(Constant.REQUEST_SUCCESS)) {
                        // tell everybody you have succed upload image and post strings
                        Logg.i("Messsage", message);
                    } else {
                        Logg.i("Unexpected", message);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                String errorMessage = "Unknown error";
                if (networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        errorMessage = "Request timeout";
                    } else if (error.getClass().equals(NoConnectionError.class)) {
                        errorMessage = "Failed to connect server";
                    }
                } else {
                    String result = new String(networkResponse.data);
                    try {
                        JSONObject response = new JSONObject(result);
                        String status = response.getString("status");
                        String message = response.getString("message");

                        Logg.e("Error Status", status);
                        Logg.e("Error Message", message);

                        if (networkResponse.statusCode == 404) {
                            errorMessage = "Resource not found";
                        } else if (networkResponse.statusCode == 401) {
                            errorMessage = message + " Please login again";
                        } else if (networkResponse.statusCode == 400) {
                            errorMessage = message + " Check your inputs";
                        } else if (networkResponse.statusCode == 500) {
                            errorMessage = message + " Something is getting wrong";
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                Logg.i("Error", errorMessage);
                error.printStackTrace();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("api_token", "gh659gjhvdyudo973823tt9gvjf7i6ric75r76");
                params.put("name", mNameInput.getText().toString());
                params.put("location", mLocationInput.getText().toString());
                params.put("about", mAvatarInput.getText().toString());
                params.put("contact", mContactInput.getText().toString());
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                // file name could found file base or direct access from real path
                // for now just get bitmap data from ImageView
                params.put("avatar", new DataPart("file_avatar.jpg", AppHelper.getFileDataFromDrawable(getBaseContext(), mAvatarImage.getDrawable()), "image/jpeg"));
                params.put("cover", new DataPart("file_cover.jpg", AppHelper.getFileDataFromDrawable(getBaseContext(), mCoverImage.getDrawable()), "image/jpeg"));

                return params;
            }
        };

        addToRequestQueue(multipartRequest);
    }*/

}
