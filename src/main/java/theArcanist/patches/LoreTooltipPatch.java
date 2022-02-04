package theArcanist.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import javassist.*;
import theArcanist.ArcanistMod;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static basemod.patches.com.megacrit.cardcrawl.screens.SingleCardViewPopup.TitleFontSize.fontFile;

public class LoreTooltipPatch {
    private static BitmapFont loreFont = LoreTooltipPatch.prepFont(22.0f, false);

    @SpirePatch(
            clz = TipHelper.class,
            method = "renderKeywords"
    )
    public static class TipHelperRenderLorePatch{
        @SpirePostfixPatch
        public static void TipHelperRenderLore(float x, @ByRef float[] y, SpriteBatch sb,
                                               ArrayList<String> keywords,
                                               float ___TIP_DESC_LINE_SPACING, float ___BODY_TEXT_WIDTH,
                                               float ___BOX_EDGE_H, float ___SHADOW_DIST_X, float ___SHADOW_DIST_Y,
                                               float ___BOX_W, float ___BOX_BODY_H, float ___TEXT_OFFSET_X,
                                               Color ___BASE_COLOR, AbstractCard ___card) {
            Color color;
            Color textColor;
            String s;
            try {
                Field field1 = AbstractCard.class.getField("lore");
                Field field2 = AbstractCard.class.getField("loreColor");
                Field field3 = AbstractCard.class.getField("loreTextColor");
                s = (String) field1.get(___card);
                color = (Color) field2.get(___card);
                textColor = (Color) field3.get(___card);
                if (color == null || s == null)
                    return;
                if (textColor == null)
                    textColor = ___BASE_COLOR;
            }
            catch (Exception e) {
                return;
            }

            float h = -FontHelper.getSmartHeight(FontHelper.tipBodyFont, s,
                    ___BODY_TEXT_WIDTH, ___TIP_DESC_LINE_SPACING) - 7.0F * Settings.scale;

            sb.setColor(Settings.TOP_PANEL_SHADOW_COLOR);
            sb.draw(ImageMaster.KEYWORD_TOP, x + ___SHADOW_DIST_X, y[0] - ___SHADOW_DIST_Y, ___BOX_W, ___BOX_EDGE_H);
            sb.draw(ImageMaster.KEYWORD_BODY, x + ___SHADOW_DIST_X, y[0] - h - ___BOX_EDGE_H - ___SHADOW_DIST_Y, ___BOX_W, h + ___BOX_EDGE_H);
            sb.draw(ImageMaster.KEYWORD_BOT, x + ___SHADOW_DIST_X, y[0] - h - ___BOX_BODY_H - ___SHADOW_DIST_Y, ___BOX_W, ___BOX_EDGE_H);
            sb.setColor(color.cpy());
            sb.draw(ArcanistMod.TIP_TOP, x, y[0], ___BOX_W, ___BOX_EDGE_H);
            sb.draw(ArcanistMod.TIP_MID, x, y[0] - h - ___BOX_EDGE_H, ___BOX_W, h + ___BOX_EDGE_H);
            sb.draw(ArcanistMod.TIP_BOT, x, y[0] - h - ___BOX_BODY_H, ___BOX_W, ___BOX_EDGE_H);

            FontHelper.renderSmartText(sb, loreFont, s,x + ___TEXT_OFFSET_X,
                    y[0], ___BODY_TEXT_WIDTH, ___TIP_DESC_LINE_SPACING,
                    textColor);

                y[0] -= h + ___BOX_EDGE_H * 3.15F;
        }
        @SpirePrefixPatch
        public static void TipHelperSpacing(float x, @ByRef float[] y, SpriteBatch sb, ArrayList<String> keywords,
                                            AbstractCard ___card) {
            try {
                Field field = AbstractCard.class.getField("lore");
                if (field.get(___card) == null)
                    return;
                if (keywords.size() >= 3)
                    y[0] += 62.0f * Settings.scale;
                if (keywords.size() >= 2)
                    y[0] += 31.0f * Settings.scale;
            }
            catch (Exception e) {}
        }
    }

    @SpirePatch(
            clz = CardStrings.class,
            method = SpirePatch.CONSTRUCTOR
    )
    public static class CardStringsLoreVarPatch {
        @SpireRawPatch
        public static void rawPatch(CtBehavior ctBehavior) throws NotFoundException, CannotCompileException {
            CtClass cardStringsCtClass = ctBehavior.getDeclaringClass().getClassPool().get(CardStrings.class.getName());
            String fieldSource = "public String LORE;";
            CtField field = CtField.make(fieldSource, cardStringsCtClass);
            cardStringsCtClass.addField(field);
        }
    }

    @SpirePatch(
            clz = AbstractCard.class,
            method = SpirePatch.CONSTRUCTOR,
            paramtypez = {String.class, String.class, String.class, int.class, String.class,
                    AbstractCard.CardType.class, AbstractCard.CardColor.class, AbstractCard.CardRarity.class,
                    AbstractCard.CardTarget.class, DamageInfo.DamageType.class}
    )
    public static class AbstractCardLoreFields {
        @SpireRawPatch
        public static void rawPatch(CtBehavior ctBehavior) throws NotFoundException, CannotCompileException {
            CtClass abstractCardClass = ctBehavior.getDeclaringClass().getClassPool().get(AbstractCard.class.getName());
            String fieldSource1 = "public String lore = null;";
            String fieldSource2 = "public com.badlogic.gdx.graphics.Color loreColor = com.badlogic.gdx.graphics.Color.WHITE.cpy();";
            String fieldSource3 = "public com.badlogic.gdx.graphics.Color loreTextColor = null;";
            CtField field1 = CtField.make(fieldSource1, abstractCardClass);
            CtField field2 = CtField.make(fieldSource2, abstractCardClass);
            CtField field3 = CtField.make(fieldSource3, abstractCardClass);
            abstractCardClass.addField(field1);
            abstractCardClass.addField(field2);
            abstractCardClass.addField(field3);
        }
    }

    public static BitmapFont prepFont(float size, boolean isLinearFiltering) {
        FreeTypeFontGenerator g = new FreeTypeFontGenerator(fontFile);

        if (Settings.BIG_TEXT_MODE) {
            size *= 1.2F;
        }

        float fontScale = 1.0f;

        FreeTypeFontGenerator.FreeTypeFontParameter p = new FreeTypeFontGenerator.FreeTypeFontParameter();
        p.characters = "";
        p.incremental = true;
        p.size = Math.round(size * fontScale * Settings.scale);
        p.gamma = 0.9f;
        p.spaceX = 0;
        p.spaceY = 0;
        p.borderColor = new Color(0.4F, 0.1F, 0.1F, 1.0F);
        p.borderStraight = false;
        p.borderWidth = 0.0f;
        p.borderGamma = 0.9f;
        p.shadowColor = new Color(0, 0, 0, 0);
        p.shadowOffsetX = 0;
        p.shadowOffsetY = 0;
        if (isLinearFiltering) {
            p.minFilter = Texture.TextureFilter.Linear;
            p.magFilter = Texture.TextureFilter.Linear;
        } else {
            p.minFilter = Texture.TextureFilter.Nearest;
            p.magFilter = Texture.TextureFilter.MipMapLinearNearest;
        }

        g.scaleForPixelHeight(p.size);
        BitmapFont font = g.generateFont(p);
        font.setUseIntegerPositions(!isLinearFiltering);
        font.getData().markupEnabled = true;
        if (LocalizedStrings.break_chars != null) {
            font.getData().breakChars = LocalizedStrings.break_chars.toCharArray();
        }

        font.getData().fontFile = fontFile;
        return font;
    }
}