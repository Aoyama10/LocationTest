package jp.ac.kit.locationtest;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;

public class MainActivity extends AppCompatActivity implements LocationListener
{

    private LocationManager manager = null;
    private MqttAndroidClient mqttAndroidClient;
    private TextView latitude;
    private TextView longitude;
    private TextView altitude;
    private String latlng;
    private MqttMessage message;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //mqttbrokerに接続
        mqttAndroidClient = new MqttAndroidClient(this, "tcp://192.168.11.7:15672", "piyo");
        try
        {
            mqttAndroidClient.connect();
        }
        catch (MqttException e)
        {
            e.printStackTrace();
        }

        //GPS取得
        manager = (LocationManager)getSystemService(LOCATION_SERVICE);
        latitude = (TextView)findViewById(R.id.latitude_id); //緯度
        longitude = (TextView)findViewById(R.id.longitude_id); //経度
        altitude = (TextView)findViewById(R.id.altitude_id); //高度
        message.getPayload();
    }

    @Override
    protected void onPause()
    {
        // TODO Auto-generated method stub
        if(manager != null)
        {
            manager.removeUpdates(this);
        }
        super.onPause();

        //publish
        try
        {
            mqttAndroidClient.publish("topic/hoge", latlng.getBytes(), 0, false);
        }
        catch (MqttPersistenceException e)
        {
            e.printStackTrace();
        }
        catch (MqttException e)
        {
            e.printStackTrace();
        }
        //subscribe
        try
        {
            mqttAndroidClient.subscribe("topic/hoge", 0);
        }
        catch (MqttPersistenceException e)
        {
            e.printStackTrace();
        }
        catch (MqttException e)
        {
            e.printStackTrace();
        }
    }

    /*
    @Override
    public void messageArrived(String topic, MqttMessage message)
    {
        message.getPayload();
    }
    */

    @Override
    protected void onResume()
    {
    // TODO Auto-generated method stub
        //GPS情報の更新
        if(manager != null)
        {
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
        super.onResume();
    }

    @Override
    public void onLocationChanged(Location location)
    {
    // TODO Auto-generated method stub
    String str = "緯度：" + location.getLatitude();
    latitude.setText(str);
    str = "経度：" + location.getLongitude();
    longitude.setText(str);
    str = "高度：" + location.getAltitude();
    altitude.setText(str);
    latlng = location.getLatitude() + ":" + location.getLongitude();
    }

    @Override
    public void onProviderDisabled(String provider) {
    // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
    // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    // TODO Auto-generated method stub
    }
}
