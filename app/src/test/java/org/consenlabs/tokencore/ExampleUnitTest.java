package org.consenlabs.tokencore;

import com.squareup.okhttp.Interceptor;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.BlockChain;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.DumpedPrivateKey;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.PeerGroup;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.net.discovery.DnsDiscovery;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.store.MemoryBlockStore;
import org.bitcoinj.store.SPVBlockStore;
import org.bitcoinj.uri.BitcoinURI;
import org.bitcoinj.utils.BriefLogFormatter;
import org.bitcoinj.wallet.DeterministicKeyChain;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.KeyChain;
import org.bitcoinj.wallet.Wallet;
import org.consenlabs.tokencore.wallet.address.SegWitBitcoinAddressCreator;
import org.consenlabs.tokencore.wallet.model.BIP44Util;
import org.junit.Test;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private static final NetworkParameters PARAMETERS = TestNet3Params.get();
    private static final WalletAppKit KIT = new WalletAppKit(PARAMETERS, new File("."), "wallet-test");

    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);

    }

    //n1zQ6xGVxgE5sjo5wa4BR45HZ9js4S2U8t
    public static void main(String[] args) throws Exception {
        // We use the WalletAppKit that handles all the boilerplate for us. Have a look at the Kit.java example for more details.
        NetworkParameters params = TestNet3Params.get();
        WalletAppKit kit = new WalletAppKit(params, new File("."), "getbalance-example");
        String code = "pottery spell awkward attitude squirrel welcome speak canvas physical little train track";
        kit.restoreWalletFromSeed(new DeterministicSeed(code, null, "", System.currentTimeMillis()));

        long start = System.currentTimeMillis();
        System.out.println("开始同步区块链,请稍后...");
        //异步同步区块
//        kit.setAutoSave(true);
        kit.startAsync();
        //等待同步区块完成(阻塞式)
        kit.awaitRunning();

        DeterministicKeyChain keyChain = kit.wallet().getActiveKeyChain();

        DeterministicKey key = keyChain.getKeyByPath(BIP44Util.generatePath("m/44'/1'/0'" + "/0/0"), true);
        kit.wallet().importKey(key);
//        BlockChain blockChain = new BlockChain(params, kit.wallet(), new MemoryBlockStore(params));
//
//        PeerGroup peerGroup = new PeerGroup(params, blockChain);
//        peerGroup.addPeerDiscovery(new DnsDiscovery(params));
//        peerGroup.addWallet(kit.wallet());
//        peerGroup.startAsync();

        Coin balance = kit.wallet().getBalance();
        long end = System.currentTimeMillis();
        System.out.println("余额: " + balance.toFriendlyString() + "  总耗时:" + (end - start));

//        peerGroup.downloadBlockChain();
//        peerGroup.stopAsync();

    }

    @Test
    public void restoreWallet() throws Exception {
        NetworkParameters parameters = TestNet3Params.get();
        String code = "pottery spell awkward attitude squirrel welcome speak canvas physical little train track";
        DeterministicSeed seed = new DeterministicSeed(code, null, "", 0L);
        Wallet wallet = Wallet.fromSeed(parameters, seed);
        DeterministicKeyChain keyChain = wallet.getActiveKeyChain();
        System.out.println("余额:" + wallet.getBalance(Wallet.BalanceType.AVAILABLE_SPENDABLE).toFriendlyString());
        //"m":由主私钥衍生出的私钥; 44':bip44标准;1':币种,这里指的是Testnet上的比特币;0':account;
        DeterministicKey key = keyChain.getKeyByPath(BIP44Util.generatePath("m/44'/1'/0'" + "/0/0"), true);
        String address = key.toAddress(parameters).toBase58();
        System.out.println("钱包地址:(n1zQ6xGVxgE5sjo5wa4BR45HZ9js4S2U8t)==" + address);

    }

    @Test
    public void getBalance()throws Exception{
        BriefLogFormatter.init();
        NetworkParameters params = TestNet3Params.get();
        String code = "pottery spell awkward attitude squirrel welcome speak canvas physical little train track";
        DeterministicSeed seed = new DeterministicSeed(code, null, "", 0L);
        Wallet wallet = Wallet.fromSeed(params, seed);

        BlockChain blockChain = new BlockChain(params,wallet, new SPVBlockStore(params,new File(new File("."),"balance.spvchain")));
        PeerGroup peerGroup = new PeerGroup(params, blockChain);
        peerGroup.addPeerDiscovery(new DnsDiscovery(params));
        peerGroup.addWallet(wallet);
        peerGroup.start();
        peerGroup.downloadBlockChain();
        System.out.println("余额:" + wallet.getBalance().toFriendlyString());

    }

}