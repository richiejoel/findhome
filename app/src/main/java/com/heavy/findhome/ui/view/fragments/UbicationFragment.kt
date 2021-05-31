package com.heavy.findhome.ui.view.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.location.Location
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.heavy.findhome.R
import com.heavy.findhome.databinding.FragmentUbicationBinding
import com.heavy.findhome.utils.DataParser
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class UbicationFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener, GoogleMap.OnMapClickListener, GoogleMap.OnMarkerDragListener, GoogleMap.OnMapLongClickListener {

    private var _binding: FragmentUbicationBinding? = null
    private val binding get() = _binding!!

    private lateinit var map:GoogleMap

    companion object {
        const val REQUEST_CODE_LOCATION = 100
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUbicationBinding.inflate(inflater, container, false)
        val view = binding.root
        mCreateMapFragment()
        return view
    }

    private fun mCreateMapFragment(){
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)

    }

    override fun onMapReady(googleMap: GoogleMap?) {
        map = googleMap!!
        try {
            val success = map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    requireContext(), R.raw.mapstyles
                )
            )
            if (!success) {
                Log.e("INFO", "Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e("INFO", "Can't find style. Error: ", e)
        }
        mCreateMarker()
        map.setOnMyLocationButtonClickListener(this)
        map.setOnMyLocationClickListener(this)
        mEnableLocation()

        map.setOnMapClickListener(this)
        map.setOnMarkerDragListener(this)
        map.setOnMapLongClickListener(this)


    }


    private fun mCreateMarker(){
        //40.780771, -73.979550
        val coordinates = LatLng(40.785950, -73.975869)
        val marker = MarkerOptions().position(coordinates).title("Crash!!")
            .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView()))

        val coordinates2 = LatLng(40.780771, -73.979550)
        val marker2 = MarkerOptions().position(coordinates2).title("Crash!!")
            .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView()))

        val coordinates3 = LatLng(37.401762, -122.085803)
        val marker3 = MarkerOptions().position(coordinates3).title("Crash!!")
            .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView()))
            .draggable(true)

        map.addMarker(marker)
        map.addMarker(marker2)
        map.addMarker(marker3)
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 14f), 2000, null)
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates2, 14f), 800, null)
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates2, 14f), 1000, null)
    }

    private fun createPolylines(origin: LatLng, dest: LatLng){
        val polylineOptions = PolylineOptions()
            .add(origin)
            .add(dest)
            .width(15f)
            .color(ContextCompat.getColor(requireContext(), R.color.color_map_line))

        val polyline: Polyline = map?.addPolyline(polylineOptions)
        polyline.startCap = RoundCap()
        polyline.endCap = RoundCap()

        /*val pattern: List<PatternItem> listOf(
            Dot(),Gap(10f),Dash(50f),Gap(10f)
        )

        polyline.pattern(pattern)*/
    }

    private fun getMarkerBitmapFromView(): Bitmap? {
        val customMarkerView: View? = layoutInflater.inflate(R.layout.custom_marker_layout, null)
//        val markerImageView: ImageView =
//            customMarkerView.findViewById<View>(R.id.profile_image) as ImageView
        customMarkerView?.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED );
        customMarkerView?.layout(0, 0, customMarkerView.measuredWidth, customMarkerView.measuredHeight);
        customMarkerView?.buildDrawingCache();
        val returnedBitmap = Bitmap.createBitmap(
            customMarkerView!!.measuredWidth, customMarkerView.measuredHeight,
            Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(returnedBitmap)
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN)
        val drawable = customMarkerView.background

        drawable?.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;

    }

    private fun mIsLocationPermissionGranted() = ContextCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED


    @SuppressLint("MissingPermission")
    private fun mEnableLocation(){
        if(!::map.isInitialized) return
        if(mIsLocationPermissionGranted()){
            map!!.isMyLocationEnabled = true
            //map!!.uiSettings.isMyLocationButtonEnabled = true
        } else {
            mRequestLocationPermission()
        }
    }

    private fun mRequestLocationPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )){
            Toast.makeText(
                requireContext(),
                "Ve a ajustes y acepta los permisos",
                Toast.LENGTH_LONG
            ).show()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_CODE_LOCATION
            )
        }
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            REQUEST_CODE_LOCATION -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                map!!.isMyLocationEnabled = true
                //map!!.uiSettings.isMyLocationButtonEnabled = true
            } else {
                Toast.makeText(
                    requireContext(),
                    "Para activar la localizacion Ve a ajustes y acepta los permisos",
                    Toast.LENGTH_LONG
                ).show()
            }
            else -> {Log.e("ERROR", "Error not controlled")}
        }
    }

    @SuppressLint("MissingPermission")
    override fun onResume() {
        super.onResume()
        if(!::map.isInitialized) return
        if(!mIsLocationPermissionGranted()){
            map.isMyLocationEnabled = false
            //map!!.uiSettings.isMyLocationButtonEnabled = false
            Toast.makeText(
                requireContext(),
                "Para activar la localizacion Ve a ajustes y acepta los permisos",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(requireContext(), "Boton pulsado", Toast.LENGTH_SHORT).show()
        return false
    }

    override fun onMyLocationClick(loc: Location) {
        Toast.makeText(
            requireContext(),
            "Estas en ${loc.latitude} ${loc.longitude}",
            Toast.LENGTH_SHORT
        ).show()

        //Get position currently
        val origin = LatLng(loc.latitude,loc.longitude)
        val dest = LatLng(37.401762, -122.085803)

        getDirectionsURL(origin, dest)
        createPolylines(origin, dest)

        /*val url: String = getDirectionsURL(origin, dest)!!
        val downloadTask: DownloadTask = DownloadTask()
        // Start downloading json data from Google Directions API
        downloadTask.execute(url)*/

    }

    private fun getDirectionsURL(origin:LatLng, dest:LatLng): String? {

        //Origin of route
        val strOrigin = "origin="+ origin.latitude +","+ origin.longitude

        //Destination of route
        val strDest = "destination="+ dest.latitude +","+ dest.longitude;

        //setting transportation mode
        val mode = "sensor=false"

        //Building parameters to the API Directions
        val parameters = "$strOrigin&$strDest&$mode"

        //Output format
        val output = "json"
        //val key = "AIzaSyCIMIVNvWtIaMBDqPySBjsV-q4h4YwSOjA";
        val key = "AIzaSyCASsBISqXNR4imAynxnIw8Fa68Cv1c8cY";

        //Building the url for API Directions
        val a = "https://maps.googleapis.com/maps/api/directions/$output?$parameters&key=$key"
        Log.i("prueba",a)
        return "https://maps.googleapis.com/maps/api/directions/$output?$parameters&key=$key"
    }



    inner class DownloadTask: AsyncTask<String?, Void?, String>(){

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            val parserTask= ParserTask()
            parserTask.execute(result)
        }

        override fun doInBackground(vararg url: String?): String {
            var data = ""
            try {
                data = downloadUrl(url[0].toString()).toString()
            } catch (e: java.lang.Exception){
                Log.d("Background Task", e.toString())
            }

            return data
        }

    }

    @Throws(IOException::class)
    private fun downloadUrl(strUrl: String): String? {
        var data = ""
        var iStream: InputStream? = null
        var urlConnection: HttpURLConnection? = null

        try {
            val url = URL(strUrl)
            urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.connect()
            iStream = urlConnection!!.inputStream
            val br = BufferedReader(InputStreamReader(iStream))
            val sb = StringBuffer()
            var line:String? = ""
            while (br.readLine().also { line = it } != null){
                sb.append(line)
            }
            data = sb.toString()
            br.close()

        } catch (e: java.lang.Exception){
            Log.d("Download Url ", e.toString())
        } finally {
            iStream!!.close()
            urlConnection!!.disconnect()
        }

        return data
    }

    inner class ParserTask: AsyncTask<String?, Int?, List<List<HashMap<String,String>>>?> (){

        override fun doInBackground(vararg jsonData: String?): List<List<HashMap<String, String>>>? {
            val jsonObject: JSONObject
            var routes: List<List<HashMap<String, String>>>? = null
            try {
                jsonObject = JSONObject(jsonData[0])
                val parser = DataParser()
                routes = parser.parse(jsonObject)
            } catch (e: java.lang.Exception){
                e.printStackTrace()
            }

            return routes
        }

        override fun onPostExecute(result: List<List<HashMap<String, String>>>?) {
            super.onPostExecute(result)
            val points = ArrayList<LatLng?>()
            val lineOptions = PolylineOptions()

            for(i in result!!.indices){
                val path = result[i]
                for(j in path!!.indices){
                    val point = path[j]
                    val lat = point["lat"]!!.toDouble()
                    val lng = point["lng"]!!.toDouble()
                    val position = LatLng(lat, lng)
                    points.add(position)
                }
                lineOptions.addAll(points)
                lineOptions.width(8f)
                lineOptions.color(Color.RED)
                lineOptions.geodesic(true)
            }

            //Drawing polyline in google map
            if(points.size != 0) map!!.addPolyline(lineOptions)
        }

    }

    override fun onMapClick(position: LatLng?) {
        val markerAdd = MarkerOptions()?.position(position!!).draggable(true).title("Crash!!")
            .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView()))
            .draggable(true)

        map?.addMarker(markerAdd)
    }

    override fun onMarkerDragStart(p0: Marker?) {

    }

    override fun onMarkerDrag(p0: Marker?) {


    }

    override fun onMarkerDragEnd(marker: Marker?) {
        //map?.animateCamera(CameraUpdateFactory.newLatLng(marker?.getPosition()));
    }

    override fun onMapLongClick(p0: LatLng?) {

    }
}