package in.eightfolds.winga.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

//import com.canhub.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import in.eightfolds.winga.R;
import in.eightfolds.winga.adapter.ImageAdapter;
import in.eightfolds.winga.fragment.CropImageFragment;
import in.eightfolds.winga.utils.Constants;
import in.eightfolds.winga.utils.Utils;

/**
 * Created by Eightfolds on 1/28/16.
 */
public class ImageViewActivity extends BaseActivity implements View.OnClickListener {
    private ViewPager pager;
    private ImageAdapter imageAdapter;
    private int position;
    private RelativeLayout headerLayout;
    private List<Object> objectList;
    private TextView cropTV;
    private boolean isToCrop;
    private TextView submitTV;
    private RelativeLayout cropRL;
    private String str;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.setAppLanguage(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.imageview);
        pager = findViewById(R.id.pager);
        str = getIntent().getStringExtra(Constants.DATA);
        isToCrop = getIntent().getBooleanExtra(Constants.IS_TO_CROP, false);
        cropTV = findViewById(R.id.cropTV);
        submitTV = findViewById(R.id.submitTV);
        cropRL = findViewById(R.id.cropRL);
        boolean isBitmap = getIntent().getBooleanExtra(Constants.IS_LOCAL_IMAGE, false);
        List<String> stringList = new ArrayList<>();
        stringList.add(str);
        if (isToCrop) {
            cropRL.setVisibility(View.VISIBLE);
        }
        imageAdapter = new ImageAdapter(getSupportFragmentManager(), this, stringList, isBitmap);
        imageAdapter.setToCrop(isToCrop);
        findViewById(R.id.closeIv).setOnClickListener(this);
        findViewById(R.id.editIv).setOnClickListener(this);
        pager.setAdapter(imageAdapter);
        pager.setCurrentItem(position);
        cropTV.setOnClickListener(this);
        submitTV.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
//        switch (view.getId()) {
//            case R.id.cropTV:
//                getCropImage();
//                break;
//            case R.id.submitTV:
//                getCropImage();
//                break;
//            case R.id.closeIv:
//                onBackPressed();
//                break;
//            case R.id.editIv:
//
//                break;
//        }
    }


   /* private void getCropImage() {
        CropImageFragment cropImageFragment = (CropImageFragment) imageAdapter.getCurrentFragment();
        CropImageView cropImageView = cropImageFragment.getImageView();
        Bitmap bitmap = null;
        if (cropImageView != null && isToCrop) {
            bitmap = cropImageView.getCroppedImage();
            cropImageView.setImageBitmap(bitmap);
        }
        Intent intent = new Intent();
        if (bitmap != null) {
            Uri tempUri = getImageUri(getApplicationContext(), bitmap);
            if(tempUri != null) {
                File finalFile = new File(getRealPathFromURI(tempUri));
                intent.putExtra(Constants.PATH, finalFile.toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }*/

    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "WinGa", null);
        if(path != null) {
            return Uri.parse(path);
        }else{
            return null;
        }
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
}
