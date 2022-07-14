package com.shoppi.app

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.findFragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson
import org.json.JSONObject

class HomeFragment: Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        val button = view.findViewById<Button>(R.id.btn_enter_product_detail)
//        button.setOnClickListener {
//            findNavController().navigate(R.id.action_home_to_product_detail)
//            val transaction = parentFragmentManager.beginTransaction()
//            transaction.add(R.id.container_main, ProductDetailFragment())
//            transaction.commit()
//        }
        val toolbarTitle = view.findViewById<TextView>(R.id.toolbar_home_title)
        val toolbarIcon = view.findViewById<ImageView>(R.id.toolbar_home_icon)
        val viewpager = view.findViewById<ViewPager2>(R.id.viewpager_home_banner)
        val viewpagerIndicator = view.findViewById<TabLayout>(R.id.viewpager_home_banner_indicator)

        val assetLoader = AssetLoader()
        val homeJsonString = assetLoader.getJsonString(requireContext(), "home.json")
        Log.d("homeData", homeJsonString ?: "")

        if(!homeJsonString.isNullOrEmpty()) {
            val gson = Gson()
            val homeData = gson.fromJson(homeJsonString, HomeData::class.java)

//            val jsonObject = JSONObject(homeJsonString)
//            val title = jsonObject.getJSONObject("title")
//            val text = title.getString("text")
//            val iconUrl = title.getString("icon_url")
            toolbarTitle.text = homeData.title.text
            GlideApp.with(this)
                .load(homeData.title.iconUrl)
                .into(toolbarIcon)
//            val topBanners = jsonObject.getJSONArray("top_banners")
//            val size = topBanners.length()
//            for(index in 0 until size) {
//                val bannerObject = topBanners.getJSONObject(index)
//                val backgroundImageUrl = bannerObject.getString("background_image_url")
//                val badgeObj ect = bannerObject.getJSONObject("badge")
//                val badgeLabel = badgeObject.getString("label")
//                val badgeBackgroundColor = badgeObject.getString("background_color")
//                val bannerBadge = BannerBadge(badgeLabel, badgeBackgroundColor)
//                val bannerLabel = bannerObject.getString("label")
//                val productDetailObject = bannerObject.getJSONObject("product_detail")
//                val productDetailBrandName = productDetailObject.getString("brand_name")
//                val productDetailLabel = productDetailObject.getString("label")
//                val productDetailDiscountRate = productDetailObject.getInt("discount_rate")
//                val productDetailPrice = productDetailObject.getInt("price")
//                val productDetailThumbnailImageUrl = productDetailObject.getString("thumbnail_image_url")
//                val productDetailProductId = productDetailObject.getString("product_id")
//                val bannerProductDetail = ProductDetail(
//                    productDetailBrandName,
//                    productDetailLabel,
//                    productDetailDiscountRate,
//                    productDetailPrice,
//                    productDetailThumbnailImageUrl,
//                    productDetailProductId
//                )
//
//
//                val banner = Banner(
//                    backgroundImageUrl,
//                    bannerBadge,
//                    bannerLabel,
//                    bannerProductDetail
//                )
//
//
//
//            }

            viewpager.adapter = HomeBannerAdapter().apply {
                submitList(homeData.topBanners)
            }
            val pageWidth = resources.getDimension(R.dimen.viewpager_item_width)
            val pageMargin = resources.getDimension(R.dimen.viewpager_item_margin) // dp를 픽셀로 반환해준다.
            val screenWidth = resources.displayMetrics.widthPixels
            val offset = screenWidth - pageWidth - pageMargin

            viewpager.offscreenPageLimit = 3
            viewpager.setPageTransformer { page, position ->
                page.translationX = position * -offset
            }
            TabLayoutMediator(viewpagerIndicator, viewpager) { tab, position ->

            }.attach()
        }
    }
}