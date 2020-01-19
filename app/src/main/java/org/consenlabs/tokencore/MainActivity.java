package org.consenlabs.tokencore;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import org.consenlabs.tokencore.wallet.Identity;
import org.consenlabs.tokencore.wallet.KeystoreStorage;
import org.consenlabs.tokencore.wallet.Wallet;
import org.consenlabs.tokencore.wallet.WalletManager;
import org.consenlabs.tokencore.wallet.model.Metadata;
import org.consenlabs.tokencore.wallet.model.Network;
import org.consenlabs.tokencore.wallet.transaction.EthereumTransaction;
import org.consenlabs.tokencore.wallet.transaction.TxSignResult;

import java.math.BigInteger;
import java.io.File;

public class MainActivity extends AppCompatActivity implements KeystoreStorage {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WalletManager.storage = this;
        WalletManager.scanWallets();

    }

    @Override
    public File getKeystoreDir() {
        return null;
    }


    private void exportWallet() {


        // You should create or recover Identity first before you create other wallets
        // The last param, Metadata.P2WPKH means that the derived btc wallet is a SegWit wallet
        Identity identity = Identity.createIdentity("MyFirstIdentity",
                SampleKey.PASSWORD, SampleKey.PASSWORD_HINT, Network.MAINNET, Metadata.HD);
//identity.getw

        Wallet ethereumWallet = identity.getWallets().get(0);
        Wallet bitcoinWallet = identity.getWallets().get(1);

        String prvKey = WalletManager.exportPrivateKey(ethereumWallet.getId(), SampleKey.PASSWORD);
        System.out.println(String.format("PrivateKey: %s", prvKey));
        String mnemonic = WalletManager.exportMnemonic(ethereumWallet.getId(), SampleKey.PASSWORD).getMnemonic();
        System.out.println(String.format("Mnemonic: %s", mnemonic));
        String json = WalletManager.exportKeystore(ethereumWallet.getId(), SampleKey.PASSWORD);
        System.out.println(String.format("Keystore: %s", json));

// output:
// PrivateKey: f653be3f639f45ea1ed3eb152829b6d881ce62257aa873891e06fa9569a8d9aa
// Mnemonic: tide inmate cloud around wise bargain celery cement jungle melody galaxy grocery
// Keystore: {"id":"c7575eba-3ae3-4cc3-86ba-2eb9c6839cad",
// "version":3,
// "crypto":{"ciphertext":"7083ba3dd5470ba4be4237604625e05fa6b668954d270beb848365cbf6933ec5",
// "mac":"f4f9ea8d42ff348b11fc146c396da446cc975309b3538e08a58c0b218bddd15d",
// "cipher":"aes-128-ctr","cipherparams":{"iv":"db3f523faf4da4f1c6edcd7bc1386879"},
// "kdf":"pbkdf2","kdfparams":{"dklen":32,"c":10240,"prf":"hmac-sha256",
// "salt":"0ce830e9f888dfe33c31e6cfc444d6f588161c9d4128d4066ee5dfdcbc5d0079"}},
// "address":"4a1c2072ac67b616e5c578fd9e2a4d30e0158471"}
    }

//    void SignTransaction() {
////        BigInteger nonce=new BigInteger(11,11);
//        BigInteger gasPrice;
//        BigInteger gasLimit;
//        String to;
//        BigInteger value;
//        String data;
//        EthereumTransaction tran = new EthereumTransaction(nonce, gasPrice, gasLimit, to, value, data);
//
//        String chainId;
//        String password;
//        Wallet ethereumWallet;
//        TxSignResult result = tran.signTransaction(chainId, SampleKey.PASSWORD, ethereumWallet);
//        String signedTx = result.getSignedTx(); // This is the signature result which you need to broadcast.
//        String txHash = result.getTxHash(); // This is txHash which you can use for locating your transaction record
//    }
    //foundation:基础
}
