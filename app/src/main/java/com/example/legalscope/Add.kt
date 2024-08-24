package com.example.legalscope

import android.Manifest
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.legalscope.databinding.ActivityAddBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.File
import java.io.FileOutputStream
import java.util.Locale

class Add : AppCompatActivity() {

    private lateinit var binding: ActivityAddBinding
    private lateinit var db: PemilihDBHelper
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    companion object {
        private const val IMAGE_REQUEST_CODE = 1000
        private const val CAMERA_REQUEST_CODE = 1001
    }

    private var imageUri: Uri? = null // Nullable Uri to handle both image selection and capture

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
        db = PemilihDBHelper(this)

        var selectedGender = ""

        // Check radio button
        binding.jenisKelamin.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId != -1) {
                val radio: RadioButton = binding.jenisKelamin.findViewById(checkedId)
                selectedGender = radio.text.toString()
                Toast.makeText(
                    applicationContext,
                    "Selected gender: $selectedGender",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // Handle image selection from gallery
        binding.selectImg.setOnClickListener {
            pickImageFromGallery()
        }

        // Handle image capture from camera
        binding.captureImg.setOnClickListener {
            checkCameraPermission()
        }

        // Save button click
        binding.saveButton.setOnClickListener {
            val nik = binding.nik.text.toString()
            val nama = binding.nama.text.toString()
            val noHp = binding.noHp.text.toString()
            val jenisKelamin = selectedGender
            val alamat = binding.alamat.text.toString()
            val gambar = imageUri?.toString() // Save the URI as a String

            // Retrieve the date from the DatePicker
            val datePicker = binding.tanggalPicker
            val day = datePicker.dayOfMonth
            val month = datePicker.month + 1 // Months are zero-based
            val year = datePicker.year
            val tanggal = "$day/$month/$year"

            if (selectedGender.isEmpty()) {
                Toast.makeText(this, "Please select a gender", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Convert the gambar String back to Uri for the Pemilih constructor
            val imgUri = Uri.parse(gambar)

            val pemilih = Pemilih(0, nik, nama, noHp, jenisKelamin, tanggal, alamat, imgUri)
            db.addPemilih(pemilih)
            finish()
            Toast.makeText(this, "Successfully saved new data pemilih", Toast.LENGTH_SHORT).show()
        }



        binding.checkLocationButton.setOnClickListener {
            getLocation()

        }

    }


    // Function to pick an image from the gallery
    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    // Function to capture an image using the camera
    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                CAMERA_REQUEST_CODE
            )
        } else {
            captureImage()
        }
    }

    private fun captureImage() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, CAMERA_REQUEST_CODE)
        } else {
            Toast.makeText(this, "No application available to capture images", Toast.LENGTH_SHORT)
                .show()
        }
    }

    // Handle result from gallery or camera
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                IMAGE_REQUEST_CODE -> {
                    imageUri = data?.data
                    binding.imagePreview.setImageURI(imageUri)
                }

                CAMERA_REQUEST_CODE -> {
                    val imageBitmap = data?.extras?.get("data") as? Bitmap
                    binding.imagePreview.setImageBitmap(imageBitmap)
                    imageUri = saveImageToExternalStorage(imageBitmap)
                }
            }
        }
    }

    // Save bitmap to external storage and return its Uri
    private fun saveImageToExternalStorage(bitmap: Bitmap?): Uri {
        val imageName = "IMG_${System.currentTimeMillis()}.jpg"
        val imageFile = File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), imageName)
        FileOutputStream(imageFile).use {
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
        return Uri.fromFile(imageFile)
    }

    // Handle permission request result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                captureImage()
            } else {
                if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                    showPermissionDeniedDialog()
                } else {
                    Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showPermissionDeniedDialog() {
        AlertDialog.Builder(this)
            .setTitle("Permission Required")
            .setMessage("Camera permission is required to capture images. Please enable it in the app settings.")
            .setPositiveButton("Go to Settings") { _, _ ->
                val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", packageName, null)
                intent.data = uri
                startActivity(intent)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun getLocation() {
        //permission to access location
        if (ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                ACCESS_COARSE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(ACCESS_FINE_LOCATION), 100)
            return
        }
        // Create a location request
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        val loc = fusedLocationProviderClient.lastLocation
        loc.addOnSuccessListener { location ->
            if (location != null) {
                val geocoder = Geocoder(this, Locale.getDefault())
                val addresses: List<Address>? = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                if (addresses != null && addresses.isNotEmpty()) {
                    val address = addresses[0].getAddressLine(0) // Get the full address
                    binding.alamat.setText(address)
                } else {
                    binding.alamat.setText("Address not available")
                }
            } else {
                binding.alamat.setText("Location not available")
            }
        }
    }
}
