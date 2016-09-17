package com.lanma.lostandfound.activities;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.lanma.lostandfound.R;
import com.lanma.lostandfound.dialog.LoadingDialog;
import com.lanma.lostandfound.utils.ImageViewTintUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class SelectPictureActivity extends BaseActivity {
    @Bind(R.id.selectPictureOk)
    Button mSelectPictureOk;//确定
    @Bind(R.id.selectPictureGridView)
    GridView mSelectPictureGridView;//展示图片
    @Bind(R.id.selectPicture)
    Button mSelectPicture;//选择图片文件夹
    @Bind(R.id.selectPictureListView)
    ListView mSelectPictureListView;//图片文件夹列表

    private static int MAX_NUM = 3;//最多选择图片的个数
    private static final int TAKE_PICTURE = 520;

    public static final String INTENT_SELECTED_PICTURE = "intent_selected_picture";

    private PictureAdapter pictureAdapter;//图片Adapter
    /**
     * 临时的辅助类，用于防止同一个文件夹的多次扫描
     */
    private HashMap<String, Integer> tmpDir = new HashMap<>();
    private ArrayList<ImageFolder> mDirPaths = new ArrayList<>();

    private ContentResolver mContentResolver;
    private FolderAdapter folderAdapter;//文件夹Adapter
    private ImageFolder allImageFolder, currentImageFolder;
    private ArrayList<String> selectedPicture = new ArrayList<>();// 已选择的图片
    private String cameraPath = null;
    private LoadingDialog mDialog;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                mDialog.dismiss();
                folderAdapter.notifyDataSetChanged();
                pictureAdapter.notifyDataSetChanged();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_picture);
        ButterKnife.bind(this);
        ImageViewTintUtil.setImageViewTint((ImageView) findViewById(R.id.selectPictureBack));
        getSwipeBackLayout().setEnableGesture(true);
        initData();
    }

    private void initData() {
        mDialog = new LoadingDialog(this);
        //初始化ContentResolver,用来获取系统图库中的图片
        mContentResolver = getContentResolver();
        Intent intent = getIntent();
        selectedPicture = intent.getStringArrayListExtra("ImageList");
        //删除掉数据为null的位置,保留有数据的位置
        for (int i = 0; i < selectedPicture.size(); i++) {
            if (TextUtils.isEmpty(selectedPicture.get(i))) {
                selectedPicture.remove(i);
            }
        }
        //初始化图片文件夹
        allImageFolder = new ImageFolder();
        allImageFolder.setDir("/所有图片");
        currentImageFolder = allImageFolder;
        mDirPaths.add(allImageFolder);
        //当前已选中了几张图片
        mSelectPictureOk.setText("完成" + selectedPicture.size() + "/" + MAX_NUM);
        //图片Adapter
        pictureAdapter = new PictureAdapter();
        mSelectPictureGridView.setAdapter(pictureAdapter);
        //图片文件夹Adapter
        folderAdapter = new FolderAdapter();
        mSelectPictureListView.setAdapter(folderAdapter);
        mDialog.show();
        //扫描图片相当耗时,需放到子线程中进行,不然会造成界面卡顿,并且可能会引起ANR
        new Thread(new Runnable() {
            @Override
            public void run() {
                //获取图片缩略图及图片路径
                getThumbnail();
            }
        }).start();
    }

    private void showListAnimation() {
        TranslateAnimation ta = new TranslateAnimation(1, 0f, 1, 0f, 1, 1f, 1, 0f);
        ta.setDuration(200);
        mSelectPictureListView.startAnimation(ta);
    }

    private void hideListAnimation() {
        TranslateAnimation ta = new TranslateAnimation(1, 0f, 1, 0f, 1, 0f, 1, 1f);
        ta.setDuration(200);
        mSelectPictureListView.startAnimation(ta);
        ta.setAnimationListener(new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mSelectPictureListView.setVisibility(View.GONE);
            }
        });
    }


    @OnItemClick({R.id.selectPictureGridView, R.id.selectPictureListView})
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {
            case R.id.selectPictureGridView:
                if (position == 0) {
                    goCamera();
                }
                break;
            case R.id.selectPictureListView:
                currentImageFolder = mDirPaths.get(position);
                hideListAnimation();
                pictureAdapter.notifyDataSetChanged();
                mSelectPicture.setText(currentImageFolder.getName());
                break;
        }
    }

    /**
     * 使用相机拍照
     */
    protected void goCamera() {
        if (selectedPicture.size() + 1 > MAX_NUM) {
            showSnackBar("最多选择" + MAX_NUM + "张");
            return;
        }
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri imageUri = getOutputMediaFileUri();
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    /**
     * 用于拍照时获取输出的Uri
     */
    protected Uri getOutputMediaFileUri() {
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Night");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
        cameraPath = mediaFile.getAbsolutePath();
        return Uri.fromFile(mediaFile);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && cameraPath != null) {
            selectedPicture.add(cameraPath);
            Intent data2 = new Intent();
            data2.putExtra(INTENT_SELECTED_PICTURE, selectedPicture);
            setResult(RESULT_OK, data2);
            this.finish();
        }
    }


    @OnClick({R.id.selectPictureBack, R.id.selectPictureOk, R.id.selectPicture})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.selectPictureBack:
                finish();
                break;
            case R.id.selectPictureOk:
                Intent data = new Intent();
                data.putExtra(INTENT_SELECTED_PICTURE, selectedPicture);
                setResult(RESULT_OK, data);
                this.finish();
                break;
            case R.id.selectPicture:
                if (mSelectPictureListView.getVisibility() == View.VISIBLE) {
                    hideListAnimation();
                } else {
                    mSelectPictureListView.setVisibility(View.VISIBLE);
                    showListAnimation();
                    folderAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    class PictureAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return currentImageFolder.images.size() + 1;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(SelectPictureActivity.this, R.layout.grid_item_picture, null);
                holder = new ViewHolder();
                holder.iv = (ImageView) convertView.findViewById(R.id.iv);
                holder.checkBox = (Button) convertView.findViewById(R.id.check);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (position == 0) {
                holder.iv.setImageResource(R.drawable.pickphotos_to_camera_normal);
                holder.checkBox.setVisibility(View.INVISIBLE);
            } else {
                position = position - 1;
                holder.checkBox.setVisibility(View.VISIBLE);
                final ImageItem item = currentImageFolder.images.get(position);
                Glide.with(SelectPictureActivity.this).load("file://" + item.path).into(holder.iv);
                boolean isSelected = selectedPicture.contains(item.path);
                holder.checkBox.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!v.isSelected() && selectedPicture.size() + 1 > MAX_NUM) {
                            showSnackBar("最多选择" + MAX_NUM + "张");
                            return;
                        }
                        if (selectedPicture.contains(item.path)) {
                            selectedPicture.remove(item.path);
                        } else {
                            selectedPicture.add(item.path);
                        }
                        mSelectPictureOk.setEnabled(selectedPicture.size() > 0);
                        mSelectPictureOk.setText("完成" + selectedPicture.size() + "/" + MAX_NUM);
                        v.setSelected(selectedPicture.contains(item.path));
                    }
                });
                holder.checkBox.setSelected(isSelected);
            }
            return convertView;
        }
    }

    class ViewHolder {
        ImageView iv;
        Button checkBox;
    }

    class FolderAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mDirPaths.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            FolderViewHolder holder = null;
            if (convertView == null) {
                convertView = View.inflate(SelectPictureActivity.this, R.layout.list_dir_item, null);
                holder = new FolderViewHolder();
                holder.id_dir_item_image = (ImageView) convertView.findViewById(R.id.id_dir_item_image);
                holder.id_dir_item_name = (TextView) convertView.findViewById(R.id.id_dir_item_name);
                holder.id_dir_item_count = (TextView) convertView.findViewById(R.id.id_dir_item_count);
                holder.choose = (ImageView) convertView.findViewById(R.id.choose);
                convertView.setTag(holder);
            } else {
                holder = (FolderViewHolder) convertView.getTag();
            }
            ImageFolder item = mDirPaths.get(position);
            Glide.with(SelectPictureActivity.this).load("file://" + item.getFirstImagePath()).into(holder.id_dir_item_image);
            holder.id_dir_item_count.setText(item.images.size() + "张");
            holder.id_dir_item_name.setText(item.getName());
            holder.choose.setVisibility(currentImageFolder == item ? View.VISIBLE : View.GONE);
            return convertView;
        }
    }

    class FolderViewHolder {
        ImageView id_dir_item_image;
        ImageView choose;
        TextView id_dir_item_name;
        TextView id_dir_item_count;
    }

    /**
     * 得到缩略图
     */
    private void getThumbnail() {
        Cursor mCursor = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.ImageColumns.DATA}, "", null,
                MediaStore.MediaColumns.DATE_ADDED + " DESC");
        if (null != mCursor && mCursor.moveToFirst()) {
            int _date = mCursor.getColumnIndex(MediaStore.Images.Media.DATA);
            do {
                // 获取图片的路径
                String path = mCursor.getString(_date);
                //为空的路径不要
                if (null == path) {
                    continue;
                }
                //过滤掉不是File的文件和字节大小为0的无效图片,
                File file = new File(path);
                if (file.isFile()) {
                    if (file.length() == 0) {
                        continue;
                    }
                } else {
                    continue;
                }
                //添加图片路径到集合
                allImageFolder.images.add(new ImageItem(path));
                // 获取该图片的父路径名
                File parentFile = new File(path).getParentFile();
                //没有父路径的不要
                if (parentFile == null) {
                    continue;
                }
                ImageFolder imageFolder = null;
                String dirPath = parentFile.getAbsolutePath();
                //防止同一个文件夹的多次扫描
                if (!tmpDir.containsKey(dirPath)) {
                    // 初始化ImageFolder
                    imageFolder = new ImageFolder();
                    imageFolder.setDir(dirPath);
                    imageFolder.setFirstImagePath(path);
                    mDirPaths.add(imageFolder);
                    tmpDir.put(dirPath, mDirPaths.indexOf(imageFolder));
                } else {
                    imageFolder = mDirPaths.get(tmpDir.get(dirPath));
                }
                //添加文件夹路径
                imageFolder.images.add(new ImageItem(path));
            } while (mCursor.moveToNext());
            mCursor.close();
        }
        tmpDir = null;
        Message message = new Message();
        message.what = 0;
        mHandler.sendMessage(message);
    }

    class ImageFolder {
        private String dir;//图片的文件夹路径
        private String firstImagePath;//第一张图片的路径
        private String name;//文件夹的名称
        public List<ImageItem> images = new ArrayList<ImageItem>();

        public String getDir() {
            return dir;
        }

        public void setDir(String dir) {
            this.dir = dir;
            int lastIndexOf = this.dir.lastIndexOf("/");
            this.name = this.dir.substring(lastIndexOf);
        }

        public String getFirstImagePath() {
            return firstImagePath;
        }

        public void setFirstImagePath(String firstImagePath) {
            this.firstImagePath = firstImagePath;
        }

        public String getName() {
            return name;
        }

    }

    class ImageItem {
        String path;

        public ImageItem(String p) {
            this.path = p;
        }
    }

}
