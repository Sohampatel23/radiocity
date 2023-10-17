package com.app.radiocity.Adapter;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.app.radiocity.Provider.FilesProvider;
import com.app.radiocity.Provider.PanProvider;
import com.app.radiocity.R;
import com.app.radiocity.View.FilePreview;
import com.app.radiocity.other.SharedConst;
import com.app.radiocity.preferences.SharedPrefUtils;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

public class FilesAdapter extends RecyclerView.Adapter<FilesAdapter.FilesViewHolder> {
    private Context context;
    Activity activity;
    String filelink,file,filetype;
    ArrayList<FilesProvider> filesProviders;

    public FilesAdapter(ArrayList<FilesProvider> filesProviders, Context context, Activity activity)
    {
        this.filesProviders = filesProviders;
        this.context = context;
        this.activity = activity;
        this.filetype = filetype;
    }

    @NonNull
    @Override
    public FilesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_list_preview,parent,false);
        return new FilesViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull FilesViewHolder holder, int position) {
        final FilesProvider getDataAdapter1 = filesProviders.get(position);
        holder.title.setText(getDataAdapter1.getFilename());
        holder.ftype.setText(getDataAdapter1.getFiletype());
        file = getDataAdapter1.getFilename();
        String im = getDataAdapter1.getImage();
        Log.e("image",im);
        if(im.contains(".pdf") || im.contains(".PDF"))
        {
            holder.img.getLayoutParams().height = 200;
            holder.img.getLayoutParams().width = 200;
            holder.img.requestLayout();
            Glide.with(context).load(R.drawable.pdf_img).into(holder.img);
        }
        else if(im.contains(".txt") || im.contains(".TXT"))
        {
            holder.img.getLayoutParams().height = 200;
            holder.img.getLayoutParams().width = 200;
            holder.img.requestLayout();
            Glide.with(context).load(R.drawable.doc).into(holder.img);
        }
        else {
            File path1 = Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES);
            File file1 = new File(path1, getDataAdapter1.getFilename());
            if (file1.exists()) {
                Glide.with(context).load(file1).into(holder.img);
            } else {
                filelink = "";

                filelink = SharedPrefUtils.getStringUtils(activity, SharedConst.FILELINK);
                filelink = filelink + getDataAdapter1.getFilename();
                Log.e("file", filelink);
                DownloadManager manager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(filelink);
                DownloadManager.Request request = new DownloadManager.Request(uri);
                String title = URLUtil.guessFileName(filelink, null, null);
                request.setTitle(file);
                request.setMimeType("application/txt/pdf/jpg/jpeg");
                request.setDescription("Downloading please wait...");
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES, getDataAdapter1.getFilename());
                long reference = manager.enqueue(request);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        File path1 = Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES);
                        File file1 = new File(path1, getDataAdapter1.getFilename());
                        Glide.with(context).load(file1).into(holder.img);
                    }
                }, 1000);
            }
        }

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ((ContextCompat.checkSelfPermission(activity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
                    if ((ActivityCompat.shouldShowRequestPermissionRationale(activity,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE))) {

                    } else {
                        ActivityCompat.requestPermissions(activity,
                                new String[]{
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                100);
                    }
                } else {
                    filelink = "";
                    filelink = SharedPrefUtils.getStringUtils(activity, SharedConst.FILELINK);
                    filelink = filelink + getDataAdapter1.getFilename();
                    Log.e("file", filelink);
                    DownloadManager manager = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);
                    Uri uri = Uri.parse(filelink);
                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    String title = URLUtil.guessFileName(filelink, null, null);
                    request.setTitle(file);
                    request.setMimeType("application/txt/pdf/jpg/jpeg");
                    request.setDescription("Downloading please wait...");
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOCUMENTS, getDataAdapter1.getFilename());
                    long reference = manager.enqueue(request);
                    Log.e("csref", reference + "");

                    String im = getDataAdapter1.getFilename();
                    if (im.contains(".pdf") || im.contains(".PDF")) {
                        Intent imn = new Intent(context, FilePreview.class);
                        imn.putExtra("filetype", "pdf");
                        imn.putExtra("file", im);
                        context.startActivity(imn);
                    } else {
                        Intent imn = new Intent(context, FilePreview.class);
                        imn.putExtra("filetype", "image");
                        imn.putExtra("file", im);
                        context.startActivity(imn);
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return filesProviders.size();
    }

    public class FilesViewHolder extends RecyclerView.ViewHolder {
        TextView title,ftype;
        ImageView img;
        CardView card;
        public FilesViewHolder(@NonNull View row) {
            super(row);
            title = (TextView) row.findViewById(R.id.tv_filename);
            ftype = (TextView) row.findViewById(R.id.tv_filetype);
            img = (ImageView) row.findViewById(R.id.file_preview);
            card = (CardView) row.findViewById(R.id.backcolor);
        }
    }

}
