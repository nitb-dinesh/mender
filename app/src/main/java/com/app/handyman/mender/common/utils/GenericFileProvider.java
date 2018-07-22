package com.app.handyman.mender.common.utils;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * Created by adi on 4/10/18.
 */

public class GenericFileProvider extends FileProvider {

    public static Uri getUriFromFile(Context context, File file) {
        return FileProvider.getUriForFile(context,
                context.getApplicationContext().getPackageName() + ".com.app.handyman.mender",
                file);
    }
}
