package io.raspberrywallet.manager.database;

import java.io.IOException;

/**
 * Fake database for debug purposes
 */
public class MockDatabaseFactory {

    public static MockDatabaseFactory getInstance() {
        return new MockDatabaseFactory();
    }
    private Database database;
    WalletEntity wallet = null;
    private WalletEntity walletEntity;

    public MockDatabaseFactory() {
        try {
            database = new Database(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MockDatabaseFactory placeKeyPart(byte[] payload) {
        KeyPartEntity kp = new KeyPartEntity();
        kp.payload = payload.clone();
        if (walletEntity == null) walletEntity = new WalletEntity();
        walletEntity.getParts().add(kp);
        return this;
    }

    public MockDatabaseFactory pushWallet() {
        wallet = walletEntity;
        walletEntity = null;
        return this;
    }

    public Database getDatabase() {
        database.setWallet(wallet);
        return database;
    }

}
