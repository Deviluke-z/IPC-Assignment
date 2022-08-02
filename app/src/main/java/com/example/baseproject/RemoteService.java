package com.example.baseproject;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.RemoteException;

import androidx.annotation.Nullable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RemoteService extends Service {

  int mNum;
  private HandlerThread mHandlerThread;
  private Handler mHandler;

  @Override
  public void onCreate() {
    super.onCreate();
    mNum = 0;
    mHandlerThread = new HandlerThread("Request from client");
    mHandlerThread.start();
    mHandler = new Handler(mHandlerThread.getLooper());
    
    newProduct();
  }

  @Override
  public int onStartCommand(Intent intent, int flags, int startId) {
    if (intent.getStringExtra("request") != null) {
      String data = intent.getStringExtra("request");
    }
    return START_NOT_STICKY;
  }

  private void newProduct() {
    Thread thread = new Thread(() -> {
      while (true) {
        if (mNum < 6) {
          try {
            Thread.sleep(1000);
          } catch (InterruptedException exception) {
            exception.printStackTrace();
          }
          mNum += 1;
        }
      }
    });
    thread.start();
  }

  private AIDLRemoteService aidlRemoteService = new AIDLRemoteService.Stub() {
    @Override
    public void get() throws RemoteException {
    }

    @Override
    public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {
    }

    @Override
    public void sendParcel(CallBack callback) throws RemoteException {
      mHandler.post(new Runnable() {
        @Override
        public void run() {
          while (true) {
            if (mNum > 0) {
              mNum -= 1;
              try {
                DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                Date date = new Date();
                String time = "[" + dateFormat.format(date) + "]";
                callback.finish(getDateNow() + "Success, number of product in store: " + mNum);
              } catch (RemoteException e) {
                e.printStackTrace();
              }
              break;
            }
          }
        }
      });
    }

    @Override
    public IBinder asBinder() {
      return null;
    }
  };

  private String getDateNow() {
    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    Date date = new Date();
    String time = "[" + dateFormat.format(date) + "]:";
    return time;
  }

  @Nullable
  @Override
  public IBinder onBind(Intent intent) {
    return aidlRemoteService.asBinder();
  }
}
