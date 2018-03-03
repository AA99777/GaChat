package com.example.rui.gachat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.rui.gachat.bean.Sendmsg;
import com.example.rui.gachat.dao.LoadChatLog;
import com.example.rui.gachat.model.ChatLog;
import com.example.rui.gachat.model.FriendList;
import com.example.rui.gachat.model.MyUser;
import com.example.rui.gachat.utils.StaticCode;
import com.foamtrace.photopicker.ImageCaptureManager;
import com.foamtrace.photopicker.PhotoPickerActivity;
import com.foamtrace.photopicker.PhotoPreviewActivity;
import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.ConversationListener;
import cn.bmob.v3.exception.BmobException;

/**
 * Created by Rui on 2017/12/16.
 */

public class ChatingActivity extends AppCompatActivity implements View.OnClickListener {

    public static ChatRecycleviewAdapter crva;
    public static RecyclerView recyclerView;

    boolean isOpenConversation = false;
    BmobIMConversation mBmobIMConversation;
    FriendList friendList;
    public static ArrayList<ChatLog> chatlist = new ArrayList<ChatLog>();
    EditText et_mycontent;
    Button btn_send;
    ImageView iv_sendimg;
    private ImageCaptureManager captureManager; // 相机拍照处理类
    private ArrayList<String> imagePaths = null;

    public static enum ITEM_TYPE {
        ITEM_TYPE_TXT(0), ITEM_TYPE_IMAGE(1), ITEM_TYPE_TXTB(2), ITEM_TYPE_IMAGEB(3);
        private int iNum = 0;

        private ITEM_TYPE(int iNum) {
            this.iNum = iNum;
        }

        public int toNumber() {
            return this.iNum;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatingview);
        getChatLog();
        initView();
    }

