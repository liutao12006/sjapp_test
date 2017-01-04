package wbkj.sjapp.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import wbkj.sjapp.BaseActivity;
import wbkj.sjapp.R;
import wbkj.sjapp.SharedPreferencesUtil;
import wbkj.sjapp.models.JsonModel;
import wbkj.sjapp.models.User;
import wbkj.sjapp.utils.Base64Util;
import wbkj.sjapp.utils.HttpUtil;
import wbkj.sjapp.utils.OkHttpClientManager;
import wbkj.sjapp.utils.StringUtils;

public class UserInfoActivity extends BaseActivity {

    private Toolbar toolbar;
    private RelativeLayout rlImg, rlNname, rlSex;
    private ImageView imgUser;
    private TextView tvNname, tvSex, tvPhone, tvModPwd;

    private SharedPreferencesUtil sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = new SharedPreferencesUtil(this);
        setContentView(R.layout.activity_user_info);
        initView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 0:
                    String sex = data.getStringExtra("sex");
                    tvSex.setText(sex);
                    break;
                case 1:
                    String nick = data.getStringExtra("nick");
                    tvNname.setText(nick);
                    break;
                case 2:
                    if(data == null){
                        return;
                    }
                    Uri uri = data.getData();
                    ContentResolver cr = this.getContentResolver();
                    Bitmap bitmap = null;
                    try {
                        bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                        imgUser.setImageBitmap(bitmap);
                    }catch (IOException e) {
                        e.printStackTrace();
                    }

                    String path = getRealFilePath(uri);
                    imgUpload(Base64Util.imageToBase64(path));
//                    File file = new File(path);
//                    try {
//                        imgUpload(file);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }

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
            public void onClick(View v) {
                finish();
            }
        });

        rlImg = (RelativeLayout) findViewById(R.id.rl_user_img);
        rlNname = (RelativeLayout) findViewById(R.id.rl_user_nickname);
        rlSex = (RelativeLayout) findViewById(R.id.rl_user_sex);
        imgUser = (ImageView) findViewById(R.id.img_user_head);
        if (sp.getUser().getThirdfrom() == 3) {
            Picasso.with(this).load(HttpUtil.IMGURL + sp.getUser().getImg()).into(imgUser);
        } else {
            if( StringUtils.isNotBlank(sp.getUser().getImg().trim())){
                Picasso.with(this).load(HttpUtil.IMGURL+sp.getUser().getImg()).into(imgUser);
            }else{

            }

        }
        tvNname = (TextView) findViewById(R.id.tv_user_name);
        tvSex = (TextView) findViewById(R.id.tv_user_sex);
        tvNname.setText(sp.getUser().getNickname());
        tvPhone = (TextView) findViewById(R.id.tv_user_phone);
        if (sp.getUser().getSex() == 0) {
            tvSex.setText("女");
        } else {
            tvSex.setText("男");
        }
        tvPhone.setText(sp.getUser().getPhone());
        tvModPwd = (TextView) findViewById(R.id.user_modify_pwd);

        rlImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_PICK);
//                startActivityForResult(intent, 2);
            }
        });
        rlNname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(UserInfoActivity.this, ModifyNnameActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        rlSex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(UserInfoActivity.this, ModifySexActivity.class);
                startActivityForResult(intent, 0);
            }
        });
        tvModPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skipActivity(UserInfoActivity.this, ModifyPwdActivity.class, null);
            }
        });
        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(UserInfoActivity.this,"haha",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(intent, 2);
            }
        });
    }

    /**
     * 上传头像
     */
    private void imgUpload(String base64img) {
        String vipid =  String.valueOf(sp.getUser().getVipid());
        String  img = base64img;
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[] {
                new OkHttpClientManager.Param("vipid", String.valueOf(sp.getUser().getVipid())),
                new OkHttpClientManager.Param("img", base64img)
        };

        OkHttpClientManager.postAsyn(HttpUtil.upImgUrl, params, new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(UserInfoActivity.this, "网络链接超时，请重新上传", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
                Log.e("suc----", response.toString());
                JsonModel json = new Gson().fromJson(response, JsonModel.class);
                if (json.result == 1) {
                    Toast.makeText(UserInfoActivity.this, "头像上传成功", Toast.LENGTH_SHORT).show();
                    User user = sp.getUser();
                    user.setImg(json.img);
                    sp.onLoginSuccess(user);
                } else {
                    Toast.makeText(UserInfoActivity.this, "头像上传失败", Toast.LENGTH_SHORT).show();
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
