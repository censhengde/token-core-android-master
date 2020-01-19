/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.consenlabs.tokencore.examples1_4.src.main.java.org.bitcoinj.examples;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.subgraph.orchid.encoders.Hex;

import org.bitcoinj.core.*;
import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.Wallet;
import org.bitcoinj.wallet.Wallet.BalanceType;

import java.io.File;
import java.util.ArrayList;

/**
 * The following example shows you how to create a SendRequest to send coins from a wallet to a given address.
 */
public class SendRequest {

    public static void main(String[] args) throws Exception {

        // We use the WalletAppKit that handles all the boilerplate for us. Have a look at the Kit.java example for more details.
        NetworkParameters params = TestNet3Params.get();
        WalletAppKit kit = new WalletAppKit(params, new File("."), "sendrequest-example");
        String code="pottery spell awkward attitude squirrel welcome speak canvas physical titlle train track";
//        byte[] seed= MnemonicCode.toSeed(new ArrayList<String>(){"pottery"," spell awkward attitude squirrel welcome speak canvas physical titlle train track "},"123");
        kit.restoreWalletFromSeed(new DeterministicSeed(code,null,"123",System.currentTimeMillis()));

        System.out.println("开始同步区块链,请稍后..." );
        //异步同步区块
        kit.startAsync();
        //等待同步区块完成(阻塞式)
        kit.awaitRunning();
        Coin balance = kit.wallet().getBalance();
        System.out.println("余额: " + balance.toFriendlyString());
//        System.out.println("You have " + Coin.FRIENDLY_FORMAT.format(wallet.getBalance()));

        // How much coins do we want to send?
        // The Coin class represents a monetary Bitcoin value.
        // We use the parseCoin function to simply get a Coin instance from a simple String.
        Coin value = Coin.parseCoin("0.09");//要交易币数

        // To which address you want to send the coins?
        // The Address class represents a Bitcoin address.
        Address to = Address.fromBase58(params, "mupBAFeT63hXfeeT4rnAUcpKHDkz1n4fdw");//目标地址
        System.out.println("Send money to: " + to.toString());

        // There are different ways to create and publish a SendRequest. This is probably the easiest one.
        //这里有多种不同方式创建一个 SendRequest ,这可能是最简单的一种
        // Have a look at the code of the SendRequest class to see what's
        // happening and what other options you
        // have: https://bitcoinj.github.io/javadoc/0.11/com/google/bitcoin/core/Wallet.SendRequest.html
        // 查看一下SendRequest类的代码，看看发生了什么,以及您有哪些其他选项

        // Please note that this might raise a InsufficientMoneyException if your wallet has not enough coins to spend.
        //请注意，如果你的钱包没有足够的硬币来消费，这可能会引发InsufficientMoneyException。

        // When using the testnet you can use a faucet (like the http://faucet.xeno-genesis.com/) to get testnet coins.
        //使用testnet时，您可以使用一个水龙头(例如http://faucet.xeno.genesis.com/)来获得testnet币。

        // In this example we catch the InsufficientMoneyException and register a BalanceFuture callback that runs once the wallet has enough balance.
        //在这个例子中，我们捕获InsufficientMoneyException并注册一个BalanceFuture回调，一旦钱包有足够的余额运行。
        try {
            Wallet.SendResult result = kit.wallet().sendCoins(kit.peerGroup(), to, value);
            System.out.println("coins sent. transaction hash: " + result.tx.getHashAsString());
            // you can use a block explorer like https://www.biteasy.com/ to inspect the transaction with the printed transaction hash. 
            //您可以使用类似https://www.biteasy.com/这样的块资源管理器来检查带有打印事务散列的事务。
        } catch (InsufficientMoneyException e) {
            e.printStackTrace();
            System.out.println("Not enough coins in your wallet. Missing " + e.missing.getValue() + " satoshis are missing (including fees)");
            System.out.println("Send money to: " + kit.wallet().currentReceiveAddress().toString());

            // Bitcoinj allows you to define a BalanceFuture to execute a callback once your wallet has a certain balance.
            // Bitcoinj允许你定义一个BalanceFuture来执行一个回调，一旦你的钱包有一定的余额

            // Here we wait until the we have enough balance and display a notice.
            //在这里我们一直等到我们有足够的余额并显示一个通知。

            // Bitcoinj is using the ListenableFutures of the Guava library. Have a look here for more information: https://github.com/google/guava/wiki/ListenableFutureExplained
            // Bitcoinj使用的是番石榴库的ListenableFutures。
            // 点击这里查看更多信息:https://github.com/google/guava/wiki/ListenableFutureExplained
            ListenableFuture<Coin> balanceFuture = kit.wallet().getBalanceFuture(value, BalanceType.AVAILABLE);
            FutureCallback<Coin> callback = new FutureCallback<Coin>() {
                @Override
                public void onSuccess(Coin balance) {
                    System.out.println("coins arrived and the wallet now has enough balance");
                }

                @Override
                public void onFailure(Throwable t) {
                    System.out.println("something went wrong");
                }
            };
            Futures.addCallback(balanceFuture, callback);
        }

        // shutting down 
        //kit.stopAsync();
        //kit.awaitTerminated();
    }
}
