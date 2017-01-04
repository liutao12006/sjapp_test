package wbkj.sjapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.List;

import me.nereo.multi_image_selector.MultiImageSelectorActivity;
import wbkj.sjapp.BaseActivity;
import wbkj.sjapp.R;
import wbkj.sjapp.SharedPreferencesUtil;
import wbkj.sjapp.models.JsonModel;
import wbkj.sjapp.utils.Base64Util;
import wbkj.sjapp.utils.HttpUtil;
import wbkj.sjapp.utils.OkHttpClientManager;
import wbkj.sjapp.utils.StringUtils;

public class PubMsgActivity extends BaseActivity {

    private Toolbar toolbar;
    private Spinner spType;
    private EditText etContent;
    private ImageView imgUp;
    private RecyclerView imgRecy;
    private UpLoadImgAdapter adapter;
    private TextView btnPub;

    private SharedPreferencesUtil sp;
    private static final int REQUEST_IMAGE = 0;
    private List<String> upLoadImgPath;//选择的要上传的图片的路径集合
    String[] items = {"请选择帖子类型(必选)", "社区杂谈", "养生课堂", "二手车场"};
    int type = 0;
    private List<String> base64Img = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = new SharedPreferencesUtil(this);
        setContentView(R.layout.activity_pub_msg);
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

