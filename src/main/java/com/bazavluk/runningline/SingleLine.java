package com.bazavluk.runningline;

import android.content.Context;
import android.text.Html;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
* Created by Папа on 29.09.14.
*/
class SingleLine {
    private String content;
    private TextView textView;
    private int position;
    private int width;

    private int currentWordMeta = 0;
    private List<WordMeta> wordMetas = new ArrayList<>();

    public SingleLine(Context context, String content) {
        this(context, content, 0);
    }

    public SingleLine(Context context, String content, int position) {
        this.content = content;
        this.position = position;

        prepare(context);
    }

    public TextView getTextView() {
        return textView;
    }

    public int getPosition() {
        return position;
    }

    public int getWidth() {
        return width;
    }

    public void setTextViewPosition(int position) {
        this.position = position;

        LinearLayout.LayoutParams mainLineLayoutParams = new LinearLayout.LayoutParams(
                textView.getLayoutParams().width, LinearLayout.LayoutParams.WRAP_CONTENT);
        mainLineLayoutParams.setMargins(position, 0, 0, 0);
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

    private void prepare(Context context) {
        textView = new TextView(context);

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
                if (charToTest == ',') {
                    delay = Delays.COMMA;
                } else if (charToTest == '.') {
                    delay = Delays.DOT;
                } else if (charToTest == ':') {
                    delay = Delays.COLON;
                } else if (charToTest == ';') {
                    delay = Delays.SEMI_COLON;
                } else if (charToTest == '-') {
                    delay = Delays.DASH;
                }
            }
            wordMetas.add(new WordMeta(word, delay, width, charOffset));
            charOffset += word.length();
        }
        content = partialContent.toString();
        textView.setText(content);
        width = (int) textView.getPaint().measureText(content);
        LinearLayout.LayoutParams mainLineLayoutParams =
                new LinearLayout.LayoutParams(
                        width,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
        mainLineLayoutParams.setMargins(position, 0, 0, 0);
        textView.setLayoutParams(mainLineLayoutParams);
    }

    public void darknessPreviousWords() {
        StringBuilder highlighted = new StringBuilder();

        if (currentWordMeta - 1 == wordMetas.size()) {
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
