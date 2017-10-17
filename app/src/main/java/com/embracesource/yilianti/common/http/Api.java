package com.embracesource.yilianti.common.http;

import android.util.Log;

import com.embracesource.yilianti.bean.ApplyDiagnosisGoalBean;
import com.embracesource.yilianti.bean.ApplyDiagnosisRequestBean;
import com.embracesource.yilianti.bean.BaseBean;
import com.embracesource.yilianti.bean.LoginBean;
import com.embracesource.yilianti.bean.MyLaunchListBean;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/10/16 0016.
 * <p>
 * .map(new Function<Object, LoginBean>() {
 *
 * @Override public LoginBean apply(@NonNull Object jsonObject) throws Exception {
 * Log.i(TAG, String.valueOf(new JSONObject(jsonObject.toString())));
 * return null;
 * }
 * }
 */

public class Api implements ApiInterface {
    @Inject
    public Api() {
    }

    @Override
    public Observable<LoginBean> login(String userName, String pwd) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("username", userName);
            jsonObject.put("pwd", pwd);
//            jsonObject.put("username", "zq");
//            jsonObject.put("pwd", "123456");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toString());//传递json需要加上这一句
        return RetrofitConfig.getInstance().login(body)
                .subscribeOn(Schedulers.io());
    }


    @Override
    public Observable<MyLaunchListBean> getMyLaunchList(int pageNum,int pageSize) {
        return RetrofitConfig.getInstance_afterLogin().getMyLaunchList(pageNum,pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())/*.map(new Function() {
                    @Override
                    public Object apply(@NonNull Object o) throws Exception {
                        return o;
                    }
                })*/;
    }

    @Override
    public Observable<Response<JSONObject>> getApplyDiagnosisDetail(String id, String flag) {//ApplyDiagnosisDetailBean
        return RetrofitConfig.getInstance_afterLogin().getApplyDiagnosisDetail(id, flag)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).map(new Function<Response<JSONObject>, Response<JSONObject>>() {
                    @Override
                    public Response<JSONObject> apply(@NonNull Response<JSONObject> applyDiagnosisDetailBean) throws Exception {
                        applyDiagnosisDetailBean.toString();
                        return applyDiagnosisDetailBean;
                    }
                });
    }

    @Override
    public Observable<BaseBean> submitApplyDiagnosis(ApplyDiagnosisRequestBean bean) {
        String gsonStr = new Gson().toJson(bean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), gsonStr);//传递json需要加上这一句
        return RetrofitConfig.getInstance_afterLogin().submitApplyDiagnosis(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<ApplyDiagnosisGoalBean> getBaseData(String code) {
        return RetrofitConfig.getInstance().getBaseData(code)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).map(new Function<Response<ApplyDiagnosisGoalBean>, ApplyDiagnosisGoalBean>() {
                    @Override
                    public ApplyDiagnosisGoalBean apply(@NonNull Response<ApplyDiagnosisGoalBean> objectResponse) throws Exception {
                        Log.i("sss", objectResponse.toString());
                        return objectResponse.body();
                    }
                });
    }



}
