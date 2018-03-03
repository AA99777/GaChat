package com.example.rui.gachat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.rui.gachat.model.Friend;
import com.example.rui.gachat.model.FriendList;
import com.example.rui.gachat.model.MyUser;
import com.example.rui.gachat.utils.DatabaseHelper;
import com.example.rui.gachat.utils.ToastUtils;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;
import devlight.io.library.ntb.NavigationTabBar;

/**
 * Created by Rui on 2017/12/8.s
 */

public class MainActivity extends AppCompatActivity {
    List<FriendList> jiahaoyou;
    List<FriendList> msglist;
    public static MyUser myUser;
    public static String opfriendname = "";
    public static Gson gson = new Gson();
    DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
    public static SQLiteDatabase db = null;
    String[] aas = {"妙不可言", "雅月", "单行轨道", "风未末炎", "枫叶", "搁浅的鱼", "花好月圆", "古希腊女战士", "妙不可言", "雅月", "单行轨道", "风未末炎", "枫叶", "搁浅的鱼", "花好月圆", "古希腊女战士",
            "妙不可言", "雅月", "单行轨道", "风未末炎", "枫叶", "搁浅的鱼", "花好月圆", "古希腊女战士"};
    List<String> kkl=new ArrayList<String>();
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainview);
//        DatabaseHelper databaseHelper=new DatabaseHelper(MainActivity.this);
//        db=null;
//        db=databaseHelper.getReadableDatabase();
        getMyList();
        initUI();
