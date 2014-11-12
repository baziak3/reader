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
    List<List<String>> lines = new ArrayList<>();
    int currentLine = 0;
    int currentWord = 0;

    public LinesFromAssetsFile(Context context) {
        AssetManager am = context.getAssets();
        try {
            InputStream is = am.open("book.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while((line = reader.readLine()) != null) {
                // prepare string (no bad chars, no space before "," or "." etc.)
                line = line.trim();
                line = line.replaceAll("—", "-");
                line = line.replaceAll("^-\\s*", "-");

                String[] parts = line.split("\\s+");
                List<String> words = new ArrayList<>();
                for (String part: parts) {
                    if (part.equals("-")) {
                        words.set(
                                words.size() - 1,
                                words.get(words.size() - 1) + "-");
                    } else {
                        words.add(part);
                    }
                }
                lines.add(words);
            }
        } catch (IOException e) {
            // TODO
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getPreviousLineWords() {
        if (currentLine == 0) {
            return new ArrayList<>();
        } else {
            return lines.get(currentLine - 1);
        }
    }

    @Override
    public List<String> getCurrentLineWords() {
        return lines.get(currentLine);
    }

    @Override
    public List<String> getNextLineWords() {
        if (currentLine < lines.size() - 1) {
            return lines.get(currentLine + 1);
        } else {
            return new ArrayList<>();
        }
    }

    @Override
    public int getCurrentWord() {
        return currentWord;
    }

    @Override
    public void increaseCurrentWord() {
        if (lines.get(currentLine).size() == currentWord + 1) {
            if (lines.size() > currentLine + 1) {
                currentLine++;
                currentWord = 0;
            }
        } else {
            currentWord++;
        }
    }
}
