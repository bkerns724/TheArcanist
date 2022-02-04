package theArcanist.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.events.beyond.SensoryStone;
import com.megacrit.cardcrawl.localization.UIStrings;
import javassist.CtBehavior;
import theArcanist.ArcanistMod;

import java.util.ArrayList;
import java.util.Collections;

import static theArcanist.TheArcanist.Enums.THE_ARCANIST;
import static theArcanist.util.Wiz.*;

public class SensoryStonePatch {
    public static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ArcanistMod.makeID("SensoryStone"));
    public static final String[] TEXT = uiStrings.TEXT;

    @SpirePatch(
            clz = SensoryStone.class,
            method = "getRandomMemory"
    )
    public static class RoflPatch {
        @SpireInsertPatch(
                locator = Locator.class,
                localvars={"memories"}
        )
        public static SpireReturn Insert(SensoryStone __instance, ArrayList<String> memories) {
            if (adp().chosenClass == THE_ARCANIST) {
                __instance.imageEventText.updateBodyText(TEXT[0]);
                return SpireReturn.Return(null);
            }
            else
            {
                memories.add(TEXT[0]);
                return SpireReturn.Continue();
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(Collections.class, "shuffle");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}