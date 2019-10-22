// Generated code from Butter Knife. Do not modify!
package com.example.tabfragment;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class Set_Fragment_ViewBinding implements Unbinder {
  private Set_Fragment target;

  private View view7f0800ff;

  private View view7f080007;

  private View view7f08011c;

  @UiThread
  public Set_Fragment_ViewBinding(final Set_Fragment target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.text_quit, "field 'textQuit' and method 'quit_Login'");
    target.textQuit = Utils.castView(view, R.id.text_quit, "field 'textQuit'", TextView.class);
    view7f0800ff = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.quit_Login();
      }
    });
    view = Utils.findRequiredView(source, R.id.about_app, "field 'aboutApp' and method 'about_App'");
    target.aboutApp = Utils.castView(view, R.id.about_app, "field 'aboutApp'", TextView.class);
    view7f080007 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.about_App();
      }
    });
    view = Utils.findRequiredView(source, R.id.user_information, "field 'userInformation' and method 'onViewClicked'");
    target.userInformation = Utils.castView(view, R.id.user_information, "field 'userInformation'", TextView.class);
    view7f08011c = view;
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
    Set_Fragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.textQuit = null;
    target.aboutApp = null;
    target.userInformation = null;

    view7f0800ff.setOnClickListener(null);
    view7f0800ff = null;
    view7f080007.setOnClickListener(null);
    view7f080007 = null;
    view7f08011c.setOnClickListener(null);
    view7f08011c = null;
  }
}
