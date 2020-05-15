package dk.adaptmobile.android_seed.network;

import com.squareup.moshi.FromJson;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.ToJson;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class JavaDateAdapter extends JsonAdapter<Date> {
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault());

    @FromJson
    @Override
    public synchronized Date fromJson(JsonReader reader) throws IOException {
        if (reader.peek() == JsonReader.Token.NULL) {
            return reader.nextNull();
        }
        String string = reader.nextString();
        try {
            return formatter.parse(string);
        } catch (ParseException e) {
            throw new IOException("Error parsing date");
        }
    }

    @ToJson
    @Override
    public synchronized void toJson(JsonWriter writer, Date value) throws IOException {
        if (value == null) {
            writer.nullValue();
        } else {
            String string = formatter.format(value);
            writer.value(string);
        }
    }
}
