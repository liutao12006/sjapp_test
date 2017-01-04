package wbkj.sjapp.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.orhanobut.logger.Logger;
import com.squareup.okhttp.Request;

import wbkj.sjapp.BaseActivity;
import wbkj.sjapp.R;
import wbkj.sjapp.SharedPreferencesUtil;
import wbkj.sjapp.models.JsonModel;
import wbkj.sjapp.utils.Base64Util;
import wbkj.sjapp.utils.HttpUtil;
import wbkj.sjapp.utils.OkHttpClientManager;
import wbkj.sjapp.utils.StringUtils;

public class EditSProActivity extends BaseActivity {

    private Toolbar toolbar;
    private EditText etName, etMony, etPhone, etDesc;
    private Button btnOk;
    private ImageView img;

    String name, money, phone, desc, path;
    private SharedPreferencesUtil sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = new SharedPreferencesUtil(this);
        setContentView(R.layout.activity_edit_spro);
        initView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    if(data == null){
                        return;
                    }
                    Uri uri = data.getData();
//                    ContentResolver cr = this.getContentResolver();
//                    Bitmap bitmap = null;
//                    try {
//                        bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
//                        img.setImageBitmap(bitmap);
//                    }catch (IOException e) {
//                        e.printStackTrace();
//                    }
                    path = getRealFilePath(uri);
                    img.setImageBitmap(new Base64Util(this).getCompressBitmap(path));
                    break;

                default:
                    break;
            }
        }
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

        etName = (EditText) findViewById(R.id.spro_et_name);
        etMony = (EditText) findViewById(R.id.spro_et_money);
        etPhone = (EditText) findViewById(R.id.spro_et_phone);
        etDesc = (EditText) findViewById(R.id.spro_et_desc);
        btnOk = (Button) findViewById(R.id.spro_btn);
        img = (ImageView) findViewById(R.id.spro_img_up);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(intent, 1);
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = etName.getEditableText().toString();
                money = etMony.getEditableText().toString();
                phone = etPhone.getEditableText().toString();
                desc = etDesc.getEditableText().toString();
                if (StringUtils.isNotBlank(name, money, phone, desc, path)) {
                    Base64Util util = new Base64Util(EditSProActivity.this);
                    Bitmap bitmap = util.getCompressBitmap(path);
                    addMethod(Base64Util.bitmapToBase64(bitmap));
                }else {
                    Toast.makeText(EditSProActivity.this, "请填写完整产品信息", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addMethod(String img) {
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[]{
                new OkHttpClientManager.Param("wname", name),
                new OkHttpClientManager.Param("wsubclass", "8861"),
                new OkHttpClientManager.Param("img", img),
                new OkHttpClientManager.Param("wdprice", money),
                new OkHttpClientManager.Param("wauthor", String.valueOf(sp.getUser().getVipid())),
                new OkHttpClientManager.Param("col1", desc),
                new OkHttpClientManager.Param("col2", phone)
        };
        OkHttpClientManager.postAsyn(HttpUtil.editsProurl, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                Logger.e(e.getMessage());
                Toast.makeText(EditSProActivity.this, "网络请求失败，请重新操作", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
                Logger.e(response.toString());
                JsonModel json = new Gson().fromJson(response, JsonModel.class);
                if (json.result == 1){
                    Toast.makeText(EditSProActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditSProActivity.this, "提交失败,请重新提交", Toast.LENGTH_SHORT).show();
                }
            }
        }, null);
    }

    /**
     * 根据选择图片后ContentProvider提供的Uri，得到图片本地存储路径
     * @param uri
     * @return
     */
    public String getRealFilePath(Uri uri) {
        if (null == uri)
            return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = this.getContentResolver().query(uri,
                    new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
}
