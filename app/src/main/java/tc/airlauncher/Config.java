package tc.airlauncher;
import android.content.SharedPreferences;
import android.content.Context;

public class Config {
    public static String get(Context ctx,String sec,String key){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(sec, ctx.MODE_PRIVATE);
        String tt =  sharedPreferences.getString(key,"");
        return tt;
    }
    public static void set(Context ctx,String sec,String key,String value){
        SharedPreferences sharedPreferences = ctx.getSharedPreferences(sec, ctx.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
    }
}
