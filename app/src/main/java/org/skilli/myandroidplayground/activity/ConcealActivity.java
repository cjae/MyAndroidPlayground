package org.skilli.myandroidplayground.activity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.facebook.android.crypto.keychain.SharedPrefsBackedKeyChain;
import com.facebook.crypto.Crypto;
import com.facebook.crypto.Entity;
import com.facebook.crypto.exception.CryptoInitializationException;
import com.facebook.crypto.exception.KeyChainException;
import com.facebook.crypto.util.SystemNativeCryptoLibrary;

import org.skilli.myandroidplayground.R;
import org.skilli.myandroidplayground.util.CryptoTestUtils;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ConcealActivity extends AppCompatActivity {

//    final String MEDIA_PATH = new String("/storage/sdcard0/Pictures/Screenshots/newImagefile.txt");
//    private File file = new File(MEDIA_PATH);
//
//    final String VIDEO_MEDIA_PATH = new String("/storage/sdcard0/Pictures/Screenshots/image.png");

    final String MEDIA_PATH = "/storage/sdcard0/Pictures/Screenshots/newVideoFile.txt";
    private File file = new File(MEDIA_PATH);

    final String VIDEO_MEDIA_PATH = "/storage/sdcard0/Pictures/Screenshots/threading.mp4";
    private File videoFile = new File(VIDEO_MEDIA_PATH);

    Button decryptBtn;
    ImageView displayImage;
    VideoView displayVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conceal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        displayVideo = (VideoView) findViewById(R.id.videoView1);

        displayImage = (ImageView) findViewById(R.id.displayImage);
        decryptBtn = (Button) findViewById(R.id.decryptBtn);
        decryptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    doDecrypt();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (KeyChainException e) {
                    e.printStackTrace();
                } catch (CryptoInitializationException e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            doEncrypt();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyChainException e) {
            e.printStackTrace();
        } catch (CryptoInitializationException e) {
            e.printStackTrace();
        }
    }

    private void doEncrypt() throws IOException, KeyChainException, CryptoInitializationException {

        ProgressDialog progDailog = ProgressDialog.show(this, "Encrypting", "Please wait...", true);

        Crypto crypto = new Crypto(
                new SharedPrefsBackedKeyChain(this),
                new SystemNativeCryptoLibrary());

        if (!crypto.isAvailable()) {
            Toast.makeText(getApplicationContext(), "Crypto not available", Toast.LENGTH_SHORT).show();
            return;
        }

        OutputStream fileStream = new BufferedOutputStream(new FileOutputStream(file));
        OutputStream outputStream = crypto.getCipherOutputStream(fileStream, new Entity(CryptoTestUtils.ENTITY_NAME));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileInputStream fis = new FileInputStream(videoFile);

        byte[] buf = new byte[1024];
        int n;
        while (-1 != (n = fis.read(buf)))
            baos.write(buf, 0, n);

        outputStream.write(baos.toByteArray());
        outputStream.close();

        progDailog.dismiss();
    }

    private void doDecrypt() throws IOException, KeyChainException, CryptoInitializationException {

        ProgressDialog progDailog = ProgressDialog.show(this, "Decrypting", "Please wait...", true);

//        Crypto crypto = new Crypto(
//                new MyKeyChain(this,"bmd1eWVudGllbmxvbmc="),
//                new SystemNativeCryptoLibrary());

        Crypto crypto = new Crypto(
                new SharedPrefsBackedKeyChain(this),
                new SystemNativeCryptoLibrary());

        if (!crypto.isAvailable()) {
            Toast.makeText(getApplicationContext(), "Crypto not available", Toast.LENGTH_SHORT).show();
            return;
        }

        FileInputStream fileStream = new FileInputStream(file);

        InputStream inputStream = crypto.getCipherInputStream(
                fileStream, new Entity(CryptoTestUtils.ENTITY_NAME));

        int read;
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        while ((read = inputStream.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }

        inputStream.close();

        progDailog.dismiss();

        File outputDir = getApplicationContext().getCacheDir();
        String VIDEO_MEDIA_PATH = "decThreading";
        File tempDecVideoFile = File.createTempFile(VIDEO_MEDIA_PATH, ".mp4", outputDir);
        tempDecVideoFile.deleteOnExit();

        FileOutputStream fos = new FileOutputStream(tempDecVideoFile);
        fos.write(out.toByteArray());
        fos.close();

        displayVideo.setVideoURI((Uri.fromFile(tempDecVideoFile)));
        displayVideo.setMediaController(new MediaController(this));
        displayVideo.start();
        displayVideo.requestFocus();
    }

//    private void doEncrypt() throws IOException, KeyChainException, CryptoInitializationException {
//
//        ProgressDialog progDailog = ProgressDialog.show(this, "Encrypting", "Please wait...", true);
//
//        Crypto crypto = new Crypto(
//                new SharedPrefsBackedKeyChain(this),
//                new SystemNativeCryptoLibrary());
//
//        if (!crypto.isAvailable()) {
//            Toast.makeText(getApplicationContext(), "Crypto not available", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        OutputStream fileStream = new BufferedOutputStream(new FileOutputStream(videoFile));
//        OutputStream outputStream = crypto.getCipherOutputStream(fileStream, new Entity(CryptoTestUtils.ENTITY_NAME));
//
////        byte[] contentInBytes = content.getBytes();
//
//        outputStream.write(bitmapToBytes(convertFilePath(VIDEO_MEDIA_PATH)));
//        outputStream.close();
//
//        progDailog.dismiss();
//    }
//
//    private void doDecrypt() throws IOException, KeyChainException, CryptoInitializationException {
//
//        ProgressDialog progDailog = ProgressDialog.show(this, "Decrypting", "Please wait...", true);
//
////        Crypto crypto = new Crypto(
////                new MyKeyChain(this,"bmd1eWVudGllbmxvbmc="),
////                new SystemNativeCryptoLibrary());
//
//        Crypto crypto = new Crypto(
//                new SharedPrefsBackedKeyChain(this),
//                new SystemNativeCryptoLibrary());
//
//        if (!crypto.isAvailable()) {
//            Toast.makeText(getApplicationContext(), "Crypto not available", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        FileInputStream fileStream = new FileInputStream(file);
//
//        InputStream inputStream = crypto.getCipherInputStream(
//                fileStream, new Entity(CryptoTestUtils.ENTITY_NAME));
//
//        int read;
//        byte[] buffer = new byte[1024];
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//
//        while ((read = inputStream.read(buffer)) != -1) {
//            out.write(buffer, 0, read);
//        }
//
//        inputStream.close();
//
//        Bitmap bitmap = bytesToBitmap(out.toByteArray());
//
//        progDailog.dismiss();
//
//        displayImage.setImageBitmap(bitmap);
//
////        String s = new String(String.valueOf(out));
////        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
//    }

//    private Bitmap convertFilePath(String photoPath){
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//        Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options);
//
//        return bitmap;
//    }
//
////    private Bitmap convertFilePath(String photoPath){
////        BitmapFactory.Options options = new BitmapFactory.Options();
////        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
////        Bitmap bitmap = BitmapFactory.decodeFile(photoPath, options);
////
////        return bitmap;
////    }
//
//    // convert Bitmap to bytes
//    private byte[] bitmapToBytes(Bitmap photo) {
//        ByteArrayOutputStream stream = new ByteArrayOutputStream();
//        photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
//        byte[] byteArray = stream.toByteArray();
//        return byteArray;
//    }
//
//    // convert bytes to Bitmap
//    private Bitmap bytesToBitmap(byte[] bytes) {
//        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//        return bitmap;
//    }
}
