package com.example.myparkingapp

import android.content.Context
import android.location.Location
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import com.example.myparkingapp.parkingarea.ParkingArea
import net.daum.mf.map.api.CalloutBalloonAdapter
import net.daum.mf.map.api.MapPOIItem
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapView

class MapUtils private constructor(val mapView : MapView, val context : Context){

    companion object{
        public fun getInstance(mapView:MapView, context: Context): MapUtils{
            return MapUtils(mapView, context)
        }
    }

    private lateinit var markerListener : MyMarkerListener
    private var callbacks : Callbacks? = null
    interface  Callbacks{
        fun onBalloonSelected(parkingArea : ParkingArea)
    }
    init {
        callbacks = context as Callbacks
        markerListener = MyMarkerListener(callbacks!!)
    }

    public fun moveToCenter(location: Location){
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(location.latitude, location.longitude),true)
    }

    public fun updateMarker(parkingAreas : ArrayList<ParkingArea>){

        for(i in parkingAreas){
            val marker = MapPOIItem();

            marker.itemName=i.parkingAreaName
            marker.userObject=i
            marker.tag=0
            marker.mapPoint=MapPoint.mapPointWithGeoCoord(i.latitude, i.longitude)

            marker.markerType=MapPOIItem.MarkerType.BluePin


            marker.setCustomImageAutoscale(true); // hdpi, xhdpi 등 안드로이드 플랫폼의 스케일을 사용할 경우 지도 라이

            marker.isMoveToCenterOnSelect=false
            marker.setCustomImageAnchor(0.5f, 1.0f)


            mapView.addPOIItem(marker)
            Log.e("items", i.toString())
        }
    }

    public fun setCustomBalloon(){
        mapView.setCalloutBalloonAdapter(CustomBalloonAdapter(context))
        mapView.setPOIItemEventListener(markerListener)
    }

}
//====================================================================================================================================
class MyMarkerListener(val callbacks : MapUtils.Callbacks) : MapView.POIItemEventListener{
    override fun onPOIItemSelected(p0: MapView?, p1: MapPOIItem?) {
    }

    override fun onCalloutBalloonOfPOIItemTouched(p0: MapView?, p1: MapPOIItem?) {
    }

    override fun onCalloutBalloonOfPOIItemTouched(
        mapView: MapView?,
        poiItem: MapPOIItem?,
        buttonType: MapPOIItem.CalloutBalloonButtonType?
    ) {
        Log.e("test!", poiItem!!.userObject.toString())
        callbacks.onBalloonSelected(poiItem!!.userObject as ParkingArea)
    }
    override fun onDraggablePOIItemMoved(p0: MapView?, p1: MapPOIItem?, p2: MapPoint?) {
    }
}
//====================================================================================================================================
class CustomBalloonAdapter(context: Context) : CalloutBalloonAdapter {

    val view = LayoutInflater.from(context).inflate(R.layout.balloon_layout, null)
    val textView = view.findViewById<TextView>(R.id.textView_balloon)

    override fun getCalloutBalloon(p0: MapPOIItem?): View {
        textView.text = p0?.itemName
        return view
    }

    override fun getPressedCalloutBalloon(p0: MapPOIItem?): View {
        return view
    }

}