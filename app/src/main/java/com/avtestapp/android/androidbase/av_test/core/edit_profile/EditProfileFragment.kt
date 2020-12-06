package com.avtestapp.android.androidbase.av_test.core.edit_profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.avtestapp.android.androidbase.App
import com.avtestapp.android.androidbase.R
import com.avtestapp.android.androidbase.av_test.onboarding.upload_image.UploadImageFragmentDirections
import com.avtestapp.android.androidbase.base.BaseViewModel
import com.avtestapp.android.androidbase.base.BaseViewModelFragment
import com.avtestapp.android.androidbase.databinding.EditProfileFragmentBinding
import com.avtestapp.android.androidbase.extensions.hide
import com.avtestapp.android.androidbase.extensions.show
import com.avtestapp.android.androidbase.utils.CommonSharedPrefs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import timber.log.Timber
import java.io.File
import javax.inject.Inject

class EditProfileFragment : BaseViewModelFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var sharedPrefs: CommonSharedPrefs

    private lateinit var viewModel: EditProfileViewModel

    private lateinit var binding: EditProfileFragmentBinding

    val TITLE_AV_TEST_IMAGE_CAPTURE = "customer_image"
    val IMAGE_REQUEST_CODE = 15
    var imageCropRequestCode : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = EditProfileFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun getViewModel() = viewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (mainActivity.applicationContext as App).component.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(EditProfileViewModel::class.java)
        setupView()
        subScribeObservables()
    }


    private fun setupView() {

        mainActivity.setUpToolBar("Edit Profile", isRootPage = false)
        val profile = sharedPrefs.getUserProfile()

        if (profile?.imageUrl != null && profile.imageUrl != ""){
            displayCircularImage(profile.imageUrl, binding.userProfilePicture)
            Timber.e("profile ${profile.imageUrl}")
            binding.userInitialsText.hide()
            binding.userProfilePicture.show()

        } else {
            val firstInitial = profile?.firstName?.first()
            val secondInitial = profile?.lastName?.first()
            binding.userProfilePicture.hide()
            binding.userInitialsText.show()
            binding.userInitialsText.text = "$firstInitial$secondInitial"
        }
        binding.emailInputEditText.setText(profile?.email)
        binding.nameInputEditText.setText("${profile?.firstName} ${profile?.lastName}")
        //binding.licenseNumberInputEditText.setText(${profile?.})

        binding.userImageLayout.setOnClickListener {
            imageCropRequestCode = IMAGE_REQUEST_CODE
            CropImage.activity()
                .setOutputCompressQuality(45)
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(activity!!, this)
        }

        binding.logout.setOnClickListener {
             
        }
    }

    private fun getCameraImageCaptureUri(title: String): Uri {
        val destination = Environment.getExternalStorageDirectory().path + "/$title.jpg"
        return Uri.fromFile(File(destination))
    }

    private fun subScribeObservables(){
        viewModel.uploadSuccessfulObserver.observe(viewLifecycleOwner, Observer {
            //showToast("Image upload successful ${it.imageUrl}")
            showDialogWithAction(title = "Success",
                body = "your profile picture was changed Successfully",
                positiveAction = {}
            )
        })

        viewModel.formPath.observe(viewLifecycleOwner, Observer {
            viewModel.uploadCustomerImage()
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when {
            (requestCode == IMAGE_REQUEST_CODE)
                    && resultCode == Activity.RESULT_OK -> {

                imageCropRequestCode = requestCode

                val imageUri = getCameraImageCaptureUri(TITLE_AV_TEST_IMAGE_CAPTURE)

                CropImage.activity(imageUri)
                    .setOutputCompressQuality(45)
                    .start(activity!!, this)
            }
            (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) -> {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {

                    val croppedImageUri: Uri = result.uri
                    displayUserImage(croppedImageUri)

                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    val error = result.error
                    Timber.e(error)
                }
            }
        }

    }

    private fun displayUserImage(croppedImageUri: Uri) {
        Glide.with(requireActivity())
            .load(croppedImageUri)
            .placeholder(R.drawable.image_place_holder)
            .apply(RequestOptions().transform(RoundedCorners(8)))
            .into(binding.userProfilePicture)
        viewModel.formPath.value = croppedImageUri.path
    }

}