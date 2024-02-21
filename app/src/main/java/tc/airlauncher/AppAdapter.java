package tc.airlauncher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.content.ComponentName;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;
import android.widget.PopupMenu;
import android.view.MenuItem;
import android.net.Uri;
import android.provider.Settings;
import androidx.fragment.app.FragmentManager;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.AppHolder>{
    private List<ResolveInfo> apps;
    private Context context;

    public AppAdapter(List<ResolveInfo> apps) {
        this.apps = apps;
    }

    public String getName(int index) {
        return apps.get(index).activityInfo.name;
    }

    public static class AppHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvName;

        public AppHolder(View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            tvName = itemView.findViewById(R.id.tvName);
        }
    }

    @Override
    public AppHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_app, parent, false);
        return new AppHolder(view);
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    @Override
    public void onBindViewHolder(AppHolder holder, int position) {
        holder.tvName.setTextSize(Float.parseFloat(""+((Double.parseDouble(MainActivity.getObj().读配置("font_size"))+3)*1.5-Double.parseDouble(MainActivity.getObj().读配置("col"))*0.25)));
        //Toast.makeText(context, ""+holder.tvName.getTextSize(), Toast.LENGTH_SHORT).show();
        final ResolveInfo resolveInfo = apps.get(position);
        holder.ivIcon.setImageDrawable(resolveInfo.activityInfo.loadIcon(context.getPackageManager()));
        holder.tvName.setText(resolveInfo.loadLabel(context.getPackageManager()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isAppInstalled(resolveInfo.activityInfo.packageName)==true){
                        Intent intent = new Intent();
                        intent.setComponent(new ComponentName(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                    else{
                        Toast.makeText(context, "应用("+resolveInfo.activityInfo.packageName+")未安装", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    showPopupMenu(v,context,resolveInfo.activityInfo.packageName);
                    return false;
                }
            });
    }
    private boolean isAppInstalled(String pkgName) {
        if (pkgName == null || pkgName.isEmpty()) {
            return false;
        }
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }

        if (packageInfo != null) {
            return true;
        } 
        return false;
    }
    private void showPopupMenu(View view,final Context ctx,final String pkg) {
        PopupMenu popup = new PopupMenu(ctx, view);
        popup.getMenuInflater().inflate(R.menu.app_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.AppInfo:
                            Intent intent = new Intent()
                                .setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                .setData(Uri.fromParts("package",pkg,null));
                            ctx.startActivity(intent);
                            return true;
                        case R.id.Uninstall:
                            Uri uri = Uri.fromParts("package",pkg,null);
                            Intent mintent = new Intent(Intent.ACTION_DELETE, uri);
                            ctx.startActivity(mintent);
                            return true;
                        case R.id.AirL:
                            MainActivity.getObj().GFM();
                            return true;
                        default:
                            return false;
                    }
                }
            });

        popup.show();
    }
}
