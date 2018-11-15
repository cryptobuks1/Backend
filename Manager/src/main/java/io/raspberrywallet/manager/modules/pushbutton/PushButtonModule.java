package io.raspberrywallet.manager.modules.pushbutton;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import io.raspberrywallet.contract.RequiredInputNotFound;
import io.raspberrywallet.manager.Configuration;
import io.raspberrywallet.manager.modules.Module;

public class PushButtonModule extends Module<PushButtonConfig> {
    public static String PRESSED = "pressed";
    private final static Pin BUTTON_GPIO_PINS = RaspiPin.GPIO_23;

    private final GpioController gpio;
    private final GpioPinDigitalInput pushButton;

    // For test purposes only
    public PushButtonModule(GpioController gpio) throws InstantiationException, IllegalAccessException {
        super("Press Button", PushButtonConfig.class);
        this.gpio = gpio;
        pushButton = gpio.provisionDigitalInputPin(BUTTON_GPIO_PINS);
        initialize();
    }

    public PushButtonModule() throws InstantiationException, IllegalAccessException {
        super("Press Button", PushButtonConfig.class);
        gpio = GpioFactory.getInstance();
        pushButton = gpio.provisionDigitalInputPin(BUTTON_GPIO_PINS);
        initialize();
    }

    public PushButtonModule(Configuration.ModulesConfiguration modulesConfiguration) throws InstantiationException, IllegalAccessException {
        super("Press Button", modulesConfiguration, PushButtonConfig.class);
        gpio = GpioFactory.getInstance();
        pushButton = gpio.provisionDigitalInputPin(BUTTON_GPIO_PINS);
        initialize();
    }
    
    private void initialize() {
        pushButton.addListener((GpioPinListenerDigital) event ->
                setInput(PRESSED, event.getState().isHigh() + "")
        );
    }
    
    @Override
    public byte[] encrypt(byte[] data) {
        return data;
    }
    
    @Override
    public byte[] decrypt(byte[] payload) {
        return payload;
    }
    
    @Override
    protected void validateInputs() throws RequiredInputNotFound {
        if (!hasInput(PRESSED) || !Boolean.parseBoolean(getInput(PRESSED)))
            throw new RequiredInputNotFound(getId(), PRESSED);
    }
    
    @Override
    public String getDescription() {
        return "Module for pushing physical button on the hardware wallet.";
    }
    
    @Override
    public String getHtmlUi() {
        return null;
    }

}
