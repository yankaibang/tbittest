package com.example.yankaibang.app.activity;


import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.tbittestlib.bike.TbitBle;
import com.example.yankaibang.app.DeviceAdapter;
import com.example.tbittestlib.bluetooth.BleClient;
import com.example.tbittestlib.bluetooth.IBleClient;
import com.example.tbittestlib.bluetooth.RequestDispatcher;
import com.example.tbittestlib.bluetooth.Scanner.ScannerCallbackAdapter;
import com.example.tbittestlib.bluetooth.Scanner.decorator.LogCallback;
import com.example.tbittestlib.bluetooth.Scanner.decorator.NoneRepeatCallback;
import com.example.tbittestlib.bluetooth.model.SearchResult;
import com.example.tbittestlib.bluetooth.request.BleResponse;
import com.example.tbittestlib.bluetooth.request.ConnectRequest;
import com.example.yankaibang.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.salmonzhg.easypermission.EasyPermissionHelper;
import me.salmonzhg.easypermission.callback.DeniedCallback;
import me.salmonzhg.easypermission.callback.GrantCallback;

public class DerectConnectActivity extends AppCompatActivity {

    private static final String TAG = "DerectConnectActivity";

    @BindView(R.id.recycler_devices)
    RecyclerView recyclerView;

    private DeviceAdapter deviceAdapter;

    private List<SearchResult> data = new ArrayList<>();

    private IBleClient bleClient;

    private RequestDispatcher requestDispatcher;
    private Unbinder mUnbinder;
    private EasyPermissionHelper mEasyPermissionHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEasyPermissionHelper = new EasyPermissionHelper(this);

        mEasyPermissionHelper.checkPermissions(
                new GrantCallback() {
                    @Override
                    public void onAllGranted() {
                        initView();
                        initData();
                    }
                }, new DeniedCallback() {
                    @Override
                    public void atLeastOneDenied(List<String> list, List<String> list1) {
                        Toast.makeText(DerectConnectActivity.this, "请同意全部权限", Toast.LENGTH_SHORT)
                                .show();
                        finish();
                    }
                }, Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION);





    }

    private void initData() {
        bleClient = new BleClient();
        requestDispatcher = new RequestDispatcher(bleClient);
        refresh();
    }

    private void initView() {
        setContentView(R.layout.activity_derect_connect);
        mUnbinder = ButterKnife.bind(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        deviceAdapter = new DeviceAdapter(data, this);
        deviceAdapter.setOnclickListener(new DeviceAdapter.OnClickListener() {
            @Override
            public void onClick(SearchResult searchResult) {
                TbitBle.stopScan();
                Log.d("asd", "startConnect");
                final long startTime = System.currentTimeMillis();
                if (bleClient.getConnectionState() >= 3) {
                    bleClient.disconnect();
                }
                requestDispatcher.addRequest(new ConnectRequest(searchResult.getDevice(), new BleResponse() {
                    @Override
                    public void onResponse(int resultCode) {
                        Log.d("asd", "onResponse: " + resultCode + " t: " + (System.currentTimeMillis() - startTime));
                        Toast.makeText(DerectConnectActivity.this,"Result: " + resultCode + "\nTime： " + (System.currentTimeMillis() - startTime), Toast.LENGTH_LONG).show();
                    }
                }));
            }
        });
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(deviceAdapter);
    }

    private void afterConnected() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_device, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                refresh();
                break;
        }
        return true;
    }

    private void refresh() {

        /*TbitBle.stopScan();

        if (bleClient.getConnectionState() >= 3)
            bleClient.disconnect();*/

        data.clear();

        deviceAdapter.notifyDataSetChanged();

        // 最终得到的结果的回调
        ScannerCallbackAdapter scannerCallback = new ScannerCallbackAdapter() {
            @Override
            public void onDeviceFounded(BluetoothDevice bluetoothDevice, int i, byte[] bytes) {
                Log.d(TAG, "onDeviceFounded: " + bluetoothDevice.getName());
                Log.d(TAG, "onDeviceFounded: " + Thread.currentThread());
                data.add(0, new SearchResult(bluetoothDevice, i, bytes));

                deviceAdapter.notifyItemInserted(0);

            }

            @Override
            public void onScanCanceled() {
                super.onScanCanceled();
                Log.d(TAG, "onScanCanceled: ");
            }

            @Override
            public void onScanStart() {
                super.onScanStart();
                Log.d(TAG, "onScanStart: ");
            }

            @Override
            public void onScanStop() {
                super.onScanStop();
                Log.d(TAG, "onScanStop: ");
            }
        };

        // 确保结果非重复的装饰器
        NoneRepeatCallback noneRepeatCallback = new NoneRepeatCallback(scannerCallback);
        // 收集日志的装饰器，这个最好放在最外层包裹
        LogCallback logCallback = new LogCallback(noneRepeatCallback);

        TbitBle.startScan(logCallback, 5000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TbitBle.stopScan();
        bleClient.disconnect();
        mUnbinder.unbind();
        mEasyPermissionHelper.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        mEasyPermissionHelper.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mEasyPermissionHelper.onResume();
    }
}
