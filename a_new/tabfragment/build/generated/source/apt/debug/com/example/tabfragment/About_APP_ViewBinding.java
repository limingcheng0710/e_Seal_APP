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

public class About_APP_ViewBinding implements Unbinder {
  private About_APP target;

  private View view7f080008;

  @UiThread
  public About_APP_ViewBinding(About_APP target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public About_APP_ViewBinding(final About_APP target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.about_to_set, "field 'aboutToSet' and method 'onViewClicked'");
    target.aboutToSet = Utils.castView(view, R.id.about_to_set, "field 'aboutToSet'", ImageView.class);
    view7f080008 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    About_APP target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.aboutToSet = null;

    view7f080008.setOnClickListener(null);
    view7f080008 = null;
  }
}
