package com.example.baseproject;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RemoteService extends Service {

  int mNum;
  private Handler mHandler;

  private final AIDLRemoteService aidlRemoteService = new AIDLRemoteService.Stub() {
    @Override
    public int get() {
      return Process.myPid();
    }

    @Override
    public void sendParcel(CallBack callback) {
      Log.d("Debug", "Check callback");
      if (mNum == 0) {
        try {
          Log.d("Debug", "Product is produced");
          callback.waiting("In production. Please wait!");
        } catch (RemoteException e) {
          e.printStackTrace();
        }
      }
      while (true) {
        Log.d("Debug", "Check while loop");
        if (mNum > 0) {
          mNum -= 1;
          try {
            callback.finish(getRealTime() + "Success, number of product in store: " + mNum);
            Log.d("Debug", "Product is sent");
          } catch (RemoteException e) {
            e.printStackTrace();
          }
          break;
        }
      }
    }
  };

  @Override
  public void onCreate() {
    super.onCreate();
    Log.d("Debug", "Start Service");
    mNum = 0;

    HandlerThread mHandlerThread = new HandlerThread("Request from client");
    mHandlerThread.start();
    mHandler = new Handler(mHandlerThread.getLooper());

    newProduct();
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    return START_NOT_STICKY;
  }

  private void newProduct() {
    mHandler.post(() -> {
      while (true) {
        if (mNum < 6) {
          try {
            // 5s to produce 1 product
            Thread.sleep(5000);
          } catch (InterruptedException exception) {
            exception.printStackTrace();
          }
          mNum += 1;
        }
      }
    });
  }

  private String getRealTime() {
    @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    Date date = new Date();
    return "[" + dateFormat.format(date) + "]:";
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return aidlRemoteService.asBinder();
  }
}
