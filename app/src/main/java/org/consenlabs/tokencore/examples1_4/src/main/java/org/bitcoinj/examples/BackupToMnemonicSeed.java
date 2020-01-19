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

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Utils;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.Wallet;

/**
 * The following example shows you how to create a deterministic seed from a hierarchical deterministic wallet represented as a mnemonic code.
 * This seed can be used to fully restore your wallet. The RestoreFromSeed.java example shows how to load the wallet from this seed.
 * 
 * In Bitcoin Improvement Proposal (BIP) 39 and BIP 32 describe the details about hierarchical deterministic wallets and mnemonic sentences
 * https://github.com/bitcoin/bips/blob/master/bip-0039.mediawiki
 * https://github.com/bitcoin/bips/blob/master/bip-0032.mediawiki
 */
public class BackupToMnemonicSeed {

    public static void main(String[] args) {

        NetworkParameters params = TestNet3Params.get();//测试网络
        Wallet wallet = new Wallet(params);//创建钱包

        DeterministicSeed seed = wallet.getKeyChainSeed();//获取种子
        System.out.println("seed: " + seed.toString());

        System.out.println("creation time: " + seed.getCreationTimeSeconds());//创建时间戳
        System.out.println("mnemonicCode: " + Utils.join(seed.getMnemonicCode()));//获取助记词
    }
}
