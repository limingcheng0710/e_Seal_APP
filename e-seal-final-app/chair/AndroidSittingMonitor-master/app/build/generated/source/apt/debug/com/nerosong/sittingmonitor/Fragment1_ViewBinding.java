// Generated code from Butter Knife. Do not modify!
package com.nerosong.sittingmonitor;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class Fragment1_ViewBinding implements Unbinder {
  private Fragment1 target;

  private View view2131230766;

  @UiThread
  public Fragment1_ViewBinding(final Fragment1 target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.camera, "field 'camera' and method 'onClick'");
    target.camera = Utils.castView(view, R.id.camera, "field 'camera'", ImageButton.class);
    view2131230766 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick();
      }
    });
    target.picture = Utils.findRequiredViewAsType(source, R.id.picture, "field 'picture'", ImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    Fragment1 target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.camera = null;
    target.picture = null;

    view2131230766.setOnClickListener(null);
    view2131230766 = null;
  }
}
