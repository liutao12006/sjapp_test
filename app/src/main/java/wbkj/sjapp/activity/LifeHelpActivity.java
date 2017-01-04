package wbkj.sjapp.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import wbkj.sjapp.BaseActivity;
import wbkj.sjapp.R;

public class LifeHelpActivity extends BaseActivity {

    private Toolbar toolbar;
    private TextView tvJdwx, tvJzfw, tvKs, tvBjfw, tvZdg, tvYygh, tvJdqx, tvSs;
    private TextView tvYwpj, tvDmxp, tvQmkx, tvDnaf, tvSjwx, tvBmys, tvXhlz, tvQcfw;
    private TextView tvJzzl, tvFpsg, tvZpsx, tvCwfw, tvCzfw, tvMyqz, tvPj, tvPxkc,tvHcsy,tvScgwk,tvQtsh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_help);
        initView();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        MyClick click = new MyClick();
        tvJdwx = (TextView) findViewById(R.id.tv_jdwx);
        tvJdwx.setOnClickListener(click);
        tvJzfw = (TextView) findViewById(R.id.tv_jzfw);
        tvJzfw.setOnClickListener(click);
        tvBjfw = (TextView) findViewById(R.id.tv_bjfw);
        tvBjfw.setOnClickListener(click);
        tvKs = (TextView) findViewById(R.id.tv_ks);
        tvKs.setOnClickListener(click);
        tvZdg = (TextView) findViewById(R.id.tv_zdg);
        tvZdg.setOnClickListener(click);
        tvYygh = (TextView) findViewById(R.id.tv_yygh);
        tvYygh.setOnClickListener(click);
        tvJdqx = (TextView) findViewById(R.id.tv_jdqx);
        tvJdqx.setOnClickListener(click);
        tvSs = (TextView) findViewById(R.id.tv_ss);
        tvSs.setOnClickListener(click);

        tvYwpj = (TextView) findViewById(R.id.tv_ywpj);
        tvYwpj.setOnClickListener(click);
        tvDmxp = (TextView) findViewById(R.id.tv_dmxp);
        tvDmxp.setOnClickListener(click);
        tvQmkx = (TextView) findViewById(R.id.tv_qmkx);
        tvQmkx.setOnClickListener(click);
        tvDnaf = (TextView) findViewById(R.id.tv_dnaf);
        tvDnaf.setOnClickListener(click);
        tvSjwx = (TextView) findViewById(R.id.tv_sjwx);
        tvSjwx.setOnClickListener(click);
        tvBmys = (TextView) findViewById(R.id.tv_bmys);
        tvBmys.setOnClickListener(click);
        tvXhlz = (TextView) findViewById(R.id.tv_xhlz);
        tvXhlz.setOnClickListener(click);
        tvQcfw = (TextView) findViewById(R.id.tv_qcfw);
        tvQcfw.setOnClickListener(click);

        tvJzzl = (TextView) findViewById(R.id.tv_jzzl);
        tvJzzl.setOnClickListener(click);
        tvFpsg = (TextView) findViewById(R.id.tv_fpsg);
        tvFpsg.setOnClickListener(click);
        tvZpsx = (TextView) findViewById(R.id.tv_zpcx);
        tvZpsx.setOnClickListener(click);
        tvCwfw = (TextView) findViewById(R.id.tv_cwfw);
        tvCwfw.setOnClickListener(click);
        tvCzfw = (TextView) findViewById(R.id.tv_czfw);
        tvCzfw.setOnClickListener(click);
        tvMyqz = (TextView) findViewById(R.id.tv_myqz);
        tvMyqz.setOnClickListener(click);
        tvPj = (TextView) findViewById(R.id.tv_pj);
        tvPj.setOnClickListener(click);
        tvPxkc = (TextView) findViewById(R.id.tv_kcpx);
        tvPxkc.setOnClickListener(click);


        tvHcsy = (TextView) findViewById(R.id.tv_hcsy);
        tvHcsy.setOnClickListener(click);
        tvScgwk = (TextView) findViewById(R.id.tv_scgwk);
        tvScgwk.setOnClickListener(click);
        tvQtsh = (TextView) findViewById(R.id.tv_qtsh);
        tvQtsh.setOnClickListener(click);


    }

    class MyClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            Bundle bundle = new Bundle();
            switch (view.getId()) {
                case R.id.tv_jdwx:
                    bundle.putString("type", "79591");
                    skipActivity(LifeHelpActivity.this, ShopActivity.class, bundle);
                    break;
                case R.id.tv_jzfw:
                    bundle.putString("type", "79800");
                    skipActivity(LifeHelpActivity.this, ShopActivity.class, bundle);
                    break;
                case R.id.tv_ks:
                    bundle.putString("type", "79592");
                    skipActivity(LifeHelpActivity.this, ShopActivity.class, bundle);
                    break;
                case R.id.tv_bjfw:
                    bundle.putString("type", "79593");
                    skipActivity(LifeHelpActivity.this, ShopActivity.class, bundle);
                    break;
                case R.id.tv_zdg:
                    bundle.putString("type", "79594");
                    skipActivity(LifeHelpActivity.this, ShopActivity.class, bundle);
                    break;
                case R.id.tv_yygh:
//                    bundle.putString("type", "7943");
//                    skipActivity(LifeHelpActivity.this, ShopActivity.class, bundle);
                    bundle.putString("url", "http://wy.guahao.com/");
                    bundle.putBoolean("fromlift",true);
                    skipActivity(LifeHelpActivity.this, WebviewActivity.class, bundle);
                    break;
                case R.id.tv_jdqx:
                    bundle.putString("type", "79596");
                    skipActivity(LifeHelpActivity.this, ShopActivity.class, bundle);
                    break;
                case R.id.tv_ss:
                    bundle.putString("type", "79597");
                    skipActivity(LifeHelpActivity.this, ShopActivity.class, bundle);
                    break;


                case R.id.tv_ywpj:
                    bundle.putString("type", "79801");
                    skipActivity(LifeHelpActivity.this, ShopActivity.class, bundle);
                    break;
                case R.id.tv_dmxp:
                    bundle.putString("type", "79802");
                    skipActivity(LifeHelpActivity.this, ShopActivity.class, bundle);
                    break;
                case R.id.tv_qmkx:
                    bundle.putString("type", "79803");
                    skipActivity(LifeHelpActivity.this, ShopActivity.class, bundle);
                    break;
                case R.id.tv_dnaf:
                    bundle.putString("type", "79804");
                    skipActivity(LifeHelpActivity.this, ShopActivity.class, bundle);
                    break;
                case R.id.tv_sjwx:
                    bundle.putString("type", "79805");
                    skipActivity(LifeHelpActivity.this, ShopActivity.class, bundle);
                    break;
                case R.id.tv_bmys:
                    bundle.putString("type", "79806");
                    skipActivity(LifeHelpActivity.this, ShopActivity.class, bundle);

                    break;
                case R.id.tv_xhlz:
                    bundle.putString("type", "79807");
                    skipActivity(LifeHelpActivity.this, ShopActivity.class, bundle);
                    break;
                case R.id.tv_qcfw:
                    bundle.putString("type", "79808");
                    skipActivity(LifeHelpActivity.this, ShopActivity.class, bundle);
                    break;

                case R.id.tv_jzzl:
                    bundle.putString("type", "79809");
                    skipActivity(LifeHelpActivity.this, ShopActivity.class, bundle);
                    break;
                case R.id.tv_fpsg:
                    bundle.putString("type", "79810");
                    skipActivity(LifeHelpActivity.this, ShopActivity.class, bundle);
                    break;
                case R.id.tv_zpcx:
                    bundle.putString("type", "79811");
                    skipActivity(LifeHelpActivity.this, ShopActivity.class, bundle);
                    break;
                case R.id.tv_cwfw:
                    bundle.putString("type", "79812");
                    skipActivity(LifeHelpActivity.this, ShopActivity.class, bundle);
                    break;
                case R.id.tv_czfw:
                    bundle.putString("type", "79813");
                    skipActivity(LifeHelpActivity.this, ShopActivity.class, bundle);
                    break;
                case R.id.tv_myqz:
                    bundle.putString("type", "79814");
                    skipActivity(LifeHelpActivity.this, ShopActivity.class, bundle);

                    break;
                case R.id.tv_pj:
                    bundle.putString("type", "79815");
                    skipActivity(LifeHelpActivity.this, ShopActivity.class, bundle);
                    break;
                case R.id.tv_kcpx:
                    bundle.putString("type", "79816");
                    skipActivity(LifeHelpActivity.this, ShopActivity.class, bundle);
                    break;

                case R.id.tv_hcsy:
                    bundle.putString("type", "79817");
                    skipActivity(LifeHelpActivity.this, ShopActivity.class, bundle);
                    break;
                case R.id.tv_scgwk:
                    bundle.putString("type", "79818");
                    skipActivity(LifeHelpActivity.this, ShopActivity.class, bundle);
                    break;
                case R.id.tv_qtsh:
                    bundle.putString("type", "79819");
                    skipActivity(LifeHelpActivity.this, ShopActivity.class, bundle);
                    break;

                default:break;
            }
        }
    }

}
