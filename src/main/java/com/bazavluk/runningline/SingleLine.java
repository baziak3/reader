package com.bazavluk.runningline;

import android.content.Context;
import android.text.Html;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
* Created by Папа on 29.09.14.
*/
class SingleLine {
    private String content;
    private TextView textView;
    private int xPosition;
    private int yPosition;
    private int width;

    private int currentWordMeta = 0;
    private List<WordMeta> wordMetas = new ArrayList<>();

    public SingleLine(Context context, String content) {
        this(context, content, 0, 0);
    }

    public SingleLine(Context context, String content, int xPosition, int yPosition) {
        this.content = content;
        this.xPosition = xPosition;
        this.yPosition = yPosition;

        prepare(context);
    }

    public TextView getTextView() {
        return textView;
    }

    public int getXPosition() {
        return xPosition;
    }

    public int getWidth() {
        return width;
    }

    public void setXPosition(int position) {
        this.xPosition = position;
        updatePosition();
    }

    public void setYPosition(int position) {
        this.yPosition = position;
        updatePosition();
    }

    private void updatePosition() {
        RelativeLayout.LayoutParams mainLineLayoutParams = new RelativeLayout.LayoutParams(
                textView.getLayoutParams().width, RelativeLayout.LayoutParams.WRAP_CONTENT);
        mainLineLayoutParams.setMargins(xPosition, yPosition, 0, 0);
        textView.setLayoutParams(mainLineLayoutParams);
    }

    public WordMeta getNextWordMeta() {
        if (currentWordMeta < wordMetas.size()) {
            return wordMetas.get(currentWordMeta++);
        } else {
            currentWordMeta++;
            return null;
        }
    }

    public WordMeta getPreviousWordMeta() {
        if (currentWordMeta >=0) {
            return wordMetas.get(currentWordMeta--);
        } else {
            // currentWordMeta--;
            return null;
        }
    }

    private void prepare(Context context) {
        textView = new TextView(context);
        textView.setSingleLine();

        String[] parts = content.split("\\s+");
        List<String> words = new ArrayList<>();
        for (String part: parts) {
            if (part.equals("-")) {
                words.set(
                        words.size() - 1,
                        words.get(words.size() - 1) + "- ");
            } else {
                words.add(part + " ");
            }
        }
        StringBuilder partialContent = new StringBuilder();
        int charOffset = 0;
        for (String word: words) {
            int width = (int) textView.getPaint().measureText(partialContent.toString());
            partialContent.append(word);
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
            wordMetas.add(new WordMeta(word, delay, width, charOffset));
            charOffset += word.length();
        }
        content = partialContent.toString();
        textView.setText(content);
        width = (int) textView.getPaint().measureText(content);
        RelativeLayout.LayoutParams mainLineLayoutParams =
                new RelativeLayout.LayoutParams(
                        width,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        mainLineLayoutParams.setMargins(xPosition, yPosition, 0, 0);
        textView.setLayoutParams(mainLineLayoutParams);
    }

    /**
     * Highlight the word before the one, pointed by currentWordMeta
     */
    public void shadowInactiveWords() {
        StringBuilder highlighted = new StringBuilder();

        if (currentWordMeta == 0 || currentWordMeta > wordMetas.size()) {
            highlighted.append("<font color=\"#333333\">");
            highlighted.append(content);
            highlighted.append("</font>");
        } else {
            WordMeta wm = wordMetas.get(currentWordMeta - 1);
            highlighted.append("<font color=\"#333333\">");
            highlighted.append(content.substring(0, wm.getCharOffset()));
            highlighted.append("</font><font color=\"#FFFFFF\">");
            highlighted.append(content.substring(wm.getCharOffset(), wm.getCharOffset() + wm.getWord().length()));
            highlighted.append("</font><font color=\"#333333\">");
            highlighted.append(content.substring(wm.getCharOffset() + wm.getWord().length()));
            highlighted.append("</font>");
        }

        textView.setText(Html.fromHtml(highlighted.toString()));
    }

    public int wordsLeft() {
        return wordMetas.size() - currentWordMeta;
    }
}
