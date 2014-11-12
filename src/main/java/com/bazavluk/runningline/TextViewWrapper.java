package com.bazavluk.runningline;

import android.content.Context;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper for TextView
 */
public class TextViewWrapper {
    private TextView textView;
    private List<WordMeta> wordMetas;
    private Context context;
    private int width;

    public TextViewWrapper(Context context) {
        this.context = context;
        textView = new TextView(this.context);
        textView.setSingleLine();
    }

    public void updatePosition(int xPosition, int yPosition) {
        RelativeLayout.LayoutParams mainLineLayoutParams = new RelativeLayout.LayoutParams(
                textView.getLayoutParams().width, RelativeLayout.LayoutParams.WRAP_CONTENT);
        mainLineLayoutParams.setMargins(xPosition, yPosition, 0, 0);
        textView.setLayoutParams(mainLineLayoutParams);
    }

    public void setWords(List<String> words) {
        StringBuilder partialContent = new StringBuilder();
        wordMetas = new ArrayList<>();
        for (String word: words) {
            int offset = (int) textView.getPaint().measureText(partialContent.toString());
            int charOffset = partialContent.length();
            Delays delay = Delays.NO;
            if (word.length() > 1) {
                char charToTest = word.charAt(word.length() - 2);
                if (wordMetas.size() == 0) {
                    delay = Delays.NEW_LINE;
                } else if (charToTest == ',') {
                    delay = Delays.COMMA;
                } else if (charToTest == '.') {
                    delay = Delays.DOT;
                } else if (charToTest == ':') {
                    delay = Delays.COLON;
                } else if (charToTest == ';') {
                    delay = Delays.SEMI_COLON;
                } else if (charToTest == '-') {
                    delay = Delays.DASH;
                } else if (charToTest == '?') {
                    delay = Delays.QUESTION;
                } else if (charToTest == '!') {
                    delay = Delays.EXCLAMATION;
                }
            }
            wordMetas.add(new WordMeta(word, delay, offset, charOffset));

            partialContent.append(word).append(" ");
        }
        textView.setText(partialContent);
        this.width = (int) textView.getPaint().measureText(partialContent.toString());
        RelativeLayout.LayoutParams mainLineLayoutParams =
                new RelativeLayout.LayoutParams(
                        width,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        mainLineLayoutParams.setMargins(0, 0, 0, 0);
        textView.setLayoutParams(mainLineLayoutParams);
    }

    public int getWidth() {
        return width;
    }

    public TextView get() {
        return textView;
    }

    public int getWordOffset(int currentWordIndex) {
        return wordMetas.get(currentWordIndex).getOffset();
    }

    /**
    * Created by Папа on 29.09.14.
    */
    static class WordMeta {
        private String word;
        private Delays delay;
        private int offset;
        private int charOffset;

        public WordMeta(String word, Delays delay, int offset, int charOffset) {
            this.word = word;
            this.delay = delay;
            this.offset = offset;
            this.charOffset = charOffset;
        }

        public String getWord() {
            return word;
        }

        public Delays getDelay() {
            return delay;
        }

        public int getOffset() {
            return offset;
        }

        public int getCharOffset() {
            return charOffset;
        }
    }
}
