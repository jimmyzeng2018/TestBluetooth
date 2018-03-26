package com.example.administrator.testbluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.ParcelUuid;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    String uuid = "2F2DFFF0-2E85-649D-3545-3586428F5DA3".toLowerCase();
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothGatt bluetoothGatt;
    private boolean ss;
    private Button connect;
    private Button disConnect;

    private Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    Toast.makeText(MainActivity.this,"连接成功"+msg.obj,Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    Toast.makeText(MainActivity.this, "断开"+msg.obj , Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(MainActivity.this, "连接失败：错误码"+msg.obj , Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private Button send;
    private Button close;
    private Button setNull;
    private TextView tv;
    private Button clearCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connect = (Button)findViewById(R.id.button);
        disConnect = (Button) findViewById(R.id.button2);
        send = (Button)findViewById(R.id.button3);
        close = (Button)findViewById(R.id.button4);
        setNull = (Button)findViewById(R.id.button5);
        tv = (TextView)findViewById(R.id.tv);
        clearCache = (Button)findViewById(R.id.button6);

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {

            Toast.makeText( this , "设备不支持蓝牙4.0" , Toast.LENGTH_SHORT).show();

            finish();

        }

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
//        mBluetoothAdapter.disable();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mBluetoothAdapter.enable();
//            }
//        },1000);

//        final BluetoothAdapter.LeScanCallback  dd = new BluetoothAdapter.LeScanCallback() {
//            @Override
//            public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
//                Log.e("startLeScan", "name="+device.getName()+"address="+device.getAddress());
//            }
//        };
//        mBluetoothAdapter.startLeScan(new UUID[]{UUID.fromString("2F2DFFF0-2E85-649D-3545-3586428F5DA3")},dd);
//
//
//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mBluetoothAdapter.stopLeScan(dd);
//
//            }
//        },10000);

//        List<ScanFilter> filters = new ArrayList<>();
//
//        List<ScanFilter> bleScanFilters = new ArrayList<>();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            bleScanFilters.add(
//                    new ScanFilter.Builder().setServiceUuid(ParcelUuid.fromString("2F2DFFF0-2E85-649D-3545-3586428F5DA3")).build()
//            );
//
//
//        ScanSettings bleScanSettings = new ScanSettings.Builder().build();
//
//
//            final ScanCallback df = new ScanCallback() {
//                @Override
//                public void onScanResult(int callbackType, ScanResult result) {
//                    super.onScanResult(callbackType, result);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        Log.e("startLeScan", "name="+result.getDevice().getName()+"address="+result.getDevice().getAddress());
//                    }
//                }
//            };
//        mBluetoothAdapter.getBluetoothLeScanner().startScan(bleScanFilters, bleScanSettings,df );
//                    new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    mBluetoothAdapter.getBluetoothLeScanner().stopScan(df);
//                }
//            }
//        },10000);
//
//        }



//        mBluetoothAdapter.stopLeScan(new BluetoothAdapter.LeScanCallback() {
//            @Override
//            public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
//
//            }
//        });

        connect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice("E8:07:BF:27:37:20");//E8:07:BF:27:36:8B
                tv.setText("连接"+device.getAddress());
                final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
                int state = bluetoothManager.getConnectionState(device,BluetoothProfile.GATT);
                if (BluetoothProfile.STATE_CONNECTED == state && bluetoothGatt != null) {
                    bluetoothGatt.close();
                    bluetoothGatt = null;
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

//                final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
//                int state = bluetoothManager.getConnectionState(device,BluetoothProfile.GATT);
//                if (BluetoothProfile.STATE_CONNECTED != state) {
                        bluetoothGatt = device.connectGatt(MainActivity.this, false, new BluetoothGattCallback() {
                            @Override
                            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                                super.onConnectionStateChange(gatt, status, newState);
                                if (newState == BluetoothProfile.STATE_CONNECTED) {
                                    try {
                                        Thread.sleep(500);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    gatt.discoverServices();
                                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {

                                    Message msg = new Message();
                                    msg.what = 2;
                                    msg.obj = gatt.getDevice().getAddress();
                                    handler.sendMessage(msg);
                                    gatt.close();
                                } else{

                                    Message msg = new Message();
                                    msg.what = 3;
                                    msg.obj = newState;
                                    handler.sendMessage(msg);
                                    gatt.close();
                                }

                            }

                            @Override
                            public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                                super.onServicesDiscovered(gatt, status);

                                BluetoothGattService service = gatt.getService(UUID.fromString("2F2DFFF0-2E85-649D-3545-3586428F5DA3"));

                                BluetoothGattCharacteristic read = service.getCharacteristic(UUID.fromString("2F2DFFF4-2E85-649D-3545-3586428F5DA3"));

                                gatt.setCharacteristicNotification(read, true);
//                        BluetoothGattDescriptor descriptorRead= read.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));
//                        descriptorRead.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
//                        gatt.writeDescriptor(descriptorRead);


                                for (BluetoothGattDescriptor dp : read.getDescriptors()) {
                                    if (dp != null) {
                                        dp.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                                        gatt.writeDescriptor(dp);
                                    }
                                }
                                Message msg = new Message();
                                msg.what = 1;
                                msg.obj = gatt.getDevice().getAddress();
                                handler.sendMessage(msg);

                            }

                            @Override
                            public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                                Log.e("jimmy", "onCharacteristicRead");
                                super.onCharacteristicRead(gatt, characteristic, status);
                                if (status == BluetoothGatt.GATT_SUCCESS) {
                                    Log.e("jimmy", "read value: " + characteristic.getValue().toString());
                                }
                            }

                            @Override
                            public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                                super.onCharacteristicWrite(gatt, characteristic, status);
                                if (status == BluetoothGatt.GATT_SUCCESS) {
                                    Log.e("jimmy", "write value: " + characteristic.getValue().toString());

                                }
                            }

                            @Override
                            public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                                super.onCharacteristicChanged(gatt, characteristic);
                                Log.e("onCharacteristicChanged","onCharacteristicChanged");
                                //BluetoothGattService service = gatt.getService(UUID.fromString("2F2DFFF0-2E85-649D-3545-3586428F5DA3"));
                                //BluetoothGattCharacteristic read = service.getCharacteristic(UUID.fromString("2F2DFFF4-2E85-649D-3545-3586428F5DA3"));
                                //gatt.readCharacteristic(characteristic);
                                //onCharacteristicRead(gatt,characteristic,BluetoothGatt.GATT_SUCCESS);
                                BluetoothGattService service = gatt.getService(UUID.fromString("2F2DFFF0-2E85-649D-3545-3586428F5DA3"));
                                BluetoothGattCharacteristic read = service.getCharacteristic(UUID.fromString("2F2DFFF4-2E85-649D-3545-3586428F5DA3"));
                                if (!bluetoothGatt.readCharacteristic(read)) {
                                    onCharacteristicRead(bluetoothGatt, read, BluetoothGatt.GATT_SUCCESS);
                                }

                            }
                        });
                    }
                },1000);

                }

        });

        disConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bluetoothGatt != null)
                    bluetoothGatt.disconnect();
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bluetoothGatt != null) {
                    bluetoothGatt.close();
                }
            }
        });

        setNull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bluetoothGatt = null;
                mBluetoothAdapter = null;
            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bluetoothGatt != null) {
                    BluetoothGattService service = bluetoothGatt.getService(UUID.fromString("2F2DFFF0-2E85-649D-3545-3586428F5DA3"));
                    if (service != null) {
                        BluetoothGattCharacteristic write = service.getCharacteristic(UUID.fromString("2F2DFFF5-2E85-649D-3545-3586428F5DA3"));
                        if (write != null) {
                            //发b8
                            byte cmd = (byte) 0xB8;
                            byte[] data = new byte[]{0x00, 0x00};
                            write.setValue(packCMD(cmd, data));
                            bluetoothGatt.writeCharacteristic(write);
                        }
                    }

                }
            }
        });


        clearCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshDeviceCache();
            }
        });





