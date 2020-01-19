package org.consenlabs.tokencore.examples1_4.src.main.java.org.bitcoinj.examples;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.bitcoinj.core.Coin;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.UTXO;
import org.bitcoinj.core.UTXOProvider;
import org.bitcoinj.core.UTXOProviderException;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.script.Script;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.subgraph.orchid.encoders.Hex;

public class UTXOProviderImpl implements UTXOProvider {

	private static Logger logger = LoggerFactory.getLogger(UTXOProviderImpl.class);

	@Override
	public List<UTXO> getOpenTransactionOutputs(List<ECKey> keys) throws UTXOProviderException {
		// " + address + "
		String address = "mxWTKUe4jPqbN4ny9VE18F9PeieDMuoC4g";//支付方地址
		List<UTXO> utxos = Lists.newArrayList();
		OkHttpClient client = new OkHttpClient();
		String url = "https://testnet.blockexplorer.com/api/addr/" + address + "/utxo";
		try {
			String response = client.newCall(new Request.Builder().url(url).build()).execute().body().string();
			logger.info("HTTP Call Result:{}", JsonUtils.jsonToMap(response));
			logger.info("HTTP Call Result:{}", JsonUtils.jsonToObject(response, UTXO.class));
			@SuppressWarnings("unchecked")
			List<Map<String, Object>> outputs = (List<Map<String, Object>>) JsonUtils
					.jsonToMap(String.format("{\"a\":%s}", response)).get("a");

//			JSONObject jsonObject = JSON.parseObject(response);
//            JSONArray unspentOutputs = jsonObject.getJSONArray("unspent_outputs");
//            List<Map> outputs = JSONObject.parseArray(unspentOutputs.toJSONString(), Map.class);

			if (outputs == null || outputs.size() == 0) {
				System.out.println("Not Enough Money Error!");
			}
			for (int i = 0; i < outputs.size(); i++) {
				Map<String, Object> outputsMap = outputs.get(i);
				String txid = outputsMap.get("txid").toString();
				String vout = outputsMap.get("vout").toString();
				String script = outputsMap.get("scriptPubKey").toString();
				String value = outputsMap.get("amount").toString();
				UTXO utxo = new UTXO(Sha256Hash.wrap(txid), Long.valueOf(vout), Coin.parseCoin(value), 0, false,
						new Script(Hex.decode(script)));
				utxos.add(utxo);

			}
			return utxos;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int getChainHeadHeight() throws UTXOProviderException {
		String url = "https://blockchain.info/latestblock";
		OkHttpClient client = new OkHttpClient();
		try {
			String response = client.newCall(new Request.Builder().url(url).build()).execute().body().string();
			logger.info("GetChainHeadHeight Http Call Result:{}", response);
			Map<String, Object> latestBlock = JsonUtils.jsonToMap(response);
			int height = (int) latestBlock.get("height");
			return height;
		} catch (IOException e) {
			logger.error("GetChainHeadHeight Error:Http Call Failed ");
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public NetworkParameters getParams() {
		// TODO 暂时固定为测试网
		return TestNet3Params.get();
	}

}
