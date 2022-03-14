package theArcanist.relics;

import theArcanist.TheArcanist;

import static theArcanist.ArcanistMod.makeID;

// Code in ResonatingPower
public class TuningFork extends AbstractArcanistRelic {
    public static final String ID = makeID("TuningFork");
    public static final int BOOST_AMOUNT = 2;

    public TuningFork() {
        super(ID, RelicTier.COMMON, LandingSound.MAGICAL, TheArcanist.Enums.ARCANIST_BLARPLE_COLOR);
        amount = BOOST_AMOUNT;
    }
}
