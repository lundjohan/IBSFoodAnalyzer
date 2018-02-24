package com.johanlund.external_storage;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;

import java.io.File;

/**
 * Created by Johan on 2017-10-04.
 * very slight rewrite (mime-type) of this class https://stackoverflow
 * .com/questions/4646913/android-how-to-use-mediascannerconnection-scanfile
 */

public class SingleMediaScanner implements MediaScannerConnection.MediaScannerConnectionClient {
    private MediaScannerConnection mMs;
    private File mFile;
    private String mimeType;

    public SingleMediaScanner(Context context, File f, String mimeType) {
        mFile = f;
        this.mimeType = mimeType;
        mMs = new MediaScannerConnection(context, this);
    }

    public void scan() {
        mMs.connect();
    }

    @Override
    public void onMediaScannerConnected() {
        mMs.scanFile(mFile.getAbsolutePath(), mimeType);
    }

    @Override
    public void onScanCompleted(String path, Uri uri) {
        mMs.disconnect();
    }

}
