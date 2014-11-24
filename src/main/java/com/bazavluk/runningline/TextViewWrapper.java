package com.bazavluk.runningline;

import android.content.Context;
import android.text.Html;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bazavluk.configuration.Configuration;
import com.bazavluk.util.LS;

import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper for TextView
 */
public class TextViewWrapper {
    private TextView textView;
    private List<WordMeta> wordMetas;
    private Context context;
    private String content;
    private int width;
    private Configuration configuration = LS.get(Configuration.class);

    public TextViewWrapper(Context context) {
        this.context = context;
        textView = new TextView(this.context);
        textView.setSingleLine();
    }

    public TextViewWrapper updatePosition(int xPosition, int yPosition) {
        RelativeLayout.LayoutParams mainLineLayoutParams = new RelativeLayout.LayoutParams(
                textView.getLayoutParams().width, RelativeLayout.LayoutParams.WRAP_CONTENT);
        mainLineLayoutParams.setMargins(xPosition, yPosition, 0, 0);
        textView.setLayoutParams(mainLineLayoutParams);
        return this;
    }

    public void setWords(List<String> words) {
        StringBuilder partialContent = new StringBuilder();
        wordMetas = new ArrayList<>();
        for (String word: words) {
            int offset = (int) textView.getPaint().measureText(partialContent.toString());
            int width = (int) textView.getPaint().measureText(word);
            int charOffset = partialContent.length();
            Delay delay = Delay.NO;
            if (word.length() > 1) {
                char charToTest = word.charAt(word.length() - 1);
                if (wordMetas.size() == 0) {
                    delay = Delay.NEW_LINE;
                } else if (charToTest == ',') {
                    delay = Delay.COMMA;
                } else if (charToTest == '.') {
                    delay = Delay.DOT;
                } else if (charToTest == ':') {
                    delay = Delay.COLON;
                } else if (charToTest == ';') {
                    delay = Delay.SEMI_COLON;
                } else if (charToTest == '-') {
                    delay = Delay.DASH;
                } else if (charToTest == '?') {
                    delay = Delay.QUESTION;
                } else if (charToTest == '!') {
                    delay = Delay.EXCLAMATION;
                }
            }
            wordMetas.add(new WordMeta(word, delay, offset, charOffset, width));

            partialContent.append(word).append(" ");
        }
        content = partialContent.toString();
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

    public int getWordOffset(int wordIndex) {
        return wordMetas.get(wordIndex).getOffset();
    }

    public TextViewWrapper shadowPreviousLine() {
        textView.setText(Html.fromHtml("<font color=\"" + configuration.getPreviousLineColor() + "\">" + content + "</font>"));
        return this;
    }

    public TextViewWrapper shadowCurrentLine(int currentWord) {
        TextViewWrapper.WordMeta wm = wordMetas.get(currentWord);
        textView.setText(Html.fromHtml(("<font color=\"" + configuration.getCurrentLineColor() + "\">")
                + content.substring(0, wm.getCharOffset())
                + "</font>"
                + "<font color=\"" + configuration.getCurrentWordColor() + "\">"
                + content.substring(wm.getCharOffset(), wm.getCharOffset() + wm.getWord().length())
                + "</font>"
                + "<font color=\"" + configuration.getCurrentLineColor() + "\">"
                + content.substring(wm.getCharOffset() + wm.getWord().length())
                + "</font>"));
        return this;
    }

    public TextViewWrapper shadowNextLine() {
        textView.setText(Html.fromHtml("<font color=\"" + configuration.getNextLineColor() + "\">" + content + "</font>"));
        return this;
    }

    public WordMeta getWordMeta(int wordIndex) {
        if (wordIndex >= wordMetas.size()) {
            return null;
        } else {
            return wordMetas.get(wordIndex);
        }
    }

    public int wordsCount() {
        return wordMetas.size();
    }

    public static class WordMeta {
        private String word;
        private Delay delay;
        private int offset;
        private int charOffset;
        private int width;

        public WordMeta(String word, Delay delay, int offset, int charOffset, int width) {
            this.word = word;
            this.delay = delay;
            this.offset = offset;
            this.charOffset = charOffset;
            this.width = width;
        }

        public String getWord() {
            return word;
        }

        public double getDelay() {
            return delay.coefficient();
        }

        public int getOffset() {
            return offset;
        }

        public int getCharOffset() {
            return charOffset;
        }

        public int getWidth() {
            return width;
        }
    }

    /**
     * Describes delays' coefficients.
     * TODO move into the speed package.
     */
    static enum Delay {
        NEW_LINE(2),
        NO(1),
        COMMA(1.5),
        DOT(2),
        DASH(1.5),
        COLON(1.5),
        SEMI_COLON(1.5),
        QUESTION(2),
        EXCLAMATION(2);

        private double coefficient;

        private Delay(double value) {
            coefficient = value;
        }

        public double coefficient() {
            // return coefficient > 1 ? coefficient * 3: coefficient;
            return coefficient;
        }
    }
}
