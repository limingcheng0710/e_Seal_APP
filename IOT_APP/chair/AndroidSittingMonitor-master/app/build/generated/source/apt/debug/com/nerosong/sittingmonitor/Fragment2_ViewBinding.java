// Generated code from Butter Knife. Do not modify!
package com.nerosong.sittingmonitor;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;
import pl.droidsonroids.gif.GifImageView;

public class Fragment2_ViewBinding implements Unbinder {
  private Fragment2 target;

  @UiThread
  public Fragment2_ViewBinding(Fragment2 target, View source) {
    this.target = target;

    target.tv2BTState = Utils.findRequiredViewAsType(source, R.id.tv2BTState, "field 'tv2BTState'", TextView.class);
    target.tv2SitState = Utils.findRequiredViewAsType(source, R.id.tv2SitState, "field 'tv2SitState'", TextView.class);
    target.sw2Music = Utils.findRequiredViewAsType(source, R.id.sw2Music, "field 'sw2Music'", Switch.class);
    target.gifviewSit = Utils.findRequiredViewAsType(source, R.id.gifviewSit, "field 'gifviewSit'", GifImageView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    Fragment2 target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.tv2BTState = null;
    target.tv2SitState = null;
    target.sw2Music = null;
    target.gifviewSit = null;
  }
}
