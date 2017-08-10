package com.example.yankaibang.bluetooth.Scanner;

import android.os.Build;

/**
 * Created by Salmon on 2017/3/8 0008.
 */

public class ScanHelper {

    public static Scanner getScanner() {
        Scanner scanner;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            scanner = AndroidLScanner.getInstance();
        } else {
            scanner = BelowAndroidLScanner.getInstance();
        }
        return scanner;
    }
}
