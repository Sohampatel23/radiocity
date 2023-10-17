package com.app.radiocity.ViewModel;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.pdf.PdfRenderer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.webkit.URLUtil;

import androidx.lifecycle.ViewModel;

import com.app.radiocity.R;
import com.app.radiocity.databinding.ActivityFilePreviewBinding;
import com.app.radiocity.databinding.ActivityProfileDetailBinding;
import com.app.radiocity.other.SharedConst;
import com.app.radiocity.preferences.DialogUtils;
import com.app.radiocity.preferences.SharedPrefUtils;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoViewAttacher;

import java.io.File;


public class FilePreviewModel extends ViewModel implements View.OnClickListener,View.OnTouchListener, ScaleGestureDetector.OnScaleGestureListener{
    private Activity activity;
    private ActivityFilePreviewBinding binding;
    DialogUtils utils;
    String filelink,link;
    String file,name;
    float scale = 1.0f;
    private ScaleGestureDetector scaleGestureDetector;
    private float mScaleFactor = 1.0f;
    PhotoViewAttacher mAttacher;

    public <T> FilePreviewModel(Activity activity, T binding) {
        this.activity = activity;
        this.binding = (ActivityFilePreviewBinding) binding;
        utils = new DialogUtils(activity);
        initview();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        scaleGestureDetector.onTouchEvent(motionEvent);
        return true;
    }

    private void initview() {
        binding.getRoot().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        scaleGestureDetector = new ScaleGestureDetector(activity, this);
        binding.previewtitle.backicon.setImageDrawable(activity.getDrawable(R.drawable.ic_baseline_keyboard_backspace_black));

        binding.previewtitle.tvTitle.setText("File Preview");

        binding.previewtitle.download.setOnClickListener(this);
        binding.previewtitle.backicon.setOnClickListener(this);
      link = SharedPrefUtils.getStringUtils(activity, SharedConst.FILELINK);

        Intent in = activity.getIntent();
        String filetype = in.getStringExtra("filetype");
        name = in.getStringExtra("file");

        String uri = link + name;
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS);
        File file = new File(path, name);
        if(filetype.equals("pdf"))
        {
            Log.e("uri value",file+"");
            binding.pdfPdfview.setVisibility(View.VISIBLE);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    binding.pdfPdfview.fromFile(file)
                            .enableSwipe(true)
                            .swipeHorizontal(false)
                            .enableDoubletap(true)
                            .load();
                }
            },2000);

        }
        else
        {
            binding.pdfPdfview.setVisibility(View.GONE);
        }


        if(filetype.equals("image"))
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
            File path1 = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOCUMENTS);
            File file1 = new File(path1, name);
            binding.ivImgpreview.setVisibility(View.VISIBLE);
            Glide.with(activity).load(file1).into(binding.ivImgpreview);

                }
            },1000);
            mAttacher = new PhotoViewAttacher(binding.ivImgpreview);
            mAttacher.update();
        }
        else
        {
            binding.ivImgpreview.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.download:
                Intent in = activity.getIntent();
                file = in.getStringExtra("file");
                filedownload(file);
                break;

            case R.id.backicon:
                removefile();
                break;
        }
    }

    private void removefile() {

        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS);
        File file = new File(path, name);
      if(file.exists())
      {
          if(file.delete())
          {
              Log.e("deleted","file deleted");
          }
          else
          {
              Log.e("not deleted","file not deleted");
          }
      }
        activity.finish();
    }

    private void filedownload(String file) {
        filelink = "";
        filelink = SharedPrefUtils.getStringUtils(activity, SharedConst.FILELINK);
        filelink = filelink + file;
        Log.e("file",filelink);
        DownloadManager manager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(filelink);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        String title = URLUtil.guessFileName(filelink,null,null);
        request.setTitle(file);
        request.setMimeType("application/txt/pdf/jpg/jpeg");
        request.setDescription("Downloading please wait...");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,file);
        long reference = manager.enqueue(request);
        Log.e("csref",reference+"");
    }

    @Override
    public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
        scale *= scaleGestureDetector.getScaleFactor();
        scale = Math.max(0.1f, Math.min(scale, 10.0f));
        binding.ivImgpreview.setScaleX(scale);
        binding.ivImgpreview.setScaleY(scale);
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
        return false;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
    }

}
