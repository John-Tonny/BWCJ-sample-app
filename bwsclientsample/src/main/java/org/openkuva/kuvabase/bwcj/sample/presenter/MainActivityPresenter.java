/*
 * Copyright (c)  2018 One Kuva LLC, known as OpenKuva.org
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted (subject to the limitations in the disclaimer
 * below) provided that the following conditions are met:
 *
 *      * Redistributions of source code must retain the above copyright notice,
 *      this list of conditions and the following disclaimer.
 *
 *      * Redistributions in binary form must reproduce the above copyright
 *      notice, this list of conditions and the following disclaimer in the
 *      documentation and/or other materials provided with the distribution.
 *
 *      * Neither the name of the One Kuva LLC, known as OpenKuva.org nor the names of its
 *      contributors may be used to endorse or promote products derived from this
 *      software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY
 * THIS LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND
 * CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER
 * IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */

package org.openkuva.kuvabase.bwcj.sample.presenter;

import com.google.gson.GsonBuilder;

import org.bitcoinj.core.Coin;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Utils;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bitcoinj.crypto.MnemonicCode;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.wallet.DeterministicSeed;
import org.openkuva.kuvabase.bwcj.data.entity.interfaces.copayer.ICopayer;
import org.openkuva.kuvabase.bwcj.data.entity.interfaces.credentials.ICredentials;
import org.openkuva.kuvabase.bwcj.data.entity.interfaces.masternode.IMasternode;
import org.openkuva.kuvabase.bwcj.data.entity.interfaces.masternode.IMasternodeBroadcast;
import org.openkuva.kuvabase.bwcj.data.entity.interfaces.masternode.IMasternodeCollateral;
import org.openkuva.kuvabase.bwcj.data.entity.interfaces.masternode.IMasternodePing;
import org.openkuva.kuvabase.bwcj.data.entity.interfaces.masternode.IMasternodeRemove;
import org.openkuva.kuvabase.bwcj.data.entity.interfaces.masternode.IMasternodeStatus;
import org.openkuva.kuvabase.bwcj.data.entity.interfaces.transaction.IAtomicswapInitiateData;
import org.openkuva.kuvabase.bwcj.data.entity.interfaces.transaction.ITransactionProposal;
import org.openkuva.kuvabase.bwcj.data.entity.interfaces.wallet.IWallet;
import org.openkuva.kuvabase.bwcj.data.entity.pojo.transaction.AtomicswapInitiateData;
import org.openkuva.kuvabase.bwcj.data.entity.pojo.transaction.AtomicswapParticipateData;
import org.openkuva.kuvabase.bwcj.data.entity.pojo.transaction.AtomicswapRedeemData;
import org.openkuva.kuvabase.bwcj.data.entity.pojo.transaction.AtomicswapRefundData;
import org.openkuva.kuvabase.bwcj.data.entity.pojo.transaction.CustomData;
import org.openkuva.kuvabase.bwcj.domain.useCases.credentials.IInitializeCredentialsUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.exchange.getRate.IGetRateUseCases;
import org.openkuva.kuvabase.bwcj.domain.useCases.masternode.broadcastMasternode.IBroadcastMasternodeUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.masternode.collateral.IGetMasternodeCollateralUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.masternode.getMasterondes.IGetMasternodesUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.masternode.ping.IGetMasternodePingUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.masternode.removeMasternode.IRemoveMasternodeUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.masternode.signMasternode.ISignMasternodeUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.masternode.status.IGetMasternodeStatusUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.addNewAtomicswapInitiateTxp.IAddNewAtomicswapInitiateTxpUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.addNewAtomicswapParticipateTxp.IAddNewAtomicswapParticipateTxpUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.addNewAtomicswapRedeemTxp.IAddNewAtomicswapRedeemTxpUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.addNewAtomicswapRefundTxp.IAddNewAtomicswapRefundTxpUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.addNewTxp.IAddNewTxpUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.broadcastTxp.IBroadcastTxpUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.deleteAllPendingTxProposals.IDeleteAllPendingTxpsUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.getAllPendingAtomicswapTxProposals.IGetAllPendingAtomicswapTxpsUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.publishTxp.IPublishTxpUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.signTxp.ISignTxpUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.wallet.createWallet.CreateWalletUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.wallet.createWallet.ICreateWalletUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.wallet.getWallet.IGetWalletUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.wallet.getWalletAddress.IGetWalletAddressesUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.wallet.getWalletBalance.IGetWalletBalanceUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.wallet.joinWalletInCreation.IJoinWalletInCreationUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.wallet.joinWalletInCreation.JoinWalletInCreationUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.wallet.postWalletAddress.CreateNewMainAddressesFromWalletUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.wallet.postWalletAddress.ICreateNewMainAddressesFromWalletUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.wallet.recoveryWalletFromMnemonic.IRecoveryWalletFromMnemonicUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.getTxHistory.IGetTxHistoryUseCase;
import org.openkuva.kuvabase.bwcj.domain.utils.CopayersCryptUtils;
import org.openkuva.kuvabase.bwcj.domain.utils.Credentials;
import org.openkuva.kuvabase.bwcj.domain.utils.DeriveUtils;
import org.openkuva.kuvabase.bwcj.domain.utils.VircleCoinTypeRetriever;
import org.openkuva.kuvabase.bwcj.domain.utils.atomicswap.CreateContract;
import org.openkuva.kuvabase.bwcj.domain.utils.atomicswap.ExtractContract;
import org.openkuva.kuvabase.bwcj.domain.utils.atomicswap.RedeemContract;
import org.openkuva.kuvabase.bwcj.domain.utils.atomicswap.RefundContract;
import org.openkuva.kuvabase.bwcj.domain.utils.messageEncrypt.SjclMessageEncryptor;
import org.openkuva.kuvabase.bwcj.sample.ApiUrls;
import org.openkuva.kuvabase.bwcj.sample.view.IMainActivityView;
import org.openkuva.kuvabase.bwcj.service.bitcoreWalletService.interfaces.IBitcoreWalletServerAPI;
import org.openkuva.kuvabase.bwcj.service.bitcoreWalletService.interfaces.address.IAddressesResponse;
import org.openkuva.kuvabase.bwcj.service.bitcoreWalletService.interfaces.wallets.IJoinWalletResponse;
import org.openkuva.kuvabase.bwcj.service.bitcoreWalletService.retrofit2.IRetrofit2BwsAPI;
import org.openkuva.kuvabase.bwcj.service.bitcoreWalletService.retrofit2.Retrofit2BwsApiBridge;
import org.openkuva.kuvabase.bwcj.service.bitcoreWalletService.retrofit2.interceptors.BWCRequestSignatureInterceptor;
import org.openkuva.kuvabase.bwcj.service.rate.interfaces.IRateResponse;
import org.openkuva.kuvabase.bwcj.data.entity.interfaces.transaction.ITransactionHistory;

