package org.ethereum.android.jsonrpc.method;

import com.thetransactioncompany.jsonrpc2.*;
import com.thetransactioncompany.jsonrpc2.server.*;
import org.ethereum.android.jsonrpc.JsonRpcServerMethod;
import org.ethereum.core.AccountState;
import org.ethereum.core.Block;
import org.ethereum.facade.Ethereum;
import org.spongycastle.util.encoders.Hex;
import java.math.BigInteger;
import java.util.List;

/*
By specification this method can receive number of pending block but from -core it's not possible.
TODO: get advice from Roman about pending block
*/
public class eth_getBlockByNumber extends JsonRpcServerMethod {

    public eth_getBlockByNumber (Ethereum ethereum) {
        super(ethereum);
    }

    protected JSONRPC2Response worker(JSONRPC2Request req, MessageContext ctx) {

        List<Object> params = req.getPositionalParams();
        if (params.size() != 2) {
            return new JSONRPC2Response(JSONRPC2Error.INVALID_PARAMS, req.getID());
        } else {
            String height = (String)params.get(0);
            long blockNumber = getBlockNumber(height);
            Boolean detailed = (Boolean)params.get(1);

            if (blockNumber == -1) {
                blockNumber = ethereum.getBlockchain().getBestBlock().getNumber();
            }
            // TODO: here we must load pending block but -core not "group" it.
            if (blockNumber == -2) {
            }

            Block block = ethereum.getBlockchain().getBlockByNumber(blockNumber);

            JSONRPC2Response res = new JSONRPC2Response(blockToJS(block, detailed), req.getID());
            return res;
        }

    }
}