//        upshuju();
//zhuce();
    }

    private void upshuju() {
        String [] iy={"http://bmob-cdn-15945.b0.upaiyun.com/2017/12/28/9dc5f71040aa1ce780cd9aaf0d601d9b.jpg",
                      "http://bmob-cdn-15945.b0.upaiyun.com/2017/12/28/b24d01ca409485f280f90d6d4f628826.jpg",
                      "http://bmob-cdn-15945.b0.upaiyun.com/2017/12/28/3373045940fea06b806ae37be5a00c0c.jpg",
                      "http://bmob-cdn-15945.b0.upaiyun.com/2017/12/28/abca07cf4043ef7680f22f1dc5251fb6.jpg",
                      "http://bmob-cdn-15945.b0.upaiyun.com/2017/12/28/eb2b887840dfc91e80ed004b9b87e988.jpg",
                      "http://bmob-cdn-15945.b0.upaiyun.com/2017/12/28/f8a575e640edc65980aad3844bab275f.jpg",
                      "http://bmob-cdn-15945.b0.upaiyun.com/2017/12/28/649b056a40e7cc8c804180844d680897.jpg",
                      "http://bmob-cdn-15945.b0.upaiyun.com/2017/12/28/8097c0644082347480626af6718032d5.jpg"};
        List<FriendList> msglist2 = new ArrayList<FriendList>();
        List<FriendList> haoyou = new ArrayList<FriendList>();
        for (int i = 0; i < 16; i++) {
            FriendList pp = new FriendList();
            String bb = "iiii" + i;
            pp.setFriendName(bb);
            pp.setFriendIcon(iy[i%8]);
            haoyou.add(pp);
            FriendList oo = new FriendList();
            String dd = "jjjj" + i;
            oo.setFriendName(dd);
            oo.setFriendIcon(iy[i%8]);
            msglist2.add(oo);

        }
        Friend friend = new Friend();
//注意：不能调用gameScore.setObjectId("")方法
        friend.setUser(myUser);
        friend.setFriends(haoyou);
        friend.setMsglist(msglist2);
        friend.save(new SaveListener<String>() {

            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    ToastUtils.toast(MainActivity.this, "创建数据成功：" + objectId);
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    public void shangchuan() {
        //详细示例可查看BmobExample工程中BmobFileActivity类
        final String[] fileaa = new String[8];
        for (int j = 0; j < j; j++) {
            fileaa[j] = "/storage/emulated/0/Quark/Download/img0" +j + ".jpg";}

            BmobFile.uploadBatch(fileaa, new UploadBatchListener() {

                @Override
                public void onSuccess(List<BmobFile> files, List<String> urls) {
                    //1、files-上传完成后的BmobFile集合，是为了方便大家对其上传后的数据进行操作，例如你可以将该文件保存到表中
                    //2、urls-上传文件的完整url地址
                    if (urls.size() == fileaa.length) {//如果数量相等，则代表文件全部上传完成
                        //do something
                        kkl=urls;
                        ToastUtils.toast(MainActivity.this,"上传完成");
                        zhuce();
                    }
                }

                @Override
                public void onError(int statuscode, String errormsg) {
                }

                @Override
                public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                    //1、curIndex--表示当前第几个文件正在上传
                    //2、curPercent--表示当前上传文件的进度值（百分比）
                    //3、total--表示总的上传文件数
                    //4、totalPercent--表示总的上传进度（百分比）
                }
            });

    }

    public void zhuce() {


        for (int i = 0; i < 24; i++) {
            MyUser myUser = new MyUser();
            myUser.setName(aas[i]);
            myUser.setUsername("llll" + i);
            myUser.setPassword("llll" + i);
            myUser.setNick_URL_NET("123");
            myUser.setSex(true);
            myUser.setAge(18);
            myUser.signUp(new SaveListener<MyUser>() {
                @Override
                public void done(MyUser s, BmobException e) {
                    if (e == null) {
                        Toast.makeText(MainActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                    } else {
                        //loge(e);
                        Toast.makeText(MainActivity.this, "成功", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    private void getMyList() {
        db = databaseHelper.getReadableDatabase();
        Intent intent = getIntent();
        myUser = (MyUser) intent.getSerializableExtra("myuser");
        if (intent.getBooleanExtra("newuser", false)) {
            jiahaoyou = new ArrayList<FriendList>();
            msglist = new ArrayList<FriendList>();
        } else {
//        Friend dh = (Friend) intent.getSerializableExtra("dd");
            jiahaoyou = (ArrayList<FriendList>) intent.getSerializableExtra("friendlist");
            msglist = (ArrayList<FriendList>) intent.getSerializableExtra("msglist");
        }
        Connects();
    }

    public void Connects() {
        BmobIM.connect(myUser.getUsername(), new ConnectListener() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Toast.makeText(MainActivity.this, myUser.getUsername() + "服务器连接成功", Toast.LENGTH_SHORT).show();
                } else {
                    Log.i("TAG", e.getMessage() + "  " + e.getErrorCode());
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        BmobIM.connect(myUser.getUsername(), new ConnectListener() {
            @Override
            public void done(String s, BmobException e) {

            }
        });
    }

    private void initUI() {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.vp_main);
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public boolean isViewFromObject(final View view, final Object object) {
                return view.equals(object);
            }

            @Override
            public void destroyItem(final View container, final int position, final Object object) {
                ((ViewPager) container).removeView((View) object);
            }

            @Override
            public Object instantiateItem(final ViewGroup container, final int position) {
                if (position == 0) {
                    final View view = LayoutInflater.from(
                            getBaseContext()).inflate(R.layout.msglist, null, false);

                    final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.msglist);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(
                                    getBaseContext(), LinearLayoutManager.VERTICAL, false
                            )
                    );
                    RecycleAdapter msgra = new RecycleAdapter(position);
                    recyclerView.setAdapter(msgra);

                    container.addView(view);
                    return view;
                } else if (position == 1) {
                    final View view = LayoutInflater.from(
                            getBaseContext()).inflate(R.layout.friendslist, null, false);

                    final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.friendslist);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(
                                    getBaseContext(), LinearLayoutManager.VERTICAL, false
                            )
                    );
                    RecycleAdapter friendra = new RecycleAdapter(position);
                    recyclerView.setAdapter(friendra);

                    container.addView(view);
                    return view;
                } else {
                    final View view = LayoutInflater.from(
                            getBaseContext()).inflate(R.layout.myselfview, null, false);
                    ImageView usericon = (ImageView) view.findViewById(R.id.usericon);
                    Uri uri;
                    if (myUser.getNick_URL_NET()==null){
                        uri = Uri.parse("http://bmob-cdn-15945.b0.upaiyun.com/2018/01/31/fc6d41ab40b00e568074bf5ffb812a57.png");
                    }
                    else {
                     uri = Uri.parse(myUser.getNick_URL_NET());}
                    Glide.with(MainActivity.this)
                            .load(uri)
                            .override(120, 120)
                            .into(usericon);
                    TextView userid = view.findViewById(R.id.userid);
                    userid.setText(myUser.getUsername());
                    Button tuichubtn = view.findViewById(R.id.tuichu);
                    tuichubtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    container.addView(view);
                    return view;
                }
            }

        });


        final String[] colors = getResources().getStringArray(R.array.default_preview);

        final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_horizontal);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.xinx),
                        Color.parseColor(colors[0]))
                       // .selectedIcon(getResources().getDrawable(R.drawable.xinx))
                        .title("Message")
                        .badgeTitle("NTB")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.txl),
                        Color.parseColor(colors[1]))
