package data;

import java.util.Optional;

public class SoundCard {

    private Optional<USB> usb;

    public SoundCard(){
        this.usb = Optional.of(new USB());
    }
    public Optional<USB> getUSB() {
        return this.usb;
    }

}