import org.openkuva.kuvabase.bwcj.domain.useCases.masternode.getMasterondes.GetMasternodesUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.masternode.removeMasternode.RemoveMasternodeUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.masternode.status.GetMasternodeStatusUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.masternode.collateral.GetMasternodeCollateralUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.masternode.signMasternode.SignMasternodeUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.masternode.broadcastMasternode.BroadcastMasternodeUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.masternode.ping.GetMasternodePingUseCase;


import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static org.openkuva.kuvabase.bwcj.domain.utils.ListUtils.join;
import static org.openkuva.kuvabase.bwcj.domain.utils.ListUtils.split;
import org.bitcoinj.core.Sha256Hash;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivityPresenter implements IMainActivityPresenter {
    private final IMainActivityView view;
    private final ICredentials credentials;

    private final ICreateWalletUseCase createWallet;
    private final IJoinWalletInCreationUseCase joinWalletInCreationUseCase;
    private final IGetWalletAddressesUseCase addressesUseCases;
    private final IGetWalletBalanceUseCase getWalletBalanceUseCases;
    private final IGetRateUseCases getRateUseCases;
    private final IAddNewTxpUseCase postTransaction;
    private final IPublishTxpUseCase publishTxpUseCase;
    private final ISignTxpUseCase signTxpUseCase;
    private final IBroadcastTxpUseCase broadcastTxpUseCase;
    private final IDeleteAllPendingTxpsUseCase deleteAllPendingTxpsUseCase;
    private final IRecoveryWalletFromMnemonicUseCase recoveryWalletFromMnemonicUseCase;
    private final IBitcoreWalletServerAPI bitcoreWalletServerAPI;
    private final IInitializeCredentialsUseCase initializeCredentialsUseCase;
    private final ICreateNewMainAddressesFromWalletUseCase getWalletAddressesUseCases;
    private final IGetTxHistoryUseCase getTxHistoryUseCase;
    private final IGetMasternodeStatusUseCase getMasternodeStatusUseCase;
    private final IGetMasternodeCollateralUseCase getMasternodeCollateralUseCase;
    private final ISignMasternodeUseCase signMasternodeUseCase;
    private final IBroadcastMasternodeUseCase broadcastMasternodeUseCase;
    private final IGetMasternodesUseCase getMasternodesUseCase;
    private final IRemoveMasternodeUseCase removeMasternodeUseCase;
    private final IGetMasternodePingUseCase getMasternodePingUseCase;
    private final IAddNewAtomicswapInitiateTxpUseCase addNewAtomicswapInitiateTxpUseCase;
    private final IAddNewAtomicswapParticipateTxpUseCase addNewAtomicswapParticipateTxpUseCase;
    private final IAddNewAtomicswapRedeemTxpUseCase addNewAtomicswapRedeemTxpUseCase;
    private final IAddNewAtomicswapRefundTxpUseCase addNewAtomicswapRefundTxpUseCase;
    private final IGetAllPendingAtomicswapTxpsUseCase getAllPendingAtomicswapTxpsUseCase;
    private final IGetWalletUseCase getWalletUseCase;


    public MainActivityPresenter(
            IMainActivityView view,
            ICredentials credentials,
            ICreateWalletUseCase createWallet,
            IJoinWalletInCreationUseCase joinWalletInCreationUseCase,
            IGetWalletAddressesUseCase addressesUseCases,
            IGetWalletBalanceUseCase getWalletBalanceUseCases,
            IGetRateUseCases getRateUseCases,
            IAddNewTxpUseCase postTransaction,
            IPublishTxpUseCase publishTxpUseCase,
            ISignTxpUseCase signTxpUseCase,
            IBroadcastTxpUseCase broadcastTxpUseCase,
            IDeleteAllPendingTxpsUseCase deleteAllPendingTxpsUseCase,
            IRecoveryWalletFromMnemonicUseCase recoveryWalletFromMnemonicUseCase,
            IBitcoreWalletServerAPI bitcoreWalletServerAPI,
            IInitializeCredentialsUseCase initializeCredentialsUseCase,
            ICreateNewMainAddressesFromWalletUseCase getWalletAddressesUseCases,
            IGetTxHistoryUseCase getTxHistoryUseCase,
            IGetMasternodeStatusUseCase getMasternodeStatusUseCase,
            IGetMasternodeCollateralUseCase getMasternodeCollateralUseCase,
            ISignMasternodeUseCase signMasternodeUseCase,
            IBroadcastMasternodeUseCase broadcastMasternodeUseCase,
            IGetMasternodesUseCase getMasternodesUseCase,
            IRemoveMasternodeUseCase removeMasternodeUseCase,
            IGetMasternodePingUseCase getMasternodePingUseCase,
            IAddNewAtomicswapInitiateTxpUseCase addNewAtomicswapInitiateTxpUseCase,
            IAddNewAtomicswapParticipateTxpUseCase addNewAtomicswapParticipateTxpUseCase,
            IAddNewAtomicswapRedeemTxpUseCase addNewAtomicswapRedeemTxpUseCase,
            IAddNewAtomicswapRefundTxpUseCase addNewAtomicswapRefundTxpUseCase,
            IGetAllPendingAtomicswapTxpsUseCase getAllPendingAtomicswapTxpsUseCase,
            IGetWalletUseCase getWalletUseCase
            ) {

        this.view = view;
        this.credentials = credentials;
        this.createWallet = createWallet;
        this.joinWalletInCreationUseCase = joinWalletInCreationUseCase;
        this.addressesUseCases = addressesUseCases;
        this.getWalletBalanceUseCases = getWalletBalanceUseCases;
        this.getRateUseCases = getRateUseCases;
        this.postTransaction = postTransaction;
        this.publishTxpUseCase = publishTxpUseCase;
        this.signTxpUseCase = signTxpUseCase;
        this.broadcastTxpUseCase = broadcastTxpUseCase;
        this.deleteAllPendingTxpsUseCase = deleteAllPendingTxpsUseCase;
        this.recoveryWalletFromMnemonicUseCase = recoveryWalletFromMnemonicUseCase;
        this.bitcoreWalletServerAPI = bitcoreWalletServerAPI;
        this.initializeCredentialsUseCase = initializeCredentialsUseCase;
        this.getWalletAddressesUseCases = getWalletAddressesUseCases;

        this.getTxHistoryUseCase = getTxHistoryUseCase;
        this.getMasternodeStatusUseCase = getMasternodeStatusUseCase;
        this.getMasternodeCollateralUseCase  = getMasternodeCollateralUseCase;
        this.signMasternodeUseCase = signMasternodeUseCase;
        this.broadcastMasternodeUseCase = broadcastMasternodeUseCase;
        this.getMasternodesUseCase = getMasternodesUseCase;
        this.removeMasternodeUseCase = removeMasternodeUseCase;
        this.getMasternodePingUseCase = getMasternodePingUseCase;
        this.addNewAtomicswapInitiateTxpUseCase = addNewAtomicswapInitiateTxpUseCase;
        this.addNewAtomicswapParticipateTxpUseCase = addNewAtomicswapParticipateTxpUseCase;
        this.addNewAtomicswapRedeemTxpUseCase = addNewAtomicswapRedeemTxpUseCase;
        this.addNewAtomicswapRefundTxpUseCase = addNewAtomicswapRefundTxpUseCase;
        this.getAllPendingAtomicswapTxpsUseCase = getAllPendingAtomicswapTxpsUseCase;
        this.getWalletUseCase = getWalletUseCase;
    }

    @Override
    public void createWallet() {
        try {
            // view.showMnemonic(join(initializeCredentialsUseCase.execute(""), " "));
            view.showMessage(join(this.credentials.getMnemonic(), ""));
            String walletId = createWallet.execute();
            joinWalletInCreationUseCase.execute(walletId);
            getWalletAddressesUseCases.create();
            view.updateWalletId(walletId);
        } catch (Exception e) {
            view.showMessage(e.getMessage());
        }
    }

    @Override
    public void getAddress(String address) {
        try {
            IAddressesResponse response = addressesUseCases.execute(address);
            view.updateWalletAddress(response.getAddress());
        } catch (Exception e) {
            view.showMessage(e.getMessage());
        }
    }

    @Override
    public void getBalance() {
        try {
            String name3 = System.mapLibraryName("java.lang.System");
            IWallet wallet = getWalletBalanceUseCases.execute();
            view.updateWalletBalance(String.valueOf(wallet.getBalance().getAvailableAmount()));
            String name = wallet.getWalletCore().getName();
            String name1 = wallet.getWalletCore().getCopayers()[0].getName();
            String customData = wallet.getWalletCore().getCopayers()[0].getCustomData();
            System.out.println("aaa");

        } catch (Exception e) {
            view.showMessage(e.getMessage());
        }
    }

    @Override
    public void sendDashToAddress(String address, String dash, String msg) {
        try {
            // IRateResponse rate = getRateUseCases.execute();
            ITransactionProposal proposal =
                    postTransaction.execute(
                            address,
                            Coin.parseCoin(dash).value,
                            msg,
                            false,
                            /*new CustomData(
                                    rate.getRate(),
                                    "send",
                                    null,
                                    null,
                                    null),
                             */
                            msg,
                            true);
            ITransactionProposal publishedTxp = publishTxpUseCase.execute(proposal);
            ITransactionProposal signTxp = signTxpUseCase.execute(publishedTxp);
            // broadcastTxpUseCase.execute(signTxp.getId());
            view.updateSendDashResult("Tx done!");
        } catch (Exception e) {
            view.showMessage(e.getMessage());
        }
    }

    @Override
    public void onRecovery(String mnemonic) {
        try {
            /*IWallet response =
                    recoveryWalletFromMnemonicUseCase.execute(split(mnemonic), "", new ECKey());*/
            /*IWallet response =
                    recoveryWalletFromMnemonicUseCase.execute(split(mnemonic), "", credentials.getWalletPrivateKey());
             */

            IWallet response =
                    recoveryWalletFromMnemonicUseCase.execute(split(mnemonic), "");
            /*
            IWallet response =
                    getWalletUseCase.execute();
            */

            String name = response.getWalletCore().getCopayers()[0].getName();
            view.updateWalletBalance(
                    String.valueOf(
                            response
                                    .getBalance()
                                    .getAvailableAmount()));
            view.updateWalletId(
                    response
                            .getWalletCore()
                            .getId());
        } catch (Exception e) {
            view.showMessage(e.getMessage());
        }
    }

    @Override
    public void deleteAllPendingTxp() {
        deleteAllPendingTxpsUseCase.execute();
    }

    @Override
    public void getTxHistory(Integer skip, Integer limit, String tokenAddress, Integer includeExtendedInfo) {
         try{
            ITransactionHistory[] txs = getTxHistoryUseCase.execute(skip, limit, tokenAddress, includeExtendedInfo);
            System.out.println(txs);
         } catch (Exception e) {
             view.showMessage(e.getMessage());
         }
    }

    @Override
    public void getMasternodeStatus(String coin, String txid, String address, String payee) {
        try {
            IMasternodeStatus txs = getMasternodeStatusUseCase.execute(coin, txid, address, payee );
            System.out.println(txs);
        } catch (Exception e) {
            view.showMessage(e.getMessage());
        }
    }

    @Override
    public void getMasternodeCollateral() {
        try {
           IMasternodeCollateral[] txs = getMasternodeCollateralUseCase.execute();
            System.out.println(txs);

            view.updateContract(txs[0].getTxid());
            view.updateSecret(String.valueOf(txs[0].getVout()));

        } catch (Exception e) {
            view.showMessage(e.getMessage());
        }
    }

    @Override
    public void signMasternode(String coin,
                               String txid,
                               int vout,
                               String signPrivKey,
                               long pingHeight,
                               String pingHash,
                               String privKey,
                               String address,
                               int port) {
        try {
            String rawHex =  signMasternodeUseCase.execute(coin, txid, vout, signPrivKey, pingHeight, pingHash, privKey, address, port);
            System.out.println(rawHex);
        } catch (Exception e) {
            view.showMessage(e.getMessage());
        }
    }

    @Override
    public void broadcastMasternode(String coin, String rawTx) {
        try {
            IMasternodeBroadcast ret = broadcastMasternodeUseCase.execute(coin, rawTx);
            System.out.println(ret);
        } catch (Exception e) {
            view.showMessage(e.getMessage());
        }
    }

    @Override
    public  void getMasternodes(String coin, String txid, String address, String payee) {
        try {
            IMasternode[] txs = getMasternodesUseCase.execute(coin, txid, address, payee);
            System.out.println(txs);

        } catch (Exception e) {
            view.showMessage(e.getMessage());
        }
    }

    @Override
    public void removeMasternodes(String coin, String txid, int vout) {
        try {
            IMasternodeRemove ret = removeMasternodeUseCase.execute(coin, txid, vout);
            System.out.println(ret);
            if(ret.getN() == 0) {
                view.showMessage("no remove masternode");
            }else if(ret.getN()>0 && ret.getOk().equalsIgnoreCase("1")){
                view.showMessage("remove masternode is successful");
            }
        } catch (Exception e) {
            view.showMessage(e.getMessage());
        }
    }

    @Override
    public void getMasternodePing(String coin, String txid, int vout) {
        try {
            IMasternodePing ret = getMasternodePingUseCase.execute(coin, txid, vout);
            System.out.println(ret);
        } catch (Exception e) {
            view.showMessage(e.getMessage());
        }
    }

    @Override
    public void activateMasternode(String coin, String txid, int vout, String masternodeKey, String address, int port, ICredentials credentials) {
        try {
            IMasternodePing ret = getMasternodePingUseCase.execute(coin, txid, vout);
            CopayersCryptUtils copayersCryptUtils = new CopayersCryptUtils(new VircleCoinTypeRetriever());
            DeterministicKey xpriv = copayersCryptUtils.derivedXPrivKey(credentials.getSeed(), credentials.getNetworkParameters());
            DeterministicKey priv = DeriveUtils.deriveChildByPath(xpriv, ret.getPath());
            String signPrivKey = Utils.HEX.encode(priv.getPrivKeyBytes());
            String pingHash = ret.getPingHash();
            // String pingHash = "0000000003e5502708a07606f3c6e1e6d5b7c343df5da9d9b7be18d78eda5027";
            String rawHex = signMasternodeUseCase.execute(coin, txid, vout, signPrivKey, ret.getPingHeight(), pingHash, masternodeKey, address, port);
            IMasternodeBroadcast ret1 = broadcastMasternodeUseCase.execute(rawHex, masternodeKey);
            if(ret1.isSuccessful()){
                view.showMessage("activate masternode is successful");
            }
            String errMsg = ret1.getErrorMessage();
            System.out.println(ret1);
        } catch (Exception e) {
            view.showMessage(e.getMessage());
        }
    }

    public void test_atomicswap() {
        String pubKey= "0241137b959dd5c65cabd7f241869b3e73c6df3129bd2f415a644cca9665bf7bcd";
        String bb = ECKey.fromPublicOnly(Utils.HEX.decode(pubKey)).toAddress(this.credentials.getNetworkParameters()).toBase58();

        NetworkParameters parameters =  this.credentials.getNetworkParameters();
        ECKey ecKey = new ECKey();
        byte[] btSecret = ecKey.getPrivKeyBytes();
        String secret = Utils.HEX.encode(btSecret);
        String secretHash = Utils.HEX.encode(Sha256Hash.hash(btSecret));

        String themAddr = "SYcyNS956KQzKjMfyPL8UkeHtuHM7gZz5C";
        String meAddr = "SfoimqkZktJDFn9jcZQ4DVsVf4tT6MpTEv";
        CreateContract contract = new CreateContract(themAddr, meAddr, true, parameters);
        System.out.println(contract);

        String rawTx = "0200000001fecbcc72c2cc2ef82b5d755b5a72a02a00284220d9d25676da1df25223b012ea00000000ef473044022003e6908ba48043a81df603c2ce102b1b24349bfbdec1fa9fb6054405b1becff502204b1cca0d4e42e723de9afc294e37f9b4ec5dde4cf6c8a75bd6770bbb52b4e38e012103e2d60572ab0d7f70ab254e95e13f21b1335855cfb7bf293e818aec567af5d84b209aa0433e6555eeefede8b261c5b81ea0702c37d64a539a7a9e0ea62304c0011e514c616382012088a8209582448f54d28020f3c763eb6968f545800db9fd642ad0b9667c6c8c7a4bc2058876a914218618a385ac4cd893e0ee3c6654dcb14a0453af6704bdf28060b17576a91499c189a60774ef73f28dc86ae80fd2f3f46d2c4b6888acffffffff0170b60600000000001976a9145642f95015a3fe415b462eda9fdc2ddac4e1ad1688acbdf28060";
        ExtractContract extractContract = new ExtractContract(rawTx, parameters);

        String sigAndPub = "473044022069fa51b2a5db0bee08aa6443f8bcd61fec9d2823381ec0cc29e7c9141f5ca7020220615e76356e2923ac8e943abe9fc5ac29cd5839f4a3fc2262ee937e0905dc049601210241137b959dd5c65cabd7f241869b3e73c6df3129bd2f415a644cca9665bf7bcd";
        RefundContract refundContract = new RefundContract(contract.getContract(), sigAndPub, contract.getSecret(), parameters);
        RedeemContract redeemContract = new RedeemContract(contract.getContract(), sigAndPub,contract.getSecret(), parameters);

        System.out.println(redeemContract.getRedeemContract());
    }

    @Override
    public void atomicswapInitiate(String address, String dash, String msg) {
        try {
            // IRateResponse rate = getRateUseCases.execute();
            ITransactionProposal proposal=
                    addNewAtomicswapInitiateTxpUseCase.execute(
                            address,
                            Coin.parseCoin(dash).value,
                            false,
                            /*
                            new CustomData(
                                    "123", // rate.getRate(),
                                    "send",
                                    null,
                                    null,
                                    null),
                             */
                            msg,
                            true
                            );
            view.updateContract(proposal.getAtomicswap().getContract());
            view.updateSecretHash(proposal.getAtomicswapSecretHash());
            view.updateSecret(proposal.getMessage().toString());

            ITransactionProposal publishedTxp = publishTxpUseCase.execute(proposal);
            ITransactionProposal signTxp = signTxpUseCase.execute(publishedTxp);
            broadcastTxpUseCase.execute(signTxp.getId());
            view.updateSendDashResult("Tx done!");
        } catch (Exception e) {
            view.showMessage(e.getMessage());
        }
    }

    @Override
    public void atomicswapParticipate(String address, String dash, String msg, String secretHash) {
        try {
            IRateResponse rate = getRateUseCases.execute();
            ITransactionProposal proposal=
                    addNewAtomicswapParticipateTxpUseCase.execute(
                            address,
                            Coin.parseCoin(dash).value,
                            false,
                            /*new CustomData(
                                    rate.getRate(),
                                    "send",
                                    null,
                                    null,
                                    null),
                             */
                            msg,
                            true,
                            secretHash
                    );

            view.updateContract(proposal.getAtomicswap().getContract());

            ITransactionProposal publishedTxp = publishTxpUseCase.execute(proposal);
            ITransactionProposal signTxp = signTxpUseCase.execute(publishedTxp);
            broadcastTxpUseCase.execute(signTxp.getId());
            view.updateSendDashResult("Tx done!");
        } catch (Exception e) {
            view.showMessage(e.getMessage());
        }
    }

    @Override
    public void atomicswapRedeem(String address, String msg, String contract, String secret, NetworkParameters parameters) {
        try {
            IRateResponse rate = getRateUseCases.execute();
            ITransactionProposal proposal=
                    addNewAtomicswapRedeemTxpUseCase.execute(
                            address,
                            null,
                            false,
                            /*new CustomData(
                                    rate.getRate(),
                                    "send",
                                    null,
                                    null,
                                    null),
                             */
                            msg,
                            true,
                            contract,
                            secret
                    );
            ITransactionProposal publishedTxp = publishTxpUseCase.execute(proposal);
            ITransactionProposal signTxp = signTxpUseCase.execute(publishedTxp);
            broadcastTxpUseCase.execute(signTxp.getId());
            view.updateSendDashResult("Tx done!");
        } catch (Exception e) {
            view.showMessage(e.getMessage());
        }
    }

    @Override
    public void atomicswapRefund(String address, String msg, String contract, NetworkParameters parameters) {
        try {
            IRateResponse rate = getRateUseCases.execute();
            ITransactionProposal proposal=
                    addNewAtomicswapRefundTxpUseCase.execute(
                            address,
                            null,
                            false,
                            /*new CustomData(
                                    rate.getRate(),
                                    "send",
                                    null,
                                    null,
                                    null),
                             */
                            msg,
                            true,
                            contract
                    );
            ITransactionProposal publishedTxp = publishTxpUseCase.execute(proposal);
            ITransactionProposal signTxp = signTxpUseCase.execute(publishedTxp);
            broadcastTxpUseCase.execute(signTxp.getId());
            view.updateSendDashResult("Tx done!");
        } catch (Exception e) {
            view.showMessage(e.getMessage());
        }
    }

    @Override
    public void getPendingAtomicswap() {
        try{
            ITransactionProposal[] txs = getAllPendingAtomicswapTxpsUseCase.execute();
            if(txs.length>0) {
                Object aaa = txs[0].getMessage();
            }
            System.out.println(txs);
        } catch (Exception e) {
            view.showMessage(e.getMessage());
        }
    }

    public static String getStrDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置为东八区
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        Date newDate = new Date();
        String dateStr = sdf.format(newDate);
        return dateStr;
    }

    public static String getDateTime(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //设置为东八区
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        Date newDate = new Date();
        return String.valueOf(newDate.getTime()/1000);
    }

    @Override
    public void testSingle() {
        // john
        try {
            Credentials credentials1 = createCredentials(null);
            IBitcoreWalletServerAPI bitcoreWalletServerAPI1 = createBwsApi("http://52.82.67.41:3232/bws/api/", credentials1);
            CreateWalletUseCase createWalletUseCase1 = new CreateWalletUseCase(
                    credentials1,
                    new CopayersCryptUtils(
                            new VircleCoinTypeRetriever()),
                    bitcoreWalletServerAPI1);
            JoinWalletInCreationUseCase joinWalletInCreationUseCase1 = new JoinWalletInCreationUseCase(
                    credentials1,
                    new CopayersCryptUtils(
                            new VircleCoinTypeRetriever()),
                    bitcoreWalletServerAPI1);
            CreateNewMainAddressesFromWalletUseCase createNewMainAddressesFromWalletUseCase1 = new CreateNewMainAddressesFromWalletUseCase(
                    bitcoreWalletServerAPI1);
            String walletId = createWalletUseCase1.execute();
            IJoinWalletResponse joinWalletResponse = joinWalletInCreationUseCase1.execute(walletId);
            createNewMainAddressesFromWalletUseCase1.create();
            System.out.println("bbb");
        }catch (Exception e) {
            System.out.println(e);
        }
    }

    private Credentials createCredentials(String words) {
        Credentials credentials;
        if(words != null) {
            credentials = new Credentials(split("rotate scrap radio awesome eight fee degree fee young tone board another"),
                    "",
                    new CopayersCryptUtils(
                            new VircleCoinTypeRetriever()));

        }else{
            credentials = new Credentials("", new CopayersCryptUtils(
                    new VircleCoinTypeRetriever()));
        }
        credentials.setNetworkParameters(MainNetParams.get());
        return credentials;
    }

    private IBitcoreWalletServerAPI createBwsApi(String bws_url, Credentials credentials) {
        return new Retrofit2BwsApiBridge(
                new Retrofit.Builder()
                        .baseUrl(bws_url)
                        .addConverterFactory(
                                GsonConverterFactory.create(
                                        new GsonBuilder()
                                                .setLenient()
                                                .create()))
                        .client(
                                new OkHttpClient
                                        .Builder()
                                        .addInterceptor(
                                                new BWCRequestSignatureInterceptor(
                                                        credentials,
                                                        new CopayersCryptUtils(new VircleCoinTypeRetriever()),
                                                        bws_url))
                                        .addInterceptor(
                                                new HttpLoggingInterceptor()
                                                        .setLevel(HttpLoggingInterceptor.Level.BODY))
                                        .build())
                        .build()
                        .create(IRetrofit2BwsAPI.class));

    }
}
