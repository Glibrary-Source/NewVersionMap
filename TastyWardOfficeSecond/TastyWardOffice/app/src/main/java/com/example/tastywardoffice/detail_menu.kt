package com.example.tastywardoffice


import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.example.tastywardoffice.databinding.FragmentDetailMenuBinding
import com.example.tastywardoffice.datamodel.LocationItems
import com.example.tastywardoffice.overview.OverviewViewModel
import com.google.android.gms.maps.model.LatLng
import java.util.*

class detail_menu : Fragment() {

    val StoreData by navArgs<detail_menuArgs>()
    private val TAG = "detailFG"

    lateinit var mContext: Context

    private lateinit var overViewModel: OverviewViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is MainActivity) {
            mContext = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentDetailMenuBinding.inflate(inflater)

        overViewModel = ViewModelProvider(requireActivity()).get(OverviewViewModel::class.java)

        //detailItemData() 함수를 통해 뷰모델어서 뽑아온 스토어 데이터를 변수에 저장
        val storeDetailData = detailItemData()

        //각각의 뷰에 맞게 데이터를 입력시킨다
        bindImage(binding.foodImage, storeDetailData.storeMenuPictureUrls.menu[0])
        binding.foodImage.clipToOutline = true
        binding.storeName.text = StoreData.storename
        binding.locationText.text = locationAddress()[0].getAddressLine(0)

        binding.firstMenuButton.isChecked = true

        //네비게이션 프래그먼트 데이터 전달을 위해 번들 사용
        arguments = Bundle()

        //뷰를 처음 불러올때 메뉴부터 불러오기
        parentFragmentManager.beginTransaction().replace(
            R.id.nav_bar,
            menu_image().apply {
                arguments = Bundle().apply {
                    putString("Url", storeDetailData.storeMenuPictureUrls.menu[0])
                }
            }
        ).commit()

        //메뉴 네비게이션바 클릭시 화면 전환
        binding.menuButton.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(
                R.id.nav_bar,
                menu_image().apply {
                    arguments = Bundle().apply {
                        putString("Url", storeDetailData.storeMenuPictureUrls.menu[0])
                    }
                }
            ).commit()
        }

        //지도 네비게이션바 클릭시 화면 전환
        binding.mapButton.setOnClickListener {
            parentFragmentManager.beginTransaction().replace(
                R.id.nav_bar,
                detail_googleMap().apply {
                    arguments = Bundle().apply {
                        putParcelable("LatLng", LatLng(storeDetailData.storeGEOPoints.latitude,storeDetailData.storeGEOPoints.longitude))
                        putString("storeName", storeDetailData.storeId)
                    }
                }
            ).commit()
        }

        return binding.root
    }

    //뷰모델서버에서 불러온 스토어 데이터중 독태그와 일치하는 스토어 데이터만 뽑아와서 리턴해줌
    private fun detailItemData() : LocationItems{
            for(storedata in overViewModel.distanceStoreData.value!!.Howlong){
                if(storedata.docId == StoreData.dogId) {
                    return storedata
                }
            }
            return overViewModel.distanceStoreData.value!!.Howlong[0]
    }

    //주소 텍스트를 위한 코드
    private fun locationAddress(): List<Address> {
        val geocoder = Geocoder(mContext, Locale.KOREA)
        val address = geocoder.getFromLocation(StoreData.latlng.latitude,StoreData.latlng.longitude, 1)
        return address
    }


}