    private void initView() {

        recyclerView = (RecyclerView) findViewById(R.id.mychatingview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(
                        getBaseContext(), LinearLayoutManager.VERTICAL, false
                )
        );
        crva = new ChatRecycleviewAdapter();
        recyclerView.setAdapter(crva);

        et_mycontent = (EditText) findViewById(R.id.edit_content);

        btn_send = (Button) findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);
        iv_sendimg = (ImageView) findViewById(R.id.iv_sendimg);
        iv_sendimg.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                sendm(StaticCode.MSGTYPE_TXT, et_mycontent.getText().toString());
                break;
            case R.id.iv_sendimg:
                PhotoPickerIntent intent1 = new PhotoPickerIntent(ChatingActivity.this);
                intent1.setSelectModel(SelectModel.MULTI);
                intent1.setShowCarema(true); // 是否显示拍照
                intent1.setMaxTotal(9); // 最多选择照片数量，默认为9
                intent1.setSelectedPaths(imagePaths); // 已选中的照片地址， 用于回显选中状态
                startActivityForResult(intent1, StaticCode.REQUEST_CAMERA_CODE);
                sendm(StaticCode.MSGTYPE_IMG, "/storage/emulated/0/Quark/Download/timg(1).jpg");
        }
    }

    /**
     * 返回选择的本地照片路径到集合中，然后遍历发送照片
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
      ArrayList<String>  aa=null;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 选择照片
                case StaticCode.REQUEST_CAMERA_CODE:
                    aa= (ArrayList<String> )data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
                    break;

            }
        }
        for (String i:aa){
            sendm(StaticCode.MSGTYPE_IMG,i);
        }

    }
    /**
     * 获取聊天记录
     */
    public void getChatLog() {
        Intent intent = getIntent();
        MainActivity.myUser = (MyUser) intent.getSerializableExtra("myUser");
        friendList = (FriendList) intent.getSerializableExtra("chatUser");
        chatlist = LoadChatLog.saveChatLog(MainActivity.myUser.getUsername(), friendList.getFriendName());
    }

    /**
     * 发送聊天消息
     */
    public void sendm(final int msgType, final String content) {
        if (!isOpenConversation) {
            BmobIMUserInfo info = new BmobIMUserInfo();
            info.setAvatar("填写接收者的头像");
            info.setUserId(friendList.getFriendName());
            info.setName("填写接收者的名字");
            BmobIM.getInstance().startPrivateConversation(info, new ConversationListener() {
                @Override
                public void done(BmobIMConversation c, BmobException e) {
                    if (e == null) {
                        isOpenConversation = true;
                        //在此跳转到聊天页面或者直接转化
                        mBmobIMConversation = BmobIMConversation.obtain(BmobIMClient.getInstance(), c);
                        Sendmsg.sendmsg(mBmobIMConversation, msgType, content, friendList.getFriendName());
                    } else {
                        Toast.makeText(ChatingActivity.this, "开启会话出错", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Sendmsg.sendmsg(mBmobIMConversation, msgType, content, friendList.getFriendName());
        }
    }


    public class ChatRecycleviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

            //判断当前聊天记录发送人根据属性（本用户或者会话好友）返回显示样式
            if (viewType == ITEM_TYPE.ITEM_TYPE_TXT.toNumber()) {
                return new SenderHolder(LayoutInflater.from(getBaseContext()).inflate(R.layout.chatitemlistsender, parent, false));
            } else if (viewType == ITEM_TYPE.ITEM_TYPE_IMAGE.toNumber()) {
                return new IMGSenderHolder(LayoutInflater.from(getBaseContext()).inflate(R.layout.chatitemlistsender_img, parent, false));
            } else if (viewType == ITEM_TYPE.ITEM_TYPE_TXTB.toNumber()) {
                return new ReceiverHolder(LayoutInflater.from(getBaseContext()).inflate(R.layout.chatitemrecevier, parent, false));
            } else if (viewType == ITEM_TYPE.ITEM_TYPE_IMAGEB.toNumber()) {
                return new IMGReceiverHolder(LayoutInflater.from(getBaseContext()).inflate(R.layout.chatitemrecevier_img, parent, false));
            } else {
                return new SenderHolder(LayoutInflater.from(getBaseContext()).inflate(R.layout.chatitemlistsender, parent, false));
            }

        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            //根据返回的属性判断发送者还是接收者
            if (holder instanceof SenderHolder) {

                ((SenderHolder) holder).text.setText(chatlist.get(position).getContent()+" :发送者" );
            } else if (holder instanceof IMGSenderHolder) {
                //((IMGSenderHolder) holder).chativ.setImageDrawable(getResources().getDrawable(R.drawable.ic_third));
                Uri uri = Uri.parse(chatlist.get(position).getContent());
                File file = new File(chatlist.get(position).getContent());
                Glide.with(ChatingActivity.this)
                        .load(file)
                        .override(100, 100)
                        .into(((IMGSenderHolder) holder).chativ);

            } else if (holder instanceof ReceiverHolder) {
                //设置发送内容为文本
                ((ReceiverHolder) holder).text2.setText("接收者: " + chatlist.get(position).getContent());
            } else if (holder instanceof IMGReceiverHolder) {
                Uri uri = Uri.parse(chatlist.get(position).getContent());
                Glide.with(ChatingActivity.this)
                        .load(uri)
                        .override(100, 100)
                        .into(((IMGReceiverHolder) holder).chativ2);
                // ((IMGReceiverHolder) holder).chativ2.setImageDrawable(getResources().getDrawable(R.drawable.ic_third));
            }

        }

        @Override
        public int getItemCount() {
            return chatlist.size();
        }

        public void addData() {
            //添加消息并刷新
            //如果是集合则必须将数据能容添加到集合再刷新同步rv
            notifyItemInserted(chatlist.size());
            // ToastUtils.toast(ChatingActivity.this, "sss" + chatlist.size());
        }

        @Override
        public int getItemViewType(int position) {

            if (chatlist.get(position).getChatIdName() == StaticCode.SENDER_CODE) {
                if (chatlist.get(position).getMsgtype() == StaticCode.MSGTYPE_TXT) {
                    return ITEM_TYPE.ITEM_TYPE_TXT.toNumber();
                } else {
                    return ITEM_TYPE.ITEM_TYPE_IMAGE.toNumber();
                }
            } else {
                if (chatlist.get(position).getMsgtype() == StaticCode.MSGTYPE_TXT) {
                    return ITEM_TYPE.ITEM_TYPE_TXTB.toNumber();
                } else {
                    return ITEM_TYPE.ITEM_TYPE_IMAGEB.toNumber();
                }
            }
        }

        //用户Item
        class SenderHolder extends RecyclerView.ViewHolder {
            static final String TAG = "SenderHolder";
            LinearLayout mlinearLayout0;
            TextView text;
            ImageView imv0;
            SenderHolder(View itemView) {
                super(itemView);
                mlinearLayout0 = (LinearLayout) itemView.findViewById(R.id.cil0);
                text = (TextView) itemView.findViewById(R.id.chattv);
                imv0=(ImageView)itemView.findViewById(R.id.imv0);
                Glide.with(ChatingActivity.this)
                        .load(MainActivity.myUser.getNick_URL_NET())
                        .error(getResources().getDrawable(R.mipmap.ic_launcher))
                        .fitCenter()
                        .into(imv0);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "SenderHolder" + getPosition());
                    }
                });
            }
        }


        //用户imgItem
        class IMGSenderHolder extends RecyclerView.ViewHolder {
            static final String TAG = "imgSenderHolder";
            LinearLayout mlinearLayout1;
            ImageView chativ;
            ImageView imv1;
            IMGSenderHolder(View itemView) {
                super(itemView);
                mlinearLayout1 = (LinearLayout) itemView.findViewById(R.id.cil1);
                imv1=(ImageView)itemView.findViewById(R.id.imv1);
                Glide.with(ChatingActivity.this)
                        .load(MainActivity.myUser.getNick_URL_NET())
                        .error(getResources().getDrawable(R.mipmap.ic_launcher))
                        .fitCenter()
                        .into(imv1);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "SenderHolder" + getPosition());
                    }
                });
                chativ = (ImageView) itemView.findViewById(R.id.chativ);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "IMGSenderHolder" + getPosition());
                    }
                });
            }
        }

        //会话好友Item
        class ReceiverHolder extends RecyclerView.ViewHolder {

            static final String TAG = "ReceiverHolder";
            LinearLayout mlinearLayout2;
            TextView text2;
            ImageView imv2;
            ReceiverHolder(View itemView) {
                super(itemView);
                mlinearLayout2 = (LinearLayout) itemView.findViewById(R.id.cil2);
                text2 = (TextView) itemView.findViewById(R.id.chattv2);
                imv2=(ImageView)itemView.findViewById(R.id.imv2);
                Glide.with(ChatingActivity.this)
                        .load(friendList.getFriendIcon())
                        .error(getResources().getDrawable(R.mipmap.ic_launcher))
                        .fitCenter()
                        .into(imv2);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "ReceiverHolder" + getPosition());
                    }
                });
            }
        }

        //会话好友imgItem
        class IMGReceiverHolder extends RecyclerView.ViewHolder {
            static final String TAG = "ReceiverHolder";
            LinearLayout mlinearLayout3;
            ImageView chativ2;
            ImageView imv3;
            IMGReceiverHolder(View itemView) {
                super(itemView);
                mlinearLayout3 = (LinearLayout) itemView.findViewById(R.id.cil3);
                chativ2 = (ImageView) itemView.findViewById(R.id.chativ2);
                imv3=(ImageView)itemView.findViewById(R.id.imv3);
                Glide.with(ChatingActivity.this)
                        .load(friendList.getFriendIcon())
                        .error(getResources().getDrawable(R.mipmap.ic_launcher))
                        .fitCenter()
                        .into(imv3);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "IMGReceiverHolder" + getPosition());
                    }
                });
            }
        }
    }
}