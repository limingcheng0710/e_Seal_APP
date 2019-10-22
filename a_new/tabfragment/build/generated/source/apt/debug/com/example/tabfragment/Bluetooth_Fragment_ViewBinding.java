// Generated code from Butter Knife. Do not modify!
package com.example.tabfragment;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class Bluetooth_Fragment_ViewBinding implements Unbinder {
  private Bluetooth_Fragment target;

  private View view7f08003e;

  private View view7f080027;

  private View view7f080029;

  private View view7f08002d;

  @UiThread
  public Bluetooth_Fragment_ViewBinding(final Bluetooth_Fragment target, View source) {
    this.target = target;

    View view;
    target.etBluetoothOutput = Utils.findRequiredViewAsType(source, R.id.et_bluetooth_output, "field 'etBluetoothOutput'", EditText.class);
    target.imgBluetooth = Utils.findRequiredViewAsType(source, R.id.img_bluetooth, "field 'imgBluetooth'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.clear, "field 'clear' and method 'et_out_clear'");
    target.clear = Utils.castView(view, R.id.clear, "field 'clear'", Button.class);
    view7f08003e = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.et_out_clear();
      }
    });
    view = Utils.findRequiredView(source, R.id.bluetooth_to_LineChart, "field 'bluetoothToLineChart' and method 'bluetooth_to_Linechart'");
    target.bluetoothToLineChart = Utils.castView(view, R.id.bluetooth_to_LineChart, "field 'bluetoothToLineChart'", Button.class);
    view7f080027 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.bluetooth_to_Linechart();
      }
    });
    view = Utils.findRequiredView(source, R.id.bt_conncet_bluetooth, "field 'btConncetBluetooth' and method 'onViewClicked'");
    target.btConncetBluetooth = Utils.castView(view, R.id.bt_conncet_bluetooth, "field 'btConncetBluetooth'", Button.class);
    view7f080029 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.bt_receive, "field 'btReceive' and method 'onViewClicked'");
    target.btReceive = Utils.castView(view, R.id.bt_receive, "field 'btReceive'", Button.class);
    view7f08002d = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked(p0);
      }
    });
    target.appleswitch = Utils.findRequiredViewAsType(source, R.id.appleswitch, "field 'appleswitch'", AppleSwitch.class);
    target.switchAndroid = Utils.findRequiredViewAsType(source, R.id.switch_android, "field 'switchAndroid'", Switch.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    Bluetooth_Fragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.etBluetoothOutput = null;
    target.imgBluetooth = null;
    target.clear = null;
    target.bluetoothToLineChart = null;
    target.btConncetBluetooth = null;
    target.btReceive = null;
    target.appleswitch = null;
    target.switchAndroid = null;

    view7f08003e.setOnClickListener(null);
    view7f08003e = null;
    view7f080027.setOnClickListener(null);
    view7f080027 = null;
    view7f080029.setOnClickListener(null);
    view7f080029 = null;
    view7f08002d.setOnClickListener(null);
    view7f08002d = null;
  }
}
