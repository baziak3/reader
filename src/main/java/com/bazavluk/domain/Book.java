package com.bazavluk.domain;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Book {
    List<String> lines = new ArrayList<>();
    /*
    {
        lines.add("-Приветствую вас, Главный Мастер. Я приношу свои извинения за мой внезапный визит, но дело не терпит отлагательства.");
        lines.add("-Здравствуйте, Мастер Гордон. Проходите, присаживайтесь. И вы очень обяжете меня, если оставите свою церемонность за порогом этой комнаты. Как член Коллегии Мастеров, вы вправе беспокоить меня в любой час дня и ночи. К тому же вы выполняете ответственное поручение... Полагаю, именно это привело вас ко мне? Какие-то проблемы с вашей подопечной?");
        lines.add("Посетитель устроился в мягком удобном кресле и, внимательно поглядев на хозяина, ответил:");
        lines.add("-И да, и нет, Главный. С моей подопечной всё в порядке. Если, конечно, не считать самого факта её существования... Впрочем, вы знаете моё мнение по этому вопросу.");
        lines.add("Главный Мастер кивнул:");
        lines.add("-Да, знаю. И в целом согласен с вами. Но мы оба прекрасно понимаем, что ваше предложение неприемлемо. Мы - Хранители, а не палачи. Даже в годы Усмирения наши предшественники не решились истребить всех МакКоев.");
        lines.add("-И напрасно. Если бы они не побоялись запачкать руки, нам теперь не пришлось бы расхлёбывать последствия их мягкотелости.");
        lines.add("-Мне кажется, вы слишком драматизируете ситуацию, Гордон. Шесть рецидивов за последние двести лет...");
        lines.add("-Я не об этом, Главный... гм, простите, что перебиваю вас. Вы что-нибудь слышали о Конноре МакКое?");
        lines.add("-О котором из них? В наших хрониках упоминается несколько человек с таким именем.");
    }
    */
    int currentLine = 0;

    public Book(Context context) {
        AssetManager am = context.getAssets();
        try {
            InputStream is = am.open("book.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while((line = reader.readLine()) != null) {
                line = line.trim();
                line = line.replace('—', '-');
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
    public String getNextLine() {
        String res = lines.get(currentLine);
        currentLine++;
        if (currentLine >= lines.size()) {
            currentLine = 0;
        }
        return res;
    }
}
