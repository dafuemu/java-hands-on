package data;

import java.util.Optional;

public class Computer {

    private Optional<SoundCard> soundCard;

    public Computer() {
        this.soundCard = Optional.of(new SoundCard());
    }
    public Optional<SoundCard> getSoundCard() {
        return this.soundCard;
    }
}
