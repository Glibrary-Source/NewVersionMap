package com.example.tastywardoffice.overview

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tastywardoffice.network.RequestType
import com.example.tastywardoffice.network.TastyWardApi
import com.example.tastywardoffice.network.WholeData
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Response

class MainViewModel(): ViewModel() {
    private val TAG = "ViewModelTest"

    private var _height = MutableLiveData<Int>()
    private var _wholeData = MutableLiveData<WholeData>()

    val height: LiveData<Int>
    get() = _height

    val wholeData: LiveData<WholeData>
    get() = _wholeData

    init {
        _height.value = 170
//        totalShopData()
    }


    //api 데이터를 뷰모델에 받아오기
//    fun totalShopData() {
//        viewModelScope.launch{
//            try{
//                val tempData = RequestType("whole_stores")
//                TastyWardApi.service.getWholeData(tempData)
//                    .enqueue(object : retrofit2.Callback<WholeData> {
//                        override fun onResponse(
//                            call: Call<WholeData>,
//                            response: Response<WholeData>
//                        ) {
//                            if (response.isSuccessful) {
//                                _wholeData.value = response.body()
//                            } else {
//                                val result = response.body()
//                                Log.d("YMC", "onResponse 실패 " + result?.toString())
//                            }
//                        }
//
//                        override fun onFailure(call: Call<WholeData>, t: Throwable) {
//                            Log.d("YMC", "onFailure 에러 " + t.message.toString())
//                        }
//                    })
//            } catch (e: Exception) {
//                Log.d("bugTest", e.message.toString())
//            }
//        }
//    }
}