//                        .selectedIcon(getResources().getDrawable(R.drawable.ic_eighth))
                        .title("Friend")
                        .badgeTitle("with")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.gr),
                        Color.parseColor(colors[2]))
                     //   .selectedIcon(getResources().getDrawable(R.drawable.gr))
                        .title("Myself")
                        .badgeTitle("state")
                        .build()
        );

        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, 1);
        navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                navigationTabBar.getModels().get(position).hideBadge();
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });

        navigationTabBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < navigationTabBar.getModels().size(); i++) {
                    final NavigationTabBar.Model model = navigationTabBar.getModels().get(i);
                    navigationTabBar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            model.showBadge();
                        }
                    }, i * 100);
                }
            }
        }, 500);
    }

    public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.ViewHolder> {
        int p;

        RecycleAdapter(int p) {
            this.p = p;
        }

        @Override
        public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
            if (p == 0) {
                final View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.msgitemlist, parent, false);
                return new ViewHolder(view);
            } else {
                final View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.frienditemlist, parent, false);
                return new ViewHolder(view);
            }
        }

        public void onBindViewHolder(final ViewHolder holder, final int position) {

            if (p == 0) {
                String msg = (String) msglist.get(position).getFriendName();
                holder.msgtxt.setText(msg);
//                Uri uri = Uri.parse(msglist.get(position).getFriendIcon());
                Glide.with(MainActivity.this)
                        .load(msglist.get(position).getFriendIcon())
                        .override(120, 120)
                        .error(getResources().getDrawable(R.mipmap.ic_launcher))
                        .into(holder.msgicon);

            } else if (p == 1) {
                String friend = (String) jiahaoyou.get(position).getFriendName();
                Uri uri = Uri.parse(jiahaoyou.get(position).getFriendIcon());
                Glide.with(MainActivity.this)
                        .load(uri)
                        .override(120, 120)
                        .into(holder.friendicon);
                holder.friendtxt.setText(friend);
            } else if (p == 2) {

            }
        }

        @Override
        public int getItemCount() {
            int count;
            if (p == 0) {
                if (msglist == null) {
                    count = 0;
                } else {
                    count = msglist.size();
                }
            } else {
                count = jiahaoyou.size();
            }
            return count;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            protected static final String TAG = "ReceiverHolder";
            public ImageView msgicon;
            public ImageView friendicon;
            public TextView msgtxt;
            public TextView friendtxt;

            public ViewHolder(final View itemView) {
                super(itemView);
                msgicon = (ImageView) itemView.findViewById(R.id.msgicon);
                msgtxt = (TextView) itemView.findViewById(R.id.txt_msg_item_list);
                friendicon = (ImageView) itemView.findViewById(R.id.friendicon);
                friendtxt = (TextView) itemView.findViewById(R.id.txt_friend_item_list);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this, ChatingActivity.class);
                        intent.putExtra("myUser", myUser);
                        if (p == 0) {
                            intent.putExtra("chatUser", msglist.get(getPosition()));
                            Toast.makeText(MainActivity.this, msglist.get(getPosition()).getFriendName() , Toast.LENGTH_SHORT).show();
                            opfriendname = msglist.get(getPosition()).getFriendName();
                        } else if (p == 1) {
                            intent.putExtra("chatUser", jiahaoyou.get(getPosition()));
                            Toast.makeText(MainActivity.this, jiahaoyou.get(getPosition()).getFriendName(), Toast.LENGTH_SHORT).show();
                            opfriendname = jiahaoyou.get(getPosition()).getFriendName();
                        }
                        startActivity(intent);
                    }
                });
            }
        }

    }

}
