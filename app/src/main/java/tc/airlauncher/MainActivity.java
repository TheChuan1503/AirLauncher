package tc.airlauncher;

import tc.airlauncher.R;
import android.app.*;
import android.os.*;
import android.view.Window;
import android.content.pm.ResolveInfo;
import java.util.List;
import android.content.Intent;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.GridLayoutManager;
import android.view.WindowManager;
import android.os.Build;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.widget.LinearLayout;
import android.view.ViewGroup;
import java.util.Collections;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.Comparator;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import androidx.fragment.app.FragmentActivity;
import android.content.Context;
import androidx.fragment.app.FragmentManager;

public class MainActivity extends FragmentActivity {

    private FragmentManager fm;

    private static MainActivity mActivity;

    private Context mContext;
    
    private Handler mHandler = new Handler();
    
    private int OldAppSize;
    
    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if(OldAppSize!=scanApps().size()){
                initView(Integer.parseInt(读配置("col")));
                OldAppSize=scanApps().size();
            }
            mHandler.postDelayed(this, 2000);
        }
    };
     
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivity=this;
        mContext=this;
        fm=getSupportFragmentManager();
        if(Build.VERSION.SDK_INT >= 21){
            setTheme(android.R.style.Theme_Material_Wallpaper);
        }
        else{
            setTheme(android.R.style.Theme_Holo_Wallpaper);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        LinearLayout RootLayout=findViewById(R.id.RootLayout);
        RecyclerView AppsView=findViewById(R.id.rvApps);
        AppsView.setItemViewCacheSize(256);
        LinearLayout StatusBarPlaceholder=findViewById(R.id.StatusBarPlaceholder);
        
        int statusBarHeight1 = 0; 
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android"); 
        if (resourceId!=0) { 
            statusBarHeight1 = getResources().getDimensionPixelSize(resourceId); 
        } 
        StatusBarPlaceholder.getLayoutParams().height=statusBarHeight1;
        int col=Integer.parseInt(读配置("col"));
        String font_size=读配置("font_size");
        if(col==0){
            col=4;
            写配置("col",""+col);
        }
        if(font_size==""){
            font_size="7";
            写配置("font_size",font_size);
        }
        initView(Integer.parseInt(读配置("col")));
        mHandler.postDelayed(mRunnable, 2000);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 返回键被按下
            return true; // 禁用返回键
        } else if (keyCode == KeyEvent.KEYCODE_HOME) {
            // Home键被按下
            return true; // 禁用Home键
        }

        return super.onKeyDown(keyCode, event);
    }
	
    public void initView(int col){
        List<ResolveInfo> apps = scanApps();
        OldAppSize=apps.size();
        apps=AppSorter.sortApps(apps,this);
        AppAdapter adapter = new AppAdapter(apps);
        RecyclerView rvApps = findViewById(R.id.rvApps);
        rvApps.setLayoutManager(new GridLayoutManager(this, col));
        rvApps.setAdapter(adapter);
        //Toast.makeText(getApplication(), PinyinUtils.getPinyinFirstLetter("傻b"), Toast.LENGTH_SHORT).show();
    }
    private List<ResolveInfo> scanApps() {
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        return getPackageManager().queryIntentActivities(intent, 0);
    }
    private FragmentManager _gfm(){
        return fm;
    }                             
    public static MainActivity getObj(){

        return mActivity;              

    }
    public void GFM(){
        AirLDialogFragment dialogFragment = new AirLDialogFragment();
        dialogFragment.show(fm , "airl_dialog");
    }
    public void 写配置(String key,String value){
        Config.set(mContext,"settings",key,value);
    }                                       
    public String 读配置(String key){         
        return Config.get(mContext,"settings",key);
    }                            
}                                             
