package org.moon.figura.utils;

import com.mojang.brigadier.StringReader;
import net.minecraft.client.gui.Font;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

import java.util.ArrayList;
import java.util.List;

public class TextUtils {

    public static final ResourceLocation FIGURA_FONT = new FiguraIdentifier("default");
    public static final int TAB_SPACING = 2;

    public static Component noBadges4U(Component text) {
        return replaceInText(text, "[❗❌\uD83C\uDF54\uD83E\uDD90\uD83C\uDF19\uD83C\uDF00❤☆★]", "\uFFFD");
    }

    public static List<Component> splitText(Component text, String regex) {
        //list to return
        ArrayList<Component> textList = new ArrayList<>();

        //current line variable
        MutableComponent currentText = Component.empty();

        //iterate over the text
        for (Component entry : text.toFlatList(text.getStyle())) {
            //split text based on regex
            String entryString = entry.getString();
            String[] lines = entryString.split(regex, -1);

            //iterate over the split text
            for (int i = 0; i < lines.length; i++) {
                //if it is not the first iteration, add to return list and reset the line variable
                if (i != 0) {
                    textList.add(currentText.copy());
                    currentText = Component.empty();
                }

                //append text with the line text
                currentText.append(Component.literal(lines[i]).setStyle(entry.getStyle()));
            }
        }
        //add the last text iteration then return
        textList.add(currentText);
        return textList;
    }

    public static Component removeClickableObjects(Component text) {
        //text to return
        MutableComponent finalText = Component.empty();

        //iterate over the text
        for (Component entry : text.toFlatList(text.getStyle())) {
            //remove click events
            Component removed = Component.literal(entry.getString()).setStyle(entry.getStyle().withClickEvent(null));

            //append text to return
            finalText.append(removed);
        }

        //return text
        return finalText;
    }

    public static Component tryParseJson(String text) {
        //text to return
        Component finalText;

        try {
            //attempt to parse json
            finalText = Component.Serializer.fromJson(new StringReader(text));

            //if failed, throw a dummy exception
            if (finalText == null)
                throw new Exception("Error parsing JSON string");
        } catch (Exception ignored) {
            //on any exception, make the text as-is
            finalText = Component.literal(text);
        }

        //return text
        return finalText;
    }

    public static Component replaceInText(Component text, String regex, Object replacement) {
        //fix replacement object
        Component replace = replacement instanceof Component c ? c : Component.literal(replacement.toString());

        //text to return
        MutableComponent ret = Component.empty();

        //iterate over the initial text
        List<Component> list = text.toFlatList(text.getStyle());
        for (Component component : list) {
            //get the text raw string
            String textString = component.getString();

            //split the string keeping the split text
            String[] split = textString.split("((?<=" + regex + ")|(?=" + regex + "))");
            for (String s : split) {
                //append the text if it does not match the split, otherwise append the replacement instead
                if (!s.matches(regex))
                    ret.append(Component.literal(s).withStyle(component.getStyle()));
                else
                    ret.append(replace);
            }
        }

        //return
        return ret;
    }

    public static Component trimToWidthEllipsis(Font font, Component text, int width) {
        //return text without changes if it is not larger than width
        if (font.width(text.getVisualOrderText()) <= width)
            return text;

        //get ellipsis size
        Component dots = Component.literal("...");
        int size = font.width(dots.getVisualOrderText());

        //trim and return modified text
        String trimmed = font.substrByWidth(text, width - size).getString();
        return Component.literal(trimmed).setStyle(text.getStyle()).append(dots);
    }

    public static Component replaceTabs(Component text) {
        return TextUtils.replaceInText(text, "\\t", " ".repeat(TAB_SPACING));
    }

    public static List<FormattedCharSequence> warpTooltip(Component text, Font font, int mousePos, int screenWidth) {
        //first split the new line text
        List<Component> splitText = TextUtils.splitText(text, "\n");

        //get the possible tooltip width
        int left = mousePos - 16;
        int right = screenWidth - mousePos - 12;

        //get largest text size
        int largest = getWidth(splitText, font);

        //get the optimal side for warping
        int side = largest <= right ? right : largest <= left ? left : Math.max(left, right);

        //warp the unmodified text
        List<FormattedCharSequence> warp = new ArrayList<>();
        font.getSplitter().splitLines(text, side, Style.EMPTY, (formattedText, aBoolean) -> warp.add(Language.getInstance().getVisualOrder(formattedText)));
        return warp;
    }

    //get the largest text width from a list
    public static int getWidth(List<Component> text, Font font) {
        int width = 0;
        for (Component component : text)
            width = Math.max(width, font.width(component));
        return width;
    }
}
