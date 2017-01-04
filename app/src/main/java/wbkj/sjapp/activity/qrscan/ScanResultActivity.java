package wbkj.sjapp.activity.qrscan;

import android.os.Bundle;
import android.widget.TextView;

import wbkj.sjapp.BaseActivity;
import wbkj.sjapp.R;
import wbkj.sjapp.utils.Contants;

public class ScanResultActivity extends BaseActivity {

	private TextView tvResult;
	private String result;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		result = getIntent().getBundleExtra(Contants.ARGUMENTS_NAME).getString("result");
		setContentView(R.layout.activity_result);
		initView();
	}

	private void initView() {

		tvResult = (TextView) findViewById(R.id.scan_content);
		tvResult.setText(result);
	}
}
