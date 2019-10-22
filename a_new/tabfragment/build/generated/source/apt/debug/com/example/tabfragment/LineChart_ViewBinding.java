// Generated code from Butter Knife. Do not modify!
package com.example.tabfragment;

import android.view.View;
import android.widget.ImageView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LineChart_ViewBinding implements Unbinder {
  private LineChart target;

  private View view7f080003;

  @UiThread
  public LineChart_ViewBinding(LineChart target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public LineChart_ViewBinding(final LineChart target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.LineChart_to_Bluetooth, "field 'LineChartToBluetooth' and method 'LineChart_to_Bluetooth'");
    target.LineChartToBluetooth = Utils.castView(view, R.id.LineChart_to_Bluetooth, "field 'LineChartToBluetooth'", ImageView.class);
    view7f080003 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.LineChart_to_Bluetooth();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    LineChart target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.LineChartToBluetooth = null;

    view7f080003.setOnClickListener(null);
    view7f080003 = null;
  }
}
