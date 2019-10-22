// Generated code from Butter Knife. Do not modify!
package com.nerosong.sittingmonitor;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.github.mikephil.charting.charts.PieChart;
import java.lang.IllegalStateException;
import java.lang.Override;

public class Fragment3_ViewBinding implements Unbinder {
  private Fragment3 target;

  private View view2131230770;

  private View view2131230864;

  @UiThread
  public Fragment3_ViewBinding(final Fragment3 target, View source) {
    this.target = target;

    View view;
    target.pieChart = Utils.findRequiredViewAsType(source, R.id.pieChart, "field 'pieChart'", PieChart.class);
    target.etOP = Utils.findRequiredViewAsType(source, R.id.et3output, "field 'etOP'", EditText.class);
    view = Utils.findRequiredView(source, R.id.clear_sql, "field 'clearSql' and method 'onViewClicked'");
    target.clearSql = Utils.castView(view, R.id.clear_sql, "field 'clearSql'", Button.class);
    view2131230770 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onViewClicked();
      }
    });
    view = Utils.findRequiredView(source, R.id.record, "field 'record' and method 'record'");
    target.record = Utils.castView(view, R.id.record, "field 'record'", Button.class);
    view2131230864 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.record();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    Fragment3 target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.pieChart = null;
    target.etOP = null;
    target.clearSql = null;
    target.record = null;

    view2131230770.setOnClickListener(null);
    view2131230770 = null;
    view2131230864.setOnClickListener(null);
    view2131230864 = null;
  }
}
