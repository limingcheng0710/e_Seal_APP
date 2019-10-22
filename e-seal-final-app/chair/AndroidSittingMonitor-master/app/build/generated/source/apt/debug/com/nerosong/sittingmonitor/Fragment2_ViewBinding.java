// Generated code from Butter Knife. Do not modify!
package com.nerosong.sittingmonitor;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class Fragment2_ViewBinding implements Unbinder {
  private Fragment2 target;

  private View view2131230943;

  @UiThread
  public Fragment2_ViewBinding(final Fragment2 target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.select_All_Records, "field 'selectAllRecords' and method 'onClick'");
    target.selectAllRecords = Utils.castView(view, R.id.select_All_Records, "field 'selectAllRecords'", Button.class);
    view2131230943 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    Fragment2 target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.selectAllRecords = null;

    view2131230943.setOnClickListener(null);
    view2131230943 = null;
  }
}
