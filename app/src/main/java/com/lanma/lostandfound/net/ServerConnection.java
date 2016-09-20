package com.lanma.lostandfound.net;

import android.content.Context;
import android.text.TextUtils;

import com.lanma.lostandfound.beans.LostFoundInfo;
import com.lanma.lostandfound.beans.MessageInfo;
import com.lanma.lostandfound.beans.StudentInfo;
import com.lanma.lostandfound.beans.SuggestInfo;
import com.lanma.lostandfound.presenter.DeleteCurrentInfoPresenter;
import com.lanma.lostandfound.presenter.GetStudentInfoPresenter;
import com.lanma.lostandfound.presenter.LoginPresenter;
import com.lanma.lostandfound.presenter.LostInfoListPresenter;
import com.lanma.lostandfound.presenter.MessageInfoPresenter;
import com.lanma.lostandfound.presenter.MyLostAndFoundPresenter;
import com.lanma.lostandfound.presenter.RegisterPresenter;
import com.lanma.lostandfound.presenter.ReleaseInfoPresenter;
import com.lanma.lostandfound.presenter.SuggestionPresenter;
import com.lanma.lostandfound.presenter.UpdateStudentInfoPresenter;
import com.lanma.lostandfound.presenter.UploadHeadImagePresenter;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadBatchListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * 作者 任强强 on 2016/9/1 20:45.
 */
public class ServerConnection {

    private ServerConnection() {
    }

    /**
     * 登陆
     */
    public static void LoginAction(String userName, String password, final LoginPresenter presenter) {
        StudentInfo studentInfo = new StudentInfo();
        studentInfo.setUsername(userName);
        studentInfo.setPassword(password);
        presenter.requestStart();
        studentInfo.login(new SaveListener<StudentInfo>() {
            @Override
            public void done(StudentInfo bmobUser, BmobException e) {
                if (null == e) {
                    presenter.LoginSuccess();
                } else {
                    presenter.LoginFailure("登陆失败");
                    Logger.e(e.toString());
                }
            }
        });
    }

    /**
     * 注册
     */
    public static void RegisterAction(String userName, String password, final RegisterPresenter presenter) {
        StudentInfo studentInfo = new StudentInfo();
        studentInfo.setUsername(userName);
        studentInfo.setPassword(password);
        studentInfo.setEmail(userName);
        presenter.requestStart();
        studentInfo.signUp(new SaveListener<StudentInfo>() {
            @Override
            public void done(StudentInfo bmobUser, BmobException e) {
                if (null == e) {
                    presenter.registerSuccess();
                } else {
                    if (202 == e.getErrorCode()) {
                        presenter.registerFailure("该邮箱已被注册");
                    } else {
                        presenter.registerFailure("注册失败");
                    }
                    Logger.e(e.toString());
                }
            }
        });
    }

    /**
     * 查询用户信息
     *
     * @param username 用户注册时用的用户名,即邮箱
     */
    public static void getStudentInfo(String username, final GetStudentInfoPresenter presenter) {
        BmobQuery<StudentInfo> query = new BmobQuery<>();
        query.addWhereEqualTo("username", username);
        presenter.requestStart();
        query.findObjects(new FindListener<StudentInfo>() {
            @Override
            public void done(List<StudentInfo> list, BmobException e) {
                if (e == null) {
                    presenter.getStudentInfoSuccess(list.get(0));
                } else {
                    presenter.getStudentInfoFailure("查询失败,请稍后重试");
                }
            }
        });
    }

