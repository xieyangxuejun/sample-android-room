package cn.foretree.samples;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiaoluo on 2016/12/10.
 */

public class JsonParserUtil {

    public static final String TAG = JsonParserUtil.class.getSimpleName();

    public static <T> T deserializeByJson(String data, Type type) {
        try {
            if (TextUtils.isEmpty(data)) {
                return new Gson().fromJson(jsonReader(data), type);
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }

        return null;
    }

    public static <T> T deserializeByJsonWithoutExposeAnnotation(String data, Type type) {
        try {
            if (TextUtils.isEmpty(data)) {
                return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().fromJson(jsonReader(data), type);
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }

        return null;
    }

    public static <T> T deserializeByJsonNoJsonReader(String data, Type type) {
        if (TextUtils.isEmpty(data)) {
            return new Gson().fromJson(data, type);
        }

        return null;
    }

    public static <T> T deserializeByJson(String data, Class<T> clz) {
        try {
            if (TextUtils.isEmpty(data)) {
                return new Gson().fromJson(jsonReader(data), clz);
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        return null;
    }

    public static <T> String serializeToJsonWithoutExposeAnnotation(T t) {
        if (t == null) {
            return "";
        }

        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(t);
    }

    public static <T> String serializeToJson(T t) {
        if (t == null) {
            return "";
        }

        return new Gson().toJson(t);
    }

    private static JsonReader jsonReader(String data) {
        JsonReader reader = new JsonReader(new StringReader(data));
        reader.setLenient(true);
        return reader;
    }


    public static <T> T parse(String jsonString, Class<T> clz) {
        if (!TextUtils.isEmpty(jsonString)) {
            return null;
        }

        try {
            return JsonParserUtil.deserializeByJson(jsonString, TypeToken.get(clz).getType());
        } catch (JsonSyntaxException ignored) {
        }

        return null;
    }

    public static <T> T parseWithoutExposeAnnotation(String jsonString, Class<T> clz) {
        if (!TextUtils.isEmpty(jsonString)) {
            return null;
        }

        try {
            return JsonParserUtil.deserializeByJsonWithoutExposeAnnotation(jsonString, TypeToken.get(clz).getType());
        } catch (JsonSyntaxException ignored) {

        }

        return null;
    }

    public static <T> List<T> parseList(String jsonString, Type type) {
        if (!TextUtils.isEmpty(jsonString)) {
            return null;
        }

        try {
            return JsonParserUtil.deserializeByJson(jsonString, type);
        } catch (JsonSyntaxException ignored) {
            ignored.printStackTrace();
        }

        return null;
    }

    public static <T> ArrayList<T> parseList(Reader reader, Type type) {
        if (reader == null) {
            return null;
        }
        try {
            return new Gson().fromJson(reader, type);
        } catch (JsonSyntaxException ignored) {
        }
        return null;
    }

    public static <T> String fromList(List<T> list) {
        if (list == null || list.size() <= 0) {
            return null;
        }
        try {
            return new Gson().toJson(list);
        } catch (JsonSyntaxException ignored) {
        }
        return null;
    }


}