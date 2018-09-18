package io.raspberrywallet.manager.modules;

public class ExampleModule extends Module {

    @Override
    public String getDescription() {
        return "An example waiting and xoring module to show how things work.";
    }

    public static final byte[] KEY = "EXAMPLEKEY".getBytes();

    /*
    * Before we can decrypt a keypart, we need an encrypted one
    * */
    @Override
    public byte[] encryptInput(byte[] data, Object... params) {
        byte[] r = data.clone();
        for (int i = 0; i < r.length; ++i)
            r[i] = (byte) (r[i] ^ KEY[i % KEY.length]);
        return r;
    }

    /*
    * First the module is "registered" by manager just after user needs to decrypt keypart.
    */
    private long lastTime = 1000;

    @Override
    public void register() {
        lastTime = System.currentTimeMillis();
        setStatusString("Wait 5 seconds for decryption to start");
    }

    /*
    * The manager checks periodically for status. If status is true (i.e. here 5 seconds passed),
    * process() should be called
    */
    @Override
    public boolean check() {
        if (System.currentTimeMillis() - lastTime > 5000)
            return true;
        return false;
    }

    /*
    * We are processing (decrypting) the keypart with a KEY.
    * */
    @Override
    public void process() {
        decrypt(new Decrypter() {
            @Override
            public synchronized byte[] decrypt(byte[] payload) throws DecryptionException {

                if (payload == null) throw new Module.DecryptionException(Module.DecryptionException.NO_DATA);

                byte[] r = payload.clone();

                for (int i = 0; i < r.length; ++i)
                    r[i] = (byte) (r[i] ^ KEY[i % KEY.length]);

                return r;
            }
        });
    }
    
}