    /**
     * 更新用户信息
     */
    public static void UpdateStudentInfo(String objectId, String studentName, String studentAge, String studentSex,
                                         String studentEmailAddress, String studentPhoneNumber, String studentYear,
                                         String studentCollege, String studentSpecialities, String studentApartmentNumber,
                                         final UpdateStudentInfoPresenter presenter) {
        StudentInfo newUser = new StudentInfo();
        newUser.setStudentName(studentName);
        newUser.setStudentAge(studentAge);
        newUser.setStudentSex(studentSex);
        newUser.setEmail(studentEmailAddress);
        if (!TextUtils.isEmpty(studentPhoneNumber)) {
            if (!studentPhoneNumber.equals(BmobUser.getCurrentUser().getMobilePhoneNumber())) {
                newUser.setMobilePhoneNumber(studentPhoneNumber);
            }
        }
        newUser.setStudentYear(studentYear);
        newUser.setStudentOfCollege(studentCollege);
        newUser.setStudentSpecialities(studentSpecialities);
        newUser.setStudentApartNumber(studentApartmentNumber);
        presenter.requestStart();
        newUser.update(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    presenter.updateStudentInfoSuccess();
                } else {
                    presenter.updateStudentInfoFailure(e.getErrorCode(), "更新个人信息失败");
                }
            }
        });
    }

    /**
     * 上传头像
     */
    public static void uploadHeadImage(File file, final UploadHeadImagePresenter presenter) {
        final BmobFile bmobFile = new BmobFile(file);
        presenter.requestStart();
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    setHeadImageUrlToStudentInfo(bmobFile.getFileUrl(), presenter);
                } else {
                    presenter.uploadHeadImageFailure("头像上传失败");
                }
            }
        });
    }

    /**
     * 设置头像地址到StudentInfo
     */
    private static void setHeadImageUrlToStudentInfo(String imageUrl, final UploadHeadImagePresenter presenter) {
        StudentInfo newUser = new StudentInfo();
        newUser.setStudentHeadImage(imageUrl);
        presenter.requestStart();
        newUser.update(BmobUser.getCurrentUser().getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    presenter.uploadHeadImageSuccess();
                } else {
                    presenter.uploadHeadImageFailure("头像上传失败");
                }
            }
        });
    }


    /**
     * 获取"失物"信息列表
     */
    public static void getLostFoundInfoList(String lostInfoType, final int CurrentListSize, final LostInfoListPresenter presenter) {
        BmobQuery<LostFoundInfo> query = new BmobQuery<>();
        query.addWhereEqualTo("infoType", lostInfoType);//查询信息类型为"失物"类型的所有数据
        query.order("-createdAt");//最近发布的信息排在最上面(按发布时间由近到远排序)
        query.setLimit(10); // 限制最多10条数据结果作为一页
        query.setSkip(CurrentListSize); //分页 查询多少条数据之后的数据
        query.include("studentInfo");//同时返回关联表中的数据(_User表中的数据)
        presenter.requestStart();
        query.findObjects(new FindListener<LostFoundInfo>() {
            @Override
            public void done(List<LostFoundInfo> list, BmobException e) {
                if (e == null) {
                    if (CurrentListSize == 0) {
                        presenter.onRefreshLostInfoListSuccess(list);
                    } else {
                        presenter.onLoadMoreLostInfoListSuccess(list);
                    }
                } else {
                    if (CurrentListSize == 0) {
                        presenter.onRefreshLostInfoListFailure("数据刷新失败");
                    } else {
                        presenter.onLoadMoreLostInfoListFailure("数据加载失败");
                    }
                }
            }
        });
    }

    /**
     * 发布"失物"或"招领"信息
     */
    public static void ReleaseLostInfo(Context context, String lostInfoType, String lostInfoDesc, String lostThingType, final ArrayList<String> list,
                                       String lostInfoWhere, String lostInfoThanksWay, String lostInfoPhoneNumber,
                                       String lostInfoDescDetail, StudentInfo studentInfo, final ReleaseInfoPresenter presenter) {
        //有图片,先上传图片
        if (null != list && list.size() > 0) {
            uploadThingImage(context, lostInfoType, lostInfoDesc, lostThingType, list, lostInfoWhere, lostInfoThanksWay, lostInfoPhoneNumber,
                    lostInfoDescDetail, studentInfo, presenter);
        }
        //没有图片,直接发布
        else {
            releaseLostInfo(lostInfoType, lostInfoDesc, lostThingType, null, lostInfoWhere, lostInfoThanksWay, lostInfoPhoneNumber,
                    lostInfoDescDetail, studentInfo, presenter);
        }
    }

    /**
     * 上传物品图片(1张-3张)
     */
    private static void uploadThingImage(Context context, final String lostInfoType, final String lostInfoDesc, final String lostThingType, final ArrayList<String> mList,
                                         final String lostInfoWhere, final String lostInfoThanksWay, final String lostInfoPhoneNumber,
                                         final String lostInfoDescDetail, final StudentInfo studentInfo, final ReleaseInfoPresenter presenter) {
//        List<String> newImageList = new ArrayList<>();
//        BitmapFactory.Options option = new BitmapFactory.Options();
//        option.inSampleSize = 4;
//        /*不进行图片抖动处理*/
//        option.inDither = false;
//        /*设置让解码器以最佳方式解码*/
//        option.inPreferredConfig = null;
//        /* 下面两个字段需要组合使用 */
//        option.inPurgeable = true;
//        option.inInputShareable = true;
//        //生成的bitmap可变
//        option.inMutable = true;
//        for (String path : mList) {
//            Bitmap bitmap = BitmapFactory.decodeFile(path, option);
//            File fileFromBitmap = ImageUtils.getFileFromBitmap(context, bitmap);
//            if (null != fileFromBitmap) {
//                newImageList.add(fileFromBitmap.getPath());
//            }
//        }
        //过滤无效数据,保留有效数据
        List<String> list = new ArrayList<>();
        for (String imagePath : mList) {
            if (!TextUtils.isEmpty(imagePath)) {
                list.add(imagePath);
            }
        }
        final String[] imageLocalPath = list.toArray(new String[list.size()]);
        presenter.requestStart();
        BmobFile.uploadBatch(imageLocalPath, new UploadBatchListener() {
                    @Override
                    public void onSuccess(List<BmobFile> list, List<String> urlList) {
                        //图片全部上传成功之后把图片url地址保存到UpLoadImageInfo表中
                        if (null != urlList) {
                            if (urlList.size() == imageLocalPath.length) {
                                StringBuilder sb = new StringBuilder();
                                for (int i = 0; i < urlList.size(); i++) {
                                    if (i != urlList.size() - 1) {
                                        sb.append(urlList.get(i)).append(",");
                                    } else {
                                        sb.append(urlList.get(i));
                                    }
                                }
                                releaseLostInfo(lostInfoType, lostInfoDesc, lostThingType, sb.toString(), lostInfoWhere, lostInfoThanksWay,
                                        lostInfoPhoneNumber, lostInfoDescDetail, studentInfo, presenter);
                            }
                        }
                    }

                    @Override
                    public void onProgress(int i, int i1, int i2, int i3) {

                    }

                    @Override
                    public void onError(int i, String s) {
                        presenter.releaseFailure("物品图片上传失败");
                    }
                }

        );
    }

    /**
     * 发布"失物"或"招领"信息
     */
    private static void releaseLostInfo(String lostInfoType, String lostInfoDesc, String lostThingType, final String thingImagesUrl,
                                        String lostInfoWhere, String lostInfoThanksWay, String lostInfoPhoneNumber,
                                        String lostInfoDescDetail, StudentInfo studentInfo, final ReleaseInfoPresenter presenter) {
        LostFoundInfo info = new LostFoundInfo();
        info.setInfoType(lostInfoType);
        info.setThingDesc(lostInfoDesc);
        info.setThingType(lostThingType);
        info.setThingWhereFound(lostInfoWhere);
        info.setStudentThanksWay(lostInfoThanksWay);
        info.setStudentPhoneNumber(lostInfoPhoneNumber);
        info.setThingMark(lostInfoDescDetail);
        info.setThingImageUrl(thingImagesUrl);
        info.setStudentInfo(studentInfo);
        //如果没有图片url地址,则直接发布,需要添加presenter.requestStart();这个方法
        if (TextUtils.isEmpty(thingImagesUrl)) {
            presenter.requestStart();
        }
        info.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    presenter.releaseSuccessful();
                } else {
                    presenter.releaseFailure("信息发布失败");
                }
            }
        });
    }


    /**
     * 提交反馈建议
     */
    public static void submitSuggestion(String suggest, StudentInfo currentUser, final SuggestionPresenter presenter) {
        SuggestInfo suggestInfo = new SuggestInfo();
        suggestInfo.setSuggestContent(suggest);
        suggestInfo.setStudentInfo(currentUser);
        presenter.requestStart();
        suggestInfo.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (null == e) {
                    presenter.SuggestSuccessful();
                } else {
                    presenter.SuggestFailure("提交失败,再试试?");
                }
            }
        });
    }

    /**
     * 获取消息列表
     */
    public static void getMessageInfo(final MessageInfoPresenter presenter) {
        BmobQuery<MessageInfo> query = new BmobQuery<>();
        query.order("-createdAt");//最近发布的信息排在最上面(按发布时间由近到远排序)
        presenter.requestStart();
        query.findObjects(new FindListener<MessageInfo>() {
            @Override
            public void done(List<MessageInfo> list, BmobException e) {
                if (e == null) {
                    presenter.getMessageInfoSuccess(list);
                } else {
                    presenter.getMessageInfoFailure("消息获取失败");
                }
            }
        });
    }

    /**
     * 获取“我”发布的失物招领信息列表
     */
    public static void getMyLostAndFoundInfo(StudentInfo currentUser, final int currentListSize, String infoType, final MyLostAndFoundPresenter presenter) {
        BmobQuery<LostFoundInfo> query = new BmobQuery<>();
        query.addWhereEqualTo("studentInfo", currentUser);//查询当前用户
        query.addWhereEqualTo("infoType", infoType);//查询信息类型为"失物"或”招领“类型的所有数据
        query.order("-createdAt");//最近发布的信息排在最上面(按发布时间由近到远排序)
        query.setLimit(10); // 限制最多10条数据结果作为一页
        query.setSkip(currentListSize); //分页 查询多少条数据之后的数据
        presenter.requestStart();
        query.findObjects(new FindListener<LostFoundInfo>() {
            @Override
            public void done(List<LostFoundInfo> list, BmobException e) {
                if (e == null) {
                    if (currentListSize == 0) {
                        presenter.onRefreshMyLostAndFoundListSuccess(list);
                    } else {
                        presenter.onLoadMoreMyLostAndFoundListSuccess(list);
                    }
                } else {
                    if (currentListSize == 0) {
                        presenter.onRefreshMyLostAndFoundListFailure("刷新数据失败");
                    } else {
                        presenter.onLoadMoreMyLostAndFoundListFailure("加载数据失败");
                    }
                }
            }
        });
    }

    /**
     * 删除一条“失物”或“招领”信息
     */
    public static void deleteCurrentInfo(String objectId, final int position, final DeleteCurrentInfoPresenter presenter) {
        LostFoundInfo info = new LostFoundInfo();
        presenter.onDeleting();
        info.delete(objectId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (null == e) {
                    presenter.onDeleteSuccess(position);
                } else {
                    presenter.onDeleteFailure("删除失败,请稍后重试");
                }
            }
        });
    }
}
