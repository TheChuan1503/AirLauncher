package tc.airlauncher;

import android.content.pm.ResolveInfo;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import android.content.Context;

public class AppSorter {

    public static List<ResolveInfo> sortApps(List<ResolveInfo> apps, final Context context) {
        Collections.sort(apps, new Comparator<ResolveInfo>() {
                Collator collator = Collator.getInstance(); // 创建一个Collator实例用于不区分大小写的比较

                @Override
                public int compare(ResolveInfo lhs, ResolveInfo rhs) {
                    /*if(lhs.loadLabel(context.getPackageManager()).toString()=="AirL"){
                        return collator.compare("a", "b");
                    }*/
                    String leftAppName = lhs.loadLabel(context.getPackageManager()).toString().trim();
                    String rightAppName = rhs.loadLabel(context.getPackageManager()).toString().trim();

                    return collator.compare(leftAppName.toLowerCase(), rightAppName.toLowerCase());
                }
            });

        return apps;
    }
}