//        IntentFilter filter = new IntentFilter();
////发现设备
//        filter.addAction(BluetoothDevice.ACTION_FOUND);
////设备连接状态改变
//        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
////蓝牙设备状态改变
//        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
//        registerReceiver(mBluetoothReceiver, filter);
    }


    private static byte[] packCMD(byte cmd, byte[] data){

        int size = data.length;
        byte[] bytes = new byte[size + 5];

        bytes[0] = (byte) 0xaa;
        bytes[1] = (byte) 0x55;
        bytes[2] = cmd;
        bytes[3] = (byte) size;
        //		for (int i = 4; i < bytes.length; i++){
        //			bytes[i] = data[i-4];
        //		}
        for (int i = 0; i < data.length; i++){
            bytes[i+4] = data[i];
        }
        bytes[bytes.length-1] = (byte) calcCrcSum(bytes);


        return bytes;
    }

    public static byte calcCrcSum(byte[] data){
        int sum = 0;
        for (int i = 0; i < data.length-1; i++){
            sum = sum + data[i];
        }
        return (byte) sum;
    }


    @Override
    protected void onResume() {
        super.onResume();
        //mBluetoothAdapter.startDiscovery();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //unregisterReceiver(mBluetoothReceiver);
        if (bluetoothGatt != null){
            bluetoothGatt.disconnect();
            bluetoothGatt.close();
            bluetoothGatt = null;
        }
    }

    /**	 * Clears the internal cache and forces a refresh of the services from the	 * remote device.	 */
    public boolean refreshDeviceCache() {
        if (bluetoothGatt != null) {
            try {
                BluetoothGatt localBluetoothGatt = bluetoothGatt;
                Method localMethod = localBluetoothGatt.getClass().getMethod("refresh", new Class[0]);
                if (localMethod != null) {
                    boolean bool = ((Boolean) localMethod.invoke(localBluetoothGatt, new Object[0])).booleanValue();
                    return bool;
                }
            } catch (Exception localException) {
                Log.i("", "An exception occured while refreshing device");
            }
        }
        return false;
    }

//    private BroadcastReceiver mBluetoothReceiver = new BroadcastReceiver(){
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            Log.e(getClass().getName(),"mBluetoothReceiver action ="+action);
//            if(BluetoothDevice.ACTION_FOUND.equals(action)){//每扫描到一个设备，系统都会发送此广播。
//                //获取蓝牙设备
//                BluetoothDevice scanDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//                if(scanDevice == null || scanDevice.getName() == null) return;
//                Log.e(getClass().getName(), "name="+scanDevice.getName()+"address="+scanDevice.getAddress());
//                //蓝牙设备名称
//                String name = scanDevice.getName();
//                if(name != null && name.endsWith("3720")){
//                    mBluetoothAdapter.cancelDiscovery();
//
//                    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice("E8:07:BF:27:37:20");
//                    try {
//                        BluetoothSocket btSocket = device.createRfcommSocketToServiceRecord(UUID.fromString(uuid));
//                        btSocket.connect();
//                        Toast.makeText(MainActivity.this,"连接成功",Toast.LENGTH_SHORT).show();
//                    } catch (IOException e ) {
//                        e.printStackTrace();
//                        Toast.makeText(MainActivity.this,"连接失败" + e.getMessage(),Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
//                mBluetoothAdapter.startDiscovery();
//            }
//        }
//
//    };
}
