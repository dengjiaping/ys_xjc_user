//package com.hemaapp.wcpc_user;
//
//import android.content.Intent;
//import android.location.Location;
//import android.os.Bundle;
//
//import com.amap.api.location.AMapLocation;
//import com.amap.api.location.AMapLocationListener;
//import com.amap.api.location.LocationManagerProxy;
//import com.amap.api.location.LocationProviderProxy;
//import com.amap.api.services.core.LatLonPoint;
//import com.amap.api.services.geocoder.GeocodeResult;
//import com.amap.api.services.geocoder.GeocodeSearch;
//import com.amap.api.services.geocoder.GeocodeSearch.OnGeocodeSearchListener;
//import com.amap.api.services.geocoder.RegeocodeQuery;
//import com.amap.api.services.geocoder.RegeocodeResult;
//
//import xtom.frame.XtomObject;
//import xtom.frame.util.XtomSharedPreferencesUtil;
//
///**
// * 定位
// */
//public class BaseLocation extends XtomObject implements AMapLocationListener,
//        OnGeocodeSearchListener {
//	private LocationManagerProxy mAMapLocationManager;
//	private AMapLocation aMapLocation;
//	private GeocodeSearch geocodeSearch;
//	private static BaseLocation location;
//
//	private BaseLocation() {
//		BaseApplication application = BaseApplication.getInstance();
//		geocodeSearch = new GeocodeSearch(application);
//		geocodeSearch.setOnGeocodeSearchListener(this);
//	}
//
//	public static BaseLocation getInstance() {
//		if (location == null)
//			location = new BaseLocation();
//		return location;
//	}
//
//	public void startLocation() {
//		if (mAMapLocationManager == null) {
//			BaseApplication application = BaseApplication.getInstance();
//			mAMapLocationManager = LocationManagerProxy
//					.getInstance(application);
//		}
//		// mAMapLocManager.setGpsEnable(false);//1.0.2版本新增方法，设置true表示混合定位中包含gps定位，false表示纯网络定位，默认是true
//		// Location API定位采用GPS和网络混合定位方式，时间最短是5000毫秒
//		mAMapLocationManager.requestLocationData(
//				LocationProviderProxy.AMapNetwork, 5000, 10, this);
//	}
//
//	public void stopLocation() {
//		if (mAMapLocationManager != null) {
//			mAMapLocationManager.removeUpdates(this);
//			mAMapLocationManager.destroy();
//		}
//		mAMapLocationManager = null;
//	}
//
//	@Override
//	public void onLocationChanged(Location location) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onStatusChanged(String provider, int status, Bundle extras) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onProviderEnabled(String provider) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onProviderDisabled(String provider) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onLocationChanged(AMapLocation location) {
//		aMapLocation = location;
//		Double lat = location.getLatitude();
//		Double lng = location.getLongitude();
//		BaseApplication application = BaseApplication.getInstance();
//		log_i("定位成功：lng=" + lng.toString() + " lat=" + lat.toString());
//		XtomSharedPreferencesUtil.save(application, "lat", lat.toString());
//		XtomSharedPreferencesUtil.save(application, "lng", lng.toString());
//		XtomSharedPreferencesUtil.save(application, "dizhi", location.getAddress());
//		XtomSharedPreferencesUtil.save(application, "city_name", location.getCity());
//		XtomSharedPreferencesUtil.save(application, "city", location.getCity());
//		XtomSharedPreferencesUtil.save(application, "district", location.getProvince()+location.getCity());
//		XtomSharedPreferencesUtil.save(application, "position", location.getProvince()+","+location.getCity()+","+location.getDistrict());
//		getAddress(lat, lng);
//		 sendBroadcast();
//		stopLocation();
//	}
//
//	 public void sendBroadcast() {
//	 BaseApplication application = BaseApplication.getInstance();
//	 String action = application.getPackageName() + ".location";
//	 log_i("action=" + action);
//	 Intent intent = new Intent();
//	 intent.setAction(action);
//	 // 发送 一个无序广播
//	 application.sendBroadcast(intent);
//	 }
//
//	// 逆地理编码
//	private void getAddress(final Double mlat, final Double mLon) {
//		LatLonPoint latLonPoint = new LatLonPoint(mlat, mLon);
//		RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,
//				GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
//		geocodeSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
//	}
//
//	/**
//	 * @return the aMapLocation
//	 */
//	public AMapLocation getaMapLocation() {
//		return aMapLocation;
//	}
//
//	@Override
//	public void onGeocodeSearched(GeocodeResult arg0, int arg1) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	public void onRegeocodeSearched(RegeocodeResult result, int arg1) {
//		if (result != null && result.getRegeocodeAddress() != null
//				&& result.getRegeocodeAddress().getFormatAddress() != null) {
//			String address = result.getRegeocodeAddress().getFormatAddress();
//			BaseApplication application = BaseApplication.getInstance();
//			log_i("定位成功：address=" + address);
//			XtomSharedPreferencesUtil.save(application, "address", address);
//		} else {
//			// nothing
//		}
//	}
//}
