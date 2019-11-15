package com.example.sendbluetoothandroiddev;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.UUID;

public class BluetoothConnectionService {
    private static final String TAG = "BluetoothConnectionServ";
    private static final String appName;

    static {
        appName = "BT";
    }

    private static final UUID MY_UUID_INSECURE =
            UUID.fromString(" 00001101-0000-1000-8000-00805F9B34FB");

    private final BluetoothAdapter mBluetoothAdapter;
    Context mContext;

    private AcceptThread mInsecureAcceptThread;

    private ConnectThread mConnectThread;
    private BluetoothDevice mmDevice;
    private UUID deviceUUID;
    ProgressDialog mProgressDialog;

    private ConnectedThread mConnectedThread;

    public BluetoothConnectionService(Context context) {
        mContext = context;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        start();
    }


    private class AcceptThread extends Thread{
        private final BluetoothServerSocket mmServerSocket;
        public AcceptThread(){
            BluetoothServerSocket tmp = null;

            // Create a new listening server socket
            try{
                tmp = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord("BT", MY_UUID_INSECURE);
                Log.d(TAG, "Accept thread, setting up the server using" + MY_UUID_INSECURE);
            }catch(IOException e){

            }
            mmServerSocket = tmp;


        }
        // This is automatically called
        public void run(){
            Log.d(TAG, "run: AcceptThread running");

            BluetoothSocket socket = null;

            try{
                Log.d(TAG, "run: RFCOM server socket start...");
                // Waits for a connection to be made
                socket = mmServerSocket.accept();

                Log.d(TAG, "run: RFCOM server socket accepted connection");
            } catch(IOException e){
                Log.e(TAG, "AcceptThread: IOException" + e.getMessage());

            }

            if (socket != null){
                connected(socket, mmDevice);
            }
            Log.i(TAG, "END mAcceptThread");
        }

        public void cancel(){
            Log.d(TAG, "cancel: Cancelling AcceptThread");
            try{
                mmServerSocket.close();
            } catch(IOException e){
                Log.e(TAG, "cancel: Close of AcceptThread Server socket failed" + e.getMessage());
            }
        }

    }

    private class ConnectThread extends Thread{
        private BluetoothSocket mmSocket;

        public ConnectThread(BluetoothDevice device, UUID uuid){
            Log.d(TAG, "ConnectThread: started.");
            mmDevice = device;
            deviceUUID = uuid;
        }
        public void run(){
            BluetoothSocket tmp = null;
            Log.i(TAG, "RUN: mConnectThread");

            // Get a bluetooth socket for a connection with a given BT device
            try {
                Log.d(TAG, "ConnectThread: trying to create InsecureRFcomSocket using UUID:" + MY_UUID_INSECURE);
                tmp = mmDevice.createRfcommSocketToServiceRecord(deviceUUID);
            } catch (IOException e) {
                Log.e(TAG, "ConnectThread: Could not create RFInsecurecomSocket" + e.getMessage());
            }

            mmSocket = tmp;
            // Cancel discovery mode because it slows down a connection
            mBluetoothAdapter.cancelDiscovery();

            //make a connection to a bluetooth socket

            try {
                // This is a blocking call and will only return a successful connection or an exception
                mmSocket.connect();
                Log.d(TAG, "run: ConnectThread connected.");
            } catch (IOException e) {
                // Close the socket
                try {
                    mmSocket.close();
                    Log.d(TAG, "run: Closed Socket.");
                } catch (IOException ex) {
                    Log.e(TAG, "mConnectThread: run: Unable to close connection in socket" + e.getMessage());
                }
                Log.d(TAG, "run: ConnectThread: could not connect to UUID:" + MY_UUID_INSECURE);
            }
            connected(mmSocket, mmDevice);
        }
        public void cancel(){

            try{
                Log.d(TAG, "cancel: Closing client socket");
                mmSocket.close();

            } catch(IOException e){
                Log.e(TAG, "cancel: Close of mSocket in ConnectThread failed" + e.getMessage());
            }
        }
    }



    // Initiate AcceptThread
    public synchronized void start(){
        Log.d(TAG, "start");


        // Cancel any thread attempting to make a connection
        if (mConnectThread != null){
            mConnectThread.cancel();
            mConnectThread = null;
        }
        if (mInsecureAcceptThread == null){
            mInsecureAcceptThread = new AcceptThread();
            mInsecureAcceptThread.start();

        }
    }
    // Initiate ConnectThread
    public void startClient(BluetoothDevice device, UUID uuid){
        Log.d(TAG, "startClient: started.");

        // Initialize progress dialog
        // Pops up when the connection is trying to be made
        // UNCOMMENT THIS IF YOU FIGURE OUT WHAT'S HAPPENING HERE
       // mProgressDialog = ProcessDialog.show(mContext, "Connecting BT", "Please wait...", true);
        mConnectThread = new ConnectThread(device, uuid);
        mConnectThread.start();
    }

    private class ConnectedThread extends Thread{
        private final BluetoothSocket mmSocket;
        private final InputStream mmInputStream;
        private final OutputStream mmOutputStream;

        public ConnectedThread(BluetoothSocket socket){
            Log.d(TAG, "ConnectedThread: starting.");

            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Dismiss the progress dialog box when connection is established
            mProgressDialog.dismiss();

            try {
                tmpIn = mmSocket.getInputStream();
                tmpOut = mmSocket.getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mmInputStream = tmpIn;
            mmOutputStream = tmpOut;
        }

        public void run(){
            byte[] buffer = new byte[1024]; // buffer store for the stream
            int bytes; // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs
            while(true){
                try {
                    bytes = mmInputStream.read(buffer);
                    String incomingMessage = new String(buffer, 0, bytes);
                    Log.d(TAG, "InputStream: " + incomingMessage);
                } catch (IOException e) {
                    Log.e(TAG, "write: Error while reading inputStream:" + e.getMessage());
                    // break connection and break loop
                    break;
                }
            }
        }

        // Call this from the main activity to send data to the remote device
        public void write(byte[] bytes){
            String text = new String(bytes, Charset.defaultCharset());
            Log.d(TAG, "write: Writing to OutputStream:" + text);
            try {
                mmOutputStream.write(bytes);
            } catch (IOException e) {
                Log.e(TAG, "wrtie: Error while writting to output stream:" + e.getMessage());
            }

        }
        // Call this from the main activity to shut down the connection
        public void cancel(){
            try {
                mmSocket.close();
            } catch (IOException e) { }

        }

    }

    private void connected(BluetoothSocket mmSocket, BluetoothDevice mmDevice) {
        Log.d(TAG, "connected: Starting.");

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(mmSocket);
        mConnectedThread.start();
    }

    // Make a write method that can access the connection service which can then access the connected thread
    public void write(byte[] out){
        // Create temp obj
        ConnectedThread r;

        // Synchronize a copy of the ConnectionThread
        Log.d(TAG, "write: Write called.");
        // Perform the write
        mConnectedThread.write(out);
    }
}
