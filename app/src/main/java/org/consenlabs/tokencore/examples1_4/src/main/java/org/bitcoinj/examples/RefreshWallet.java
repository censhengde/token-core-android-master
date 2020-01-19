/*
 * Copyright 2011 Google Inc.
 * Copyright 2014 Andreas Schildbach
 *
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

import android.content.SyncStatusObserver;

import org.bitcoinj.core.*;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.store.BlockStore;
import org.bitcoinj.store.MemoryBlockStore;
import org.bitcoinj.wallet.Wallet;
import org.bitcoinj.wallet.listeners.WalletCoinsReceivedEventListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * RefreshWallet loads a wallet, then processes the block chain to update the transaction pools within it.
 * * RefreshWallet载入钱包，然后处理区块链以更新其中的交易池。
 * To get a test wallet you can use wallet-tool from the tools subproject.
 * *要获得一个测试钱包，您可以使用来自tools子项目的wallet-tool。
 */
public class RefreshWallet {

    public static void main(String[] args) throws Exception {
//        System.out.println(args[0]);
//        File file = new File("D:\\bitcoij");
////        if (!file.exists()) file.mkdir();
////        Wallet wallet = Wallet.loadFromFile(file);
        final NetworkParameters params = TestNet3Params.get();
        List<ECKey> keys = new ArrayList<>();
//        keys.add(ECKey.fromPrivate())
        Wallet wallet = Wallet.fromKeys(params, keys);
        System.out.println(wallet.toString());

        // Set up the components and link them together.
        //设置组件并将它们链接在一起
        BlockStore blockStore = new MemoryBlockStore(params);
        BlockChain chain = new BlockChain(params, wallet, blockStore);

        final PeerGroup peerGroup = new PeerGroup(params, chain);
        peerGroup.startAsync();

        wallet.addCoinsReceivedEventListener(new WalletCoinsReceivedEventListener() {
            @Override
            public synchronized void onCoinsReceived(Wallet w, Transaction tx, Coin prevBalance, Coin newBalance) {
                System.out.println("\nReceived tx " + tx.getHashAsString());
                System.out.println(tx.toString());
            }
        });

        // Now download and process the block chain.
        peerGroup.downloadBlockChain();
        peerGroup.stopAsync();
//        wallet.saveToFile(file);
        System.out.println("\nDone!\n");
        System.out.println(wallet.toString());
    }
}
