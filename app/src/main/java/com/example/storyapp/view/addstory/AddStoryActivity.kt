package com.example.storyapp.view.addstory

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.example.storyapp.databinding.ActivityAddStoryBinding
import com.example.storyapp.factory.StoryModelFactory
import com.example.storyapp.utils.getImageUri
import com.example.storyapp.utils.uriToFile
import com.example.storyapp.view.main.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.content.ContextCompat

class AddStoryActivity : AppCompatActivity() {
    val viewModel by viewModels<AddStoryViewModel> {
        StoryModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityAddStoryBinding
    private var currentImageUri: Uri? = null
    private var lat: Double? = null
    private var lon: Double? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.uploadButton.setOnClickListener { uploadImage() }

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun startGallery(){
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ){uri: Uri? ->
        if (uri != null){
            currentImageUri = uri
            showImage()
        } else{
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun startCamera(){
        currentImageUri = getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ){isSucess ->
        if (isSucess){
            showImage()
        }
    }

    private fun uploadImage(){
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this)
            val description = binding.editTextDescription.text.toString()

            if (binding.switchMaps.isChecked){
                if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION)){
                    fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                        if (location != null){
                            val lat = location.latitude
                            val lon = location.longitude
                            viewModel.uploadWithLocation(imageFile, description, lat, lon,)
                            status()
                        }
                    }
                }
            }
            else{
                viewModel.uploadImage(imageFile, description)
                status()
            }
        }
    }

    private fun status(){
        viewModel.status.observe(this) { isSucess ->
            if (isSucess) {
                Toast.makeText(this, "Upload Berhasil Berhasil", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                finish()
            } else {
                viewModel.error.observe(this) { errorMessage ->
                    if (errorMessage != null) {
                        Toast.makeText(this, "$errorMessage", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun showImage(){
        currentImageUri?.let {
            Log.d("Image URI", "showImage $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                    getMyLastLocation()
                }
                else -> {
                }
            }
        }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun getMyLastLocation() {
        if     (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ){
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    lat = location.latitude
                    lon = location.longitude
                } else {
                    Toast.makeText(
                        this@AddStoryActivity,
                        "Location is not found. Try Again",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }
}