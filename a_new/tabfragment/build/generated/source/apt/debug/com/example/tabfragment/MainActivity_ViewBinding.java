// Generated code from Butter Knife. Do not modify!
package com.example.tabfragment;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MainActivity_ViewBinding implements Unbinder {
  private MainActivity target;

  @UiThread
  public MainActivity_ViewBinding(MainActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MainActivity_ViewBinding(MainActivity target, View source) {
    this.target = target;

    target.textBluetooth = Utils.findRequiredViewAsType(source, R.id.text_bluetooth, "field 'textBluetooth'", TextView.class);
    target.textSvm = Utils.findRequiredViewAsType(source, R.id.text_svm, "field 'textSvm'", TextView.class);
    target.textSet = Utils.findRequiredViewAsType(source, R.id.text_set, "field 'textSet'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    MainActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.textBluetooth = null;
    target.textSvm = null;
    target.textSet = null;
  }
}
