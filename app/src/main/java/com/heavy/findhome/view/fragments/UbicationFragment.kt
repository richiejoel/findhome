package com.heavy.findhome.view.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.PorterDuff
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
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


class UbicationFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMyLocationClickListener {

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
    }

    private fun mCreateMarker(){
        //40.780771, -73.979550
        val coordinates = LatLng(40.785950, -73.975869)
        val marker = MarkerOptions().position(coordinates).title("Crash!!")
            .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView()))

        val coordinates2 = LatLng(40.780771, -73.979550)
        val marker2 = MarkerOptions().position(coordinates2).title("Crash!!")
            .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView()))

        val coordinates3 = LatLng(40.797863, -73.967080)
        val marker3 = MarkerOptions().position(coordinates3).title("Crash!!")
            .icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView()))

        map.addMarker(marker)
        map.addMarker(marker2)
        map.addMarker(marker3)
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 14f), 2000, null)
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates2, 14f), 800, null)
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates2, 14f), 1000, null)
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
    }
}