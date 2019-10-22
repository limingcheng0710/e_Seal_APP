// Generated code from Butter Knife. Do not modify!
package com.nerosong.sittingmonitor;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class Fragment1_ViewBinding implements Unbinder {
  private Fragment1 target;

  private View view2131230791;

  private View view2131230798;

  private View view2131230769;

  private View view2131230756;

  private View view2131230757;

  private View view2131230759;

  private View view2131230758;

  @UiThread
  public Fragment1_ViewBinding(final Fragment1 target, View source) {
    this.target = target;

    View view;
    target.linearLayout = Utils.findRequiredViewAsType(source, R.id.guangzhexian, "field 'linearLayout'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.draw_chart, "field 'drawChart' and method 'draw_chart'");
    target.drawChart = Utils.castView(view, R.id.draw_chart, "field 'drawChart'", ImageButton.class);
    view2131230791 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.draw_chart();
      }
    });
    target.layoutTable = Utils.findRequiredViewAsType(source, R.id.layout_table, "field 'layoutTable'", LinearLayout.class);
    view = Utils.findRequiredView(source, R.id.et_cardView, "field 'etCardView' and method 'onViewClicked'");
    target.etCardView = Utils.castView(view, R.id.et_cardView, "field 'etCardView'", CardView.class);
    view2131230798 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked();
      }
    });
    view = Utils.findRequiredView(source, R.id.clear_linechart, "field 'clearLinechart' and method 'clartLineChart'");
    target.clearLinechart = Utils.castView(view, R.id.clear_linechart, "field 'clearLinechart'", ImageButton.class);
    view2131230769 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.clartLineChart();
      }
    });
    target.frag1ImgBluetooth = Utils.findRequiredViewAsType(source, R.id.frag1_img_bluetooth, "field 'frag1ImgBluetooth'", ImageView.class);
    target.tvBTState = Utils.findRequiredViewAsType(source, R.id.tvBTState, "field 'tvBTState'", TextView.class);
    target.etOutput = Utils.findRequiredViewAsType(source, R.id.etOutput, "field 'etOutput'", EditText.class);
    view = Utils.findRequiredView(source, R.id.btnConnectBT, "method 'connect'");
    view2131230756 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.connect();
      }
    });
    view = Utils.findRequiredView(source, R.id.btnListenBT, "method 'listen'");
    view2131230757 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.listen();
      }
    });
    view = Utils.findRequiredView(source, R.id.btnTrainModel, "method 'train'");
    view2131230759 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.train();
      }
    });
    view = Utils.findRequiredView(source, R.id.btnPredict, "method 'go'");
    view2131230758 = view;
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
    Fragment1 target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.linearLayout = null;
    target.drawChart = null;
    target.layoutTable = null;
    target.etCardView = null;
    target.clearLinechart = null;
    target.frag1ImgBluetooth = null;
    target.tvBTState = null;
    target.etOutput = null;

    view2131230791.setOnClickListener(null);
    view2131230791 = null;
    view2131230798.setOnClickListener(null);
    view2131230798 = null;
    view2131230769.setOnClickListener(null);
    view2131230769 = null;
    view2131230756.setOnClickListener(null);
    view2131230756 = null;
    view2131230757.setOnClickListener(null);
    view2131230757 = null;
    view2131230759.setOnClickListener(null);
    view2131230759 = null;
    view2131230758.setOnClickListener(null);
    view2131230758 = null;
  }
}
