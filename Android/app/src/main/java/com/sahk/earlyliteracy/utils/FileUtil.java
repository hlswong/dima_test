package com.sahk.earlyliteracy.utils;

import java.io.File;

/**
 * Created by marso on 12/12/2016.
 */

public class FileUtil {

    public static void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecursive(child);

        fileOrDirectory.delete();
    }
}
