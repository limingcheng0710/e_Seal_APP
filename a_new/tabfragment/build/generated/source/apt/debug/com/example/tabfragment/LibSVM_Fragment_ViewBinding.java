// Generated code from Butter Knife. Do not modify!
package com.example.tabfragment;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LibSVM_Fragment_ViewBinding implements Unbinder {
  private LibSVM_Fragment target;

  private View view7f080032;

  private View view7f080031;

  @UiThread
  public LibSVM_Fragment_ViewBinding(final LibSVM_Fragment target, View source) {
    this.target = target;

    View view;
    target.etOutput = Utils.findRequiredViewAsType(source, R.id.etOutput, "field 'etOutput'", EditText.class);
    target.fileTrain = Utils.findRequiredViewAsType(source, R.id.file_train, "field 'fileTrain'", ImageView.class);
    target.filePredict = Utils.findRequiredViewAsType(source, R.id.file_predict, "field 'filePredict'", ImageView.class);
    view = Utils.findRequiredView(source, R.id.btnTrainMode, "method 'train'");
    view7f080032 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.train();
      }
    });
    view = Utils.findRequiredView(source, R.id.btnPredict, "method 'go'");
    view7f080031 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.go();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    LibSVM_Fragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.etOutput = null;
    target.fileTrain = null;
    target.filePredict = null;

    view7f080032.setOnClickListener(null);
    view7f080032 = null;
    view7f080031.setOnClickListener(null);
    view7f080031 = null;
  }
}
