package tc.airlauncher;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.app.AlertDialog;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageInfo;
import android.view.View.OnClickListener;
import android.content.DialogInterface;
import android.net.Uri;
import android.content.Intent;

public class AirLDialogFragment extends DialogFragment {

    private SeekBar 列数;
    private TextView 列数L;
    private SeekBar 字体大小;
    private Button 关于;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_airl, null);
        列数 = view.findViewById(R.id.列数);
        列数L = view.findViewById(R.id.列数Label);
        字体大小 = view.findViewById(R.id.字体大小);
        关于 = view.findViewById(R.id.关于);
        
        列数.setProgress(Integer.parseInt(MainActivity.getObj().读配置("col"))-1);
        列数L.setText(列数L.getText()+" ("+Integer.parseInt(MainActivity.getObj().读配置("col"))+")");
        列数.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    MainActivity.getObj().写配置("col",""+(progress+1));
                    列数L.setText("列数 ("+(progress+1)+")");
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    MainActivity.getObj().initView(seekBar.getProgress()+1);
                }
            });
        
        字体大小.setProgress(Integer.parseInt(MainActivity.getObj().读配置("font_size")));
        字体大小.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    MainActivity.getObj().写配置("font_size",""+(progress));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    MainActivity.getObj().initView(Integer.parseInt(MainActivity.getObj().读配置("col")));
                }
            });
        关于.setOnClickListener(new OnClickListener(){

                @Override
                public void onClick(View v) {
                    AlertDialog dialog = new AlertDialog.Builder(view.getContext())
                        .setTitle("关于")
                        .setMessage("Github:@TheChuan1503\nGitee:@TC1503\n\n在GITHUB和GITEE上查看源代码\nhttps://github.com/TheChuan1503/AirLauncher\nhttps://gitee.com/tc1503/AirLauncher\n遵循MIT协议")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dia, int which) {
                            }
                        })
                        .setNegativeButton("GITHUB", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dia, int which) {
                                Uri uri = Uri.parse("https://github.com/TheChuan1503/AirLauncher"); 
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri); 
                                startActivity(intent); 
                            }
                        })
                        .setNeutralButton("GITEE",new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dia, int which) {
                                Uri uri = Uri.parse("https://gitee.com/tc1503/AirLauncher"); 
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri); 
                                startActivity(intent); 
                            }
                        })
                        .create();
                    dialog.show();
                }
                
            
        });
        
        // 创建对话框并设置属性
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
            .setView(view)
            .setTitle("设置")
            .setCancelable(true); // 点击空白处不关闭对话框

        // 添加确定和关闭按钮到对话框
        builder.setPositiveButton(null, null); // 隐藏默认的确定按钮
        builder.setNegativeButton("关闭", null); // 不需要额外处理，dismiss会在监听器中处理

        return builder.create();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 在此也可以添加额外的视图初始化逻辑
    }
    public static String getAppVersionName(Context context) {  
        String versionName = "";  
        try {  
            // ---get the package info---  
            PackageManager pm = context.getPackageManager();  
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);  
            versionName = pi.versionName;  
            int versioncode = pi.versionCode;
            if (versionName == null || versionName.length() <= 0) {  
                return "";  
            }  
        } catch (Exception e) {
        }  
        return versionName;  
    } 
    
}
