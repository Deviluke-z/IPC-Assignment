package minhduc.deviluke.clientapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.baseproject.AIDLRemoteService;
import com.example.baseproject.CallBack;

public class MainActivity extends AppCompatActivity {

  private AIDLRemoteService aidlRemoteService;
  private TextView tvGetProduct;
  private Button btnGet;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    btnGet = findViewById(R.id.btnGet);
    tvGetProduct = findViewById(R.id.tvGetProduct);

    btnGet.setOnClickListener(v -> {
      try {
        aidlRemoteService.sendParcel(callBack);
      } catch (Exception e) {
        Toast.makeText(this, "Bug", Toast.LENGTH_SHORT).show();
        e.printStackTrace();
      }
    });
  }

  private CallBack callBack = new CallBack.Stub() {
    @Override
    public void finish(String string) {
      runOnUiThread(() -> tvGetProduct.setText(string));
    }
  };

  private ServiceConnection connection = new ServiceConnection() {
    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
      aidlRemoteService = AIDLRemoteService.Stub.asInterface(service);
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
    }

    @Override
    public void onBindingDied(ComponentName name) {
      ServiceConnection.super.onBindingDied(name);
    }
  };

  @Override
  protected void onResume() {
    super.onResume();
    setProduct();
  }

  private void setProduct() {
    String packageName = "com.example.baseproject";
    String serviceName = packageName + ".RemoteService";

    Intent intent = new Intent();
    intent.setComponent(new ComponentName(packageName, serviceName));
    intent.putExtra("message", "new product");
    try {
      startService(intent);
      Intent mIntent = new Intent();
      mIntent.setPackage(packageName);
      mIntent.setClassName(packageName, serviceName);
      if (!bindService(mIntent, connection, Context.BIND_AUTO_CREATE)) {
        Log.d("Debug", "null bind");
      }
    } catch (Exception exception) {
      tvGetProduct.setText("Start server app first please");
      exception.printStackTrace();
    }
  }
}