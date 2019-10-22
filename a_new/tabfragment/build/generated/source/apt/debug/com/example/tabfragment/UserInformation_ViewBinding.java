// Generated code from Butter Knife. Do not modify!
package com.example.tabfragment;

import android.view.View;
import android.widget.ImageButton;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class UserInformation_ViewBinding implements Unbinder {
  private UserInformation target;

  private View view7f08007c;

  @UiThread
  public UserInformation_ViewBinding(UserInformation target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public UserInformation_ViewBinding(final UserInformation target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.information_to_set, "field 'informationToSet' and method 'onViewClicked'");
    target.informationToSet = Utils.castView(view, R.id.information_to_set, "field 'informationToSet'", ImageButton.class);
    view7f08007c = view;
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
    UserInformation target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.informationToSet = null;

    view7f08007c.setOnClickListener(null);
    view7f08007c = null;
  }
}
