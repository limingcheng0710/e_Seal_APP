// Generated code from Butter Knife. Do not modify!
package com.example.tabfragment;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class RegisterActivity_ViewBinding implements Unbinder {
  private RegisterActivity target;

  @UiThread
  public RegisterActivity_ViewBinding(RegisterActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public RegisterActivity_ViewBinding(RegisterActivity target, View source) {
    this.target = target;

    target.etRegisterUsername = Utils.findRequiredViewAsType(source, R.id.et_register_username, "field 'etRegisterUsername'", EditText.class);
    target.etRegisterPwd = Utils.findRequiredViewAsType(source, R.id.et_register_pwd, "field 'etRegisterPwd'", EditText.class);
    target.cbProtocol = Utils.findRequiredViewAsType(source, R.id.cb_protocol, "field 'cbProtocol'", CheckBox.class);
    target.btRegisterSubmit = Utils.findRequiredViewAsType(source, R.id.bt_register_submit, "field 'btRegisterSubmit'", Button.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    RegisterActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.etRegisterUsername = null;
    target.etRegisterPwd = null;
    target.cbProtocol = null;
    target.btRegisterSubmit = null;
  }
}
