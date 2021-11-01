package com.example.helpdroid.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.helpdroid.R;
import com.example.helpdroid.constants.Constants;
import com.example.helpdroid.databinding.ActivityProfileBinding;
import com.github.dhaval2404.imagepicker.ImagePicker;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.editUsername.setOnClickListener(this);
        binding.editUserEmail.setOnClickListener(this);
        binding.editUserProfilePic.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Bundle bundle = new Bundle();
        switch (v.getId()) {
            case R.id.edit_username:
                bundle.putInt(Constants.USER_EDIT_SELECTION, Constants.USER_NAME_VALUE);
                bundle.putString(Constants.USER_NAME, (String) binding.username.getText());
                Intent intent1 = new Intent(ProfileActivity.this,EditUserProfileActivity.class);
                intent1.putExtras(bundle);
                startActivity(intent1);
                break;
            case R.id.edit_user_email:
                bundle.putInt(Constants.USER_EDIT_SELECTION, Constants.USER_EMAIL_VALUE);
                bundle.putString(Constants.USER_EMAIL, (String) binding.userEmail.getText());
                Intent intent2 = new Intent(ProfileActivity.this,EditUserProfileActivity.class);
                intent2.putExtras(bundle);
                startActivity(intent2);
                break;
            case R.id.edit_user_profile_pic:
                ImagePicker.with(this)
                        .galleryOnly()
                        .crop() //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
        }
    }
        @Override
        public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            Uri uri = data.getData();
            binding.profileImage.setImageURI(uri);

        }
}