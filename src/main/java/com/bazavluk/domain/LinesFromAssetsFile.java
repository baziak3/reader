package com.bazavluk.domain;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class LinesFromAssetsFile implements Lines {
    List<String> lines = new ArrayList<>();
    int currentLine = 0;

    public LinesFromAssetsFile(Context context) {
        AssetManager am = context.getAssets();
        try {
            InputStream is = am.open("book.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while((line = reader.readLine()) != null) {
                line = line.trim();
                line = line.replaceAll("—", "-");
                line = line.replaceAll("^-\\s*", "-");
                if (!line.equals("")) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            // TODO
            e.printStackTrace();
        }
    }
    /**
     * @return Well formatted string (no bad chars, no space before "," or "." etc.)
     */
    @Override
    public String getNextLine() {
        String res = lines.get(currentLine);
        currentLine++;
        if (currentLine >= lines.size()) {
            currentLine = 0;
        }
        return res;
    }
}