        spType = (Spinner) findViewById(R.id.msg_sp_type);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_selectable_list_item);
        spType.setAdapter(adapter);
        spType.setSelection(0,true);
        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(PubMsgActivity.this, items[i]+i, Toast.LENGTH_SHORT).show();
                type = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        imgRecy = (RecyclerView) findViewById(R.id.msg_img_recy);
        imgRecy.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        imgUp = (ImageView) findViewById(R.id.msg_img_up);
        imgUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skipToSelectUpLoadImg();
            }
        });
        etContent = (EditText) findViewById(R.id.pub_msg_content);
        btnPub = (TextView) findViewById(R.id.pub_btn);
        btnPub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = etContent.getEditableText().toString();
                if (StringUtils.isNotBlank(content)) {
                    addMsgMethod(content);
                } else {
                    Toast.makeText(PubMsgActivity.this, "发布内容不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addMsgMethod(String content) {

        OkHttpClientManager.postAsyn(HttpUtil.addMsgUrl, addParams(base64Img.size(), content), new OkHttpClientManager.ResultCallback<JsonElement>() {
            @Override
            public void onError(Request request, Exception e) {
                Toast.makeText(PubMsgActivity.this, "网络连接失败,请重试", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(JsonElement response) {
                JsonModel json = new Gson().fromJson(response, JsonModel.class);
                if (json.result == 1) {
                    Toast.makeText(PubMsgActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(PubMsgActivity.this, json.msg, Toast.LENGTH_SHORT).show();
                }
            }
        }, null);
    }

    /**
     * 选择要上传的图片
     */
    private void skipToSelectUpLoadImg() {

        Intent intent = new Intent(this, MultiImageSelectorActivity.class);
        // 是否显示调用相机拍照
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
        // 最大图片选择数量
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 6);
        // 设置模式 (支持 单选/MultiImageSelectorActivity.MODE_SINGLE 或者 多选/MultiImageSelectorActivity.MODE_MULTI)
        intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
        // 默认选择图片,回填选项(支持String ArrayList)
        intent.putStringArrayListExtra(MultiImageSelectorActivity.EXTRA_DEFAULT_SELECTED_LIST, new ArrayList<String>());
        startActivityForResult(intent, REQUEST_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_IMAGE){
            if(resultCode == RESULT_OK){
                base64Img.clear();
                // 获取返回的图片列表
                upLoadImgPath = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);
                Base64Util util = new Base64Util(this);
                if(upLoadImgPath.size()>0){
                    for(int i=0;i<upLoadImgPath.size();i++){
                        Log.e("path:", upLoadImgPath.get(i));
                        Bitmap bitmap = util.getCompressBitmap(upLoadImgPath.get(i));
                        base64Img.add(Base64Util.bitmapToBase64(bitmap));
                    }

                    imgRecy.setVisibility(View.VISIBLE);
                    if (adapter == null) {
                        adapter = new UpLoadImgAdapter();
                        imgRecy.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }



    public class UpLoadImgAdapter extends RecyclerView.Adapter{

        class ViewHolder extends RecyclerView.ViewHolder{
            private ImageView iv_upload;

            public ViewHolder(ImageView imageView) {
                super(imageView);
                iv_upload = imageView;
            }
            public ImageView getIv_upload() {
                return iv_upload;
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(new ImageView(parent.getContext()));
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ViewHolder viewHolder = (ViewHolder) holder;
            ImageView imageview = viewHolder.getIv_upload();
            imageview.setScaleType(ImageView.ScaleType.FIT_XY);

//            Bitmap bitmap = scaleBitmap(BitmapFactory.decodeFile(upLoadImgPath.get(position)), dip2px(PubMsgActivity.this, 50),dip2px(PubMsgActivity.this, 50));
            Bitmap bitmap = new Base64Util(PubMsgActivity.this).getCompressBitmap(upLoadImgPath.get(position));
            imageview.setLayoutParams(new ViewGroup.LayoutParams(dip2px(PubMsgActivity.this, 80),dip2px(PubMsgActivity.this, 80)));
//            左上右下
            imageview.setPadding(4,0,4,0);
            imageview.setImageBitmap(bitmap);
        }

        @Override
        public int getItemCount() {
            return upLoadImgPath.size();
        }
    }
    /**
     * 将图片缩放到指定大小
     *
     * @param bitmap
     * @param w
     * @param h
     * @return
     */
    public synchronized static Bitmap scaleBitmap(Bitmap bitmap, float w,
                                                  float h) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleW = w / (float) width;
        float scaleH = h / (float) height;
        matrix.postScale(scaleW, scaleH);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return bitmap;
    }

    private OkHttpClientManager.Param[] addParams(int size, String content) {
        OkHttpClientManager.Param[] params = new OkHttpClientManager.Param[0];
        switch (size) {
            case 0:
                params = new OkHttpClientManager.Param[]{
                        new OkHttpClientManager.Param("ttype", String.valueOf(type)),
                        new OkHttpClientManager.Param("tauthor", String.valueOf(sp.getUser().getVipid())),
                        new OkHttpClientManager.Param("tdcontent", content),
                        new OkHttpClientManager.Param("tname", "shangjie")
                };
                break;
            case 1:
                params = new OkHttpClientManager.Param[]{
                        new OkHttpClientManager.Param("ttype", String.valueOf(type)),
                        new OkHttpClientManager.Param("tauthor", String.valueOf(sp.getUser().getVipid())),
                        new OkHttpClientManager.Param("tdcontent", content),
                        new OkHttpClientManager.Param("tname", "shangjie"),
                        new OkHttpClientManager.Param("img2", base64Img.get(0))
                };
                break;
            case 2:
                params = new OkHttpClientManager.Param[]{
                        new OkHttpClientManager.Param("ttype", String.valueOf(type)),
                        new OkHttpClientManager.Param("tauthor", String.valueOf(sp.getUser().getVipid())),
                        new OkHttpClientManager.Param("tdcontent", content),
                        new OkHttpClientManager.Param("tname", "shangjie"),
                        new OkHttpClientManager.Param("img2", base64Img.get(0)),
                        new OkHttpClientManager.Param("img3", base64Img.get(1))
                };
                break;
            case 3:
                params = new OkHttpClientManager.Param[]{
                        new OkHttpClientManager.Param("ttype", String.valueOf(type)),
                        new OkHttpClientManager.Param("tauthor", String.valueOf(sp.getUser().getVipid())),
                        new OkHttpClientManager.Param("tdcontent", content),
                        new OkHttpClientManager.Param("tname", "shangjie"),
                        new OkHttpClientManager.Param("img2", base64Img.get(0)),
                        new OkHttpClientManager.Param("img3", base64Img.get(1)),
                        new OkHttpClientManager.Param("img4", base64Img.get(2))
                };
                break;
            case 4:
                params = new OkHttpClientManager.Param[]{
                        new OkHttpClientManager.Param("ttype", String.valueOf(type)),
                        new OkHttpClientManager.Param("tauthor", String.valueOf(sp.getUser().getVipid())),
                        new OkHttpClientManager.Param("tdcontent", content),
                        new OkHttpClientManager.Param("tname", "shangjie"),
                        new OkHttpClientManager.Param("img2", base64Img.get(0)),
                        new OkHttpClientManager.Param("img3", base64Img.get(1)),
                        new OkHttpClientManager.Param("img4", base64Img.get(2)),
                        new OkHttpClientManager.Param("img5", base64Img.get(3))
                };
                break;
            case 5:
                params = new OkHttpClientManager.Param[]{
                        new OkHttpClientManager.Param("ttype", String.valueOf(type)),
                        new OkHttpClientManager.Param("tauthor", String.valueOf(sp.getUser().getVipid())),
                        new OkHttpClientManager.Param("tdcontent", content),
                        new OkHttpClientManager.Param("tname", "shangjie"),
                        new OkHttpClientManager.Param("img2", base64Img.get(0)),
                        new OkHttpClientManager.Param("img3", base64Img.get(1)),
                        new OkHttpClientManager.Param("img4", base64Img.get(2)),
                        new OkHttpClientManager.Param("img5", base64Img.get(3)),
                        new OkHttpClientManager.Param("img6", base64Img.get(4))
                };
                break;
            case 6:
                params = new OkHttpClientManager.Param[]{
                        new OkHttpClientManager.Param("ttype", String.valueOf(type)),
                        new OkHttpClientManager.Param("tauthor", String.valueOf(sp.getUser().getVipid())),
                        new OkHttpClientManager.Param("tdcontent", content),
                        new OkHttpClientManager.Param("tname", "shangjie"),
                        new OkHttpClientManager.Param("img2", base64Img.get(0)),
                        new OkHttpClientManager.Param("img3", base64Img.get(1)),
                        new OkHttpClientManager.Param("img4", base64Img.get(2)),
                        new OkHttpClientManager.Param("img5", base64Img.get(3)),
                        new OkHttpClientManager.Param("img6", base64Img.get(4)),
                        new OkHttpClientManager.Param("img7", base64Img.get(5))
                };
                break;
        }
        return params;
    }

}
