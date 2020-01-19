package org.consenlabs.tokencore.examples1_4.src.main.java.org.bitcoinj.examples;

import java.util.concurrent.ExecutionException;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.InsufficientMoneyException;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.PeerGroup;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.net.discovery.DnsDiscovery;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.script.Script.ScriptType;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.UnreadableWalletException;
import org.bitcoinj.wallet.Wallet;
import org.bitcoinj.wallet.Wallet.SendResult;
import org.slf4j.event.Level;

public class TransactionTest {
	private static NetworkParameters parameters = TestNet3Params.get();

	public static void restoreFromSeed(String mnemonicCode, String passphrase)
			throws UnreadableWalletException, InsufficientMoneyException {
		long creationTimeSeconds = System.currentTimeMillis();
		DeterministicSeed seed = new DeterministicSeed(mnemonicCode, null, passphrase, creationTimeSeconds);
		Wallet wallet = Wallet.fromSeed(parameters, seed);
		System.out.println("wallet:" + wallet);
		// 添加utxo
		UTXOProviderImpl utxoProviderImpl = new UTXOProviderImpl();
		wallet.setUTXOProvider(utxoProviderImpl);

		// 发送交易
		Coin value = Coin.parseCoin("0.001");
		Address destination = Address.fromBase58(parameters, "moDs8ZfNsz7KsZu3iaevfoy9onyF8ymMV3");
		PeerGroup peerGroup = new PeerGroup(parameters);
		peerGroup.addPeerDiscovery(new DnsDiscovery(parameters));
		peerGroup.addWallet(wallet);
		peerGroup.start();
		System.out.println("wallet:" + wallet);
		System.out.println("CoinSelector:" + wallet.getCoinSelector());
		
		SendResult sendResult = wallet.sendCoins(peerGroup, destination, value);

		System.out.println("wallet:" + wallet);
		
		try {
			Transaction tx = sendResult.broadcastComplete.get();
			System.out.println(tx);
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		
	}

	public static void main(String[] args) {
//		Configurator.setRootLevel(Level.INFO);
		
		String mnemonicCode = "shaft sad frost you yellow emerge actress grace tent husband rubber ribbon";
		try {
			restoreFromSeed(mnemonicCode, "");
		} catch (UnreadableWalletException e) {
			e.printStackTrace();
		} catch (InsufficientMoneyException e) {
			e.printStackTrace();
		}

	}

}
