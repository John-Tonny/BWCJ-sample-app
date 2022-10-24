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
import org.openkuva.kuvabase.bwcj.data.entity.gson.masternode.MasternodeCollateralPro;
import org.openkuva.kuvabase.bwcj.data.entity.gson.masternode.MasternodePro;
import org.openkuva.kuvabase.bwcj.data.entity.interfaces.masternode.IMasternodePro;
import org.openkuva.kuvabase.bwcj.data.entity.gson.transaction.GsonInput;
import org.openkuva.kuvabase.bwcj.data.entity.interfaces.copayer.ICopayer;
import org.openkuva.kuvabase.bwcj.data.entity.interfaces.credentials.ICredentials;
import org.openkuva.kuvabase.bwcj.data.entity.interfaces.masternode.IMasternode;
import org.openkuva.kuvabase.bwcj.data.entity.interfaces.masternode.IMasternodeBroadcast;
import org.openkuva.kuvabase.bwcj.data.entity.interfaces.masternode.IMasternodeCollateral;
import org.openkuva.kuvabase.bwcj.data.entity.interfaces.masternode.IMasternodePing;
import org.openkuva.kuvabase.bwcj.data.entity.interfaces.masternode.IMasternodeRemove;
import org.openkuva.kuvabase.bwcj.data.entity.interfaces.masternode.IMasternodeStatus;
import org.openkuva.kuvabase.bwcj.data.entity.interfaces.asset.IAssetInfo;
import org.openkuva.kuvabase.bwcj.data.entity.interfaces.transaction.IAtomicswapInitiateData;
import org.openkuva.kuvabase.bwcj.data.entity.interfaces.transaction.IInput;
import org.openkuva.kuvabase.bwcj.data.entity.interfaces.transaction.IOutput;
import org.openkuva.kuvabase.bwcj.data.entity.interfaces.transaction.ITransactionHistory2;
import org.openkuva.kuvabase.bwcj.data.entity.interfaces.transaction.ITransactionProposal;
import org.openkuva.kuvabase.bwcj.data.entity.interfaces.wallet.IWallet;
import org.openkuva.kuvabase.bwcj.data.entity.pojo.transaction.AtomicswapInitiateData;
import org.openkuva.kuvabase.bwcj.data.entity.pojo.transaction.AtomicswapParticipateData;
import org.openkuva.kuvabase.bwcj.data.entity.pojo.transaction.AtomicswapRedeemData;
import org.openkuva.kuvabase.bwcj.data.entity.pojo.transaction.AtomicswapRefundData;
import org.openkuva.kuvabase.bwcj.data.entity.pojo.transaction.CustomData;
import org.openkuva.kuvabase.bwcj.data.entity.pojo.transaction.Output;
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
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.addNewErc1155Txp.IAddNewErc1155TxpUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.addNewTxp.IAddNewTxpUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.addNewAssetSendTxp.IAddNewAssetSendTxpUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.addNewAssetBurnTxp.IAddNewAssetBurnTxpUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.addNewAssetMintTxp.IAddNewAssetMintTxpUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.broadcastTxp.IBroadcastTxpUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.deleteAllPendingTxProposals.IDeleteAllPendingTxpsUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.getAllPendingAtomicswapTxProposals.IGetAllPendingAtomicswapTxpsUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.publishTxp.IPublishTxpUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.signTxp.ISignTxpUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.wallet.createWallet.CreateWalletUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.wallet.createWallet.ICreateWalletUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.wallet.getWallet.GetWalletUseCase;
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
import org.openkuva.kuvabase.bwcj.domain.utils.EthCoinTypeRetriever;
import org.openkuva.kuvabase.bwcj.domain.utils.VircleCoinTypeRetriever;
import org.openkuva.kuvabase.bwcj.domain.utils.atomicswap.CreateContract;
import org.openkuva.kuvabase.bwcj.domain.utils.atomicswap.ExtractContract;
import org.openkuva.kuvabase.bwcj.domain.utils.atomicswap.RedeemContract;
import org.openkuva.kuvabase.bwcj.domain.utils.atomicswap.RefundContract;
import org.openkuva.kuvabase.bwcj.domain.utils.masternode.ProRegTx;
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

import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.addNewProRegTxp.IAddNewProRegTxpUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.addNewProUpRegTxp.IAddNewProUpRegTxpUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.addNewProUpServiceTxp.IAddNewProUpServiceTxpUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.addNewProUpRevokeTxp.IAddNewProUpRevokeTxpUseCase;

import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.addNewApproveTxp.IAddNewApproveTxpUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.addNewFreezeBurnTxp.IAddNewFreezeBurnTxpUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.addNewRelayTxp.IAddNewRelayTxpUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.addNewRelayAssetTxp.IAddNewRelayAssetTxpUseCase;


import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.addNewErc721Txp.IAddNewErc721TxpUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.addNewErc721MintTxp.IAddNewErc721MintTxpUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.addNewErc721MintNFTTxp.IAddNewErc721MintNFTTxpUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.addNewErc1155Txp.IAddNewErc1155TxpUseCase;

import org.openkuva.kuvabase.bwcj.domain.useCases.asset.IGetAssetInfoUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.getTxHistory.IGetTxHistory2UseCase;

import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.getAllPendingTxProposals.IGetAllPendingTxpsUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.wallet.getUtxos.IGetUtxosUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.wallet.mergeBalance.IMergeBalanceUseCase;

import org.openkuva.kuvabase.bwcj.domain.utils.CommUtils;

import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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


import org.bitcoinj.core.Base58;
import org.bitcoinj.core.Utils;

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
    private final IAddNewProRegTxpUseCase addNewProRegTxpUseCase;
    private final IAddNewProUpRegTxpUseCase addNewProUpRegTxpUseCase;
    private final IAddNewProUpServiceTxpUseCase addNewProUpServiceTxpUseCase;
    private final IAddNewProUpRevokeTxpUseCase addNewProUpRevokeTxpUseCase;
    private final IAddNewAssetSendTxpUseCase addNewAssetSendTxpUseCase;
    private final IAddNewAssetBurnTxpUseCase addNewAssetBurnTxpUseCase;
    private final IAddNewAssetMintTxpUseCase addNewAssetMintTxpUseCase;
    private final IAddNewApproveTxpUseCase addNewApproveTxpUseCase;
    private final IAddNewFreezeBurnTxpUseCase addNewFreezeBurnTxpUseCase;
    private final IAddNewRelayTxpUseCase addNewRelayTxpUseCase;
    private final IAddNewRelayAssetTxpUseCase addNewRelayAssetTxpUseCase;
    private final IGetAssetInfoUseCase getAssetInfoUseCase;
    private final IGetTxHistory2UseCase getTxHistory2UseCase;


    private final IAddNewErc721TxpUseCase addNewErc721TxpUseCase;
    private final IAddNewErc721MintTxpUseCase addNewErc721MintTxpUseCase;
    private final IAddNewErc721MintNFTTxpUseCase addNewErc721MintNFTTxpUseCase;
    private final IAddNewErc1155TxpUseCase addNewErc1155TxpUseCase;
    private final IAddNewErc1155TxpUseCase addNewErc1155MintTxpUseCase;

    private final IGetAllPendingTxpsUseCase getAllPendingTxpsUseCase;
    private final IGetUtxosUseCase getUtxosUseCase;
    private final IMergeBalanceUseCase mergeBalanceUseCase;

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
            IGetWalletUseCase getWalletUseCase,
            IAddNewProRegTxpUseCase addNewProRegTxpUseCase,
            IAddNewProUpRegTxpUseCase addNewProUpRegTxpUseCase,
            IAddNewProUpServiceTxpUseCase addNewProUpServiceTxpUseCase,
            IAddNewProUpRevokeTxpUseCase addNewProUpRevokeTxpUseCase,
            IAddNewAssetSendTxpUseCase addNewAssetSendTxpUseCase,
            IAddNewAssetBurnTxpUseCase addNewAssetBurnTxpUseCase,
            IAddNewAssetMintTxpUseCase addNewAssetMintTxpUseCase,
            IAddNewApproveTxpUseCase addNewApproveTxpUseCase,
            IAddNewFreezeBurnTxpUseCase addNewFreezeBurnTxpUseCase,
            IAddNewRelayTxpUseCase addNewRelayTxpUseCase,
            IAddNewRelayAssetTxpUseCase addNewRelayAssetTxpUseCase,
            IGetAssetInfoUseCase getAssetInfoUseCase,
            IGetTxHistory2UseCase getTxHistory2UseCase,
            IAddNewErc721TxpUseCase addNewErc721TxpUseCase,
            IAddNewErc721MintTxpUseCase addNewErc721MintTxpUseCase,
            IAddNewErc721MintNFTTxpUseCase addNewErc721MintNFTTxpUseCase,
            IAddNewErc1155TxpUseCase addNewErc1155TxpUseCase,
            IAddNewErc1155TxpUseCase addNewErc1155MintTxpUseCase,
            IGetAllPendingTxpsUseCase getAllPendingTxpsUseCase,
            IGetUtxosUseCase getUtxosUseCase,
            IMergeBalanceUseCase mergeBalanceUseCase
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
        this.addNewProRegTxpUseCase = addNewProRegTxpUseCase;
        this.addNewProUpRegTxpUseCase = addNewProUpRegTxpUseCase;
        this.addNewProUpServiceTxpUseCase = addNewProUpServiceTxpUseCase;
        this.addNewProUpRevokeTxpUseCase = addNewProUpRevokeTxpUseCase;

        this.addNewAssetSendTxpUseCase = addNewAssetSendTxpUseCase;
        this.addNewAssetBurnTxpUseCase = addNewAssetBurnTxpUseCase;
        this.addNewAssetMintTxpUseCase = addNewAssetMintTxpUseCase;

        this.addNewApproveTxpUseCase = addNewApproveTxpUseCase;
        this.addNewFreezeBurnTxpUseCase = addNewFreezeBurnTxpUseCase;
        this.addNewRelayTxpUseCase = addNewRelayTxpUseCase;
        this.addNewRelayAssetTxpUseCase = addNewRelayAssetTxpUseCase;

        this.getAssetInfoUseCase = getAssetInfoUseCase;
        this.getTxHistory2UseCase = getTxHistory2UseCase;

        this.addNewErc721TxpUseCase = addNewErc721TxpUseCase;
        this.addNewErc721MintTxpUseCase = addNewErc721MintTxpUseCase;
        this.addNewErc721MintNFTTxpUseCase = addNewErc721MintNFTTxpUseCase;
        this.addNewErc1155TxpUseCase = addNewErc1155TxpUseCase;
        this.addNewErc1155MintTxpUseCase = addNewErc1155MintTxpUseCase;

        this.getAllPendingTxpsUseCase = getAllPendingTxpsUseCase;
        this.getUtxosUseCase = getUtxosUseCase;
        this.mergeBalanceUseCase = mergeBalanceUseCase;
    }

    @Override
    public void createWallet() {
        try {
            // view.showMnemonic(join(initializeCredentialsUseCase.execute(""), " "));
            view.showMessage(join(this.credentials.getMnemonic(), ""));
            String walletId;
            if(false) {
                walletId = createWallet.execute();
                joinWalletInCreationUseCase.execute(walletId);
                getWalletAddressesUseCases.create();
            }else{
                walletId = createWallet.execute(2, 3, "kWallet", false, "vcl");
                joinWalletInCreationUseCase.execute(walletId);
                IWallet response =
                        getWalletUseCase.execute();

                String secret = CommUtils.BuildJoinWalletSecret(response.getWalletCore().getId(), credentials.getWalletPrivateKey().getPrivateKeyAsHex(), "vcl", "livenet");
                System.out.println(secret);
            }
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
        //String tokenAddress = "0x8ffb6ceeb3c41f6286eedbc97df6d711ae00bff6";
        // long assetGuid = 3235835254L;
        // assetGuid = 3486931473L;
        long MIN_SATOSHIS = 50000000000L;
        long MAX_SATOSHIS = 1000000000000L;
        long MATURE_BLOCK = 100;
        boolean isErc721 = false;
        String tokenAddress;
        if(isErc721){
            tokenAddress = "0x26B69343Ab58f9A1A1BcB7FeE7FA6a5A1bB3409E"; //erc721
        }else{
            tokenAddress = "0x50d80b2a3eaf85b6413c4c1ad38134a6d1bb1ba6"; //erc1155
        }

        try {
            // IRateResponse rate = getRateUseCases.execute();

            ITransactionProposal proposal = null;
            boolean isOrigin = true;
            boolean isOutputs = false;
            boolean isInputs = false;
            boolean isMultiSign = false;
            if(isOrigin) {
                proposal =
                        postTransaction.execute(
                                // tokenAddress,
                                address,
                                dash,
                                msg,
                                false,
                                msg,
                                true);
            }else if(isOutputs){
                IOutput[] outputs = new IOutput[3];
                outputs[0]  = new Output(
                        "Sa9T57cng8Sg9fFXLgfASxpygXUemYStKF",
                        "111000000",
                        null);
                outputs[1]  = new Output(
                        "Sa9T57cng8Sg9fFXLgfASxpygXUemYStKF",
                        "222000000",
                        null);
                outputs[2]  = new Output(
                        "33jNHYbBY5wrC6enid4wHrUD9w1Tu2kt9G",
                        "333000000",
                        null);

                proposal = postTransaction.execute(outputs,  msg, false, "send", "", true);
            }else if(isMultiSign){
                ITransactionProposal[] proposals = getAllPendingTxpsUseCase.execute();
                if (proposals.length>=1){
                    proposal = proposals[0];
                }else{
                    view.showMessage("Not found!");
                    return;
                }
            }else if(isInputs){
                try {
                    IInput[] inputs = getUtxosUseCase.execute();
                    IOutput[] outputs = new IOutput[1];

                    ArrayList<IInput> aInputs = new ArrayList<IInput>();
                    long totalSatoshis = 0;
                    for(IInput input: inputs){
                        if(!input.isSpent() && !input.isLocked()){
                            if (input.getSatoshis() < MIN_SATOSHIS){
                                if(totalSatoshis >= MAX_SATOSHIS){
                                    break;
                                }
                                if (!input.isCoinbase() && input.getConfirmations() >= 1){
                                    totalSatoshis += input.getSatoshis();
                                    aInputs.add(input);
                                }else if(input.isCoinbase() && input.getConfirmations() >= MATURE_BLOCK){
                                    totalSatoshis += input.getSatoshis();
                                    aInputs.add(input);
                                }
                            }
                        }
                    }
                    totalSatoshis = MAX_SATOSHIS +1;
                    if(totalSatoshis < MAX_SATOSHIS){
                        view.showMessage("fund too small!");
                        return;
                    }

                    outputs[0]  = new Output(
                            "33jNHYbBY5wrC6enid4wHrUD9w1Tu2kt9G",
                            String.valueOf(totalSatoshis-100000000),
                            null);
                    IInput[] bInputs = aInputs.toArray(new IInput[0]);

                    proposal = postTransaction.execute(bInputs, outputs,  msg, false, "send", "");
                    System.out.println(inputs);
                } catch (Exception e) {
                    view.showMessage(e.getMessage());
                }
            }else{
                view.showMessage("Not Method!");
                return;
            }
            // 55a0a3ba156264b671fb7ac321c4d8b33a63341217e758e5912006a38bc720a3


            /*
            ITransactionProposal proposal =
                    addNewAssetSendTxpUseCase.execute(
                            assetGuid,
                            address,
                            dash,
                            msg,
                            false,
                            msg,
                            true);
            */

            /*
            //  eth to sys  step1
            ITransactionProposal proposal =
                    addNewApproveTxpUseCase.execute(
                            assetGuid,
                            dash);
            // 0x6d987ca3cd7a5184af3dc9d461eed54a56f2673e7ba09406198f2628fc53a0f9
            */

            /*
            // eth to sys step2
            String sysAddr = "sys1qvxd0jddvyxsqlahz8fedc0dl26f79knert6wzp";
            ITransactionProposal proposal =
                    addNewFreezeBurnTxpUseCase.execute(
                            assetGuid,
                            sysAddr,
                            dash);
            // 0x1f9de8d8301542d003c86ba39aaff64f6d7294ae7f4d69964c72c00dc9fa590f
            */

            /*
            // eth to sys step3
            String ethtxid = "0x1f9de8d8301542d003c86ba39aaff64f6d7294ae7f4d69964c72c00dc9fa590f";
            ITransactionProposal proposal =
                    addNewAssetMintTxpUseCase.execute(
                            assetGuid,
                            ethtxid);
            // a032902a6b19436d2f3e2bd217b547ab5484b554e5429eb131bfd4b4898e75ba
            */

            /*
            // sys to eth step1
            String ethAddr = "58CfA3CD076f8c79E411fDB87E473Dd8216713F0";
            ITransactionProposal proposal =
                    addNewAssetBurnTxpUseCase.execute(
                            assetGuid,
                            ethAddr,
                            dash);
            // f4932d2df7356ac721e6901d22837dd56fafc95745df75d3e283fde22ea36735
            */

            /*
            // sys to eth step2
            String txid = "f4932d2df7356ac721e6901d22837dd56fafc95745df75d3e283fde22ea36735";
            ITransactionProposal proposal =
                    addNewRelayTxpUseCase.execute(
                            assetGuid,
                            txid);
            // 0x6f8b88df111bd2da15e6483aefc06add010e23bff22a274130b9868f40f0526d
            */

            /*
            String txid = "a032902a6b19436d2f3e2bd217b547ab5484b554e5429eb131bfd4b4898e75ba";
            assetGuid = 3160230534L;
            ITransactionProposal proposal =
                    addNewRelayAssetTxpUseCase.execute(
                            assetGuid,
                            txid);
            */

            /*
            ITransactionProposal proposal =
                    addNewAssetSendTxpUseCase.execute(
                            assetGuid,
                            address,
                            dash
                            );
            // 1e226838b3c68175d0e2a9eccd1c09f374971ae24a763f12bc585acea0671732
            */

            // erc721 send
            /*
            String tokenId = "4";
            // address = "0x58CfA3CD076f8c79E411fDB87E473Dd8216713F0";  // square
            address = "0xbCDEc190eE9CfbAB5973E1aB17971CE1774F581F"; // rotate
            ITransactionProposal proposal =
                    addNewErc721TxpUseCase.execute(
                            tokenAddress,
                            address,
                            tokenId);
            //0x41f49473f04d6a1923a24a61f160409e388e0e3c7bb74a63fc918bbdb0953d50
            */

            // erc721 mint
            /*
            String tokenId = "30001";
            address = "0x58CfA3CD076f8c79E411fDB87E473Dd8216713F0";
            String tokenUri = "http://vpubchain.info/30001.json";
            ITransactionProposal proposal =
                    addNewErc721MintTxpUseCase.execute(
                            tokenAddress,
                            address,
                            tokenId,
                            tokenUri);
            // 0x7eb939c997f196bf1e18a2bc166ce78964e6d9a1b83991052ad80dab933e7026
            */

            // erc721 mintNFT
            /*
            address = "0x58CfA3CD076f8c79E411fDB87E473Dd8216713F0";
            String tokenUri = "http://vpubchain.info/20.json";
            ITransactionProposal proposal =
                    addNewErc721MintNFTTxpUseCase.execute(
                            tokenAddress,
                            address,
                            tokenUri);
            // 0xba4b77331992388ad8e3776647166c63f9ba6131e799a2233e093303c279fa0d
            */

            // erc1155 send
            /*
            String tokenId = "10000";
            address = "0x58CfA3CD076f8c79E411fDB87E473Dd8216713F0";
            ITransactionProposal proposal =
                    addNewErc1155TxpUseCase.execute(
                            tokenAddress,
                            address,
                            tokenId,
                            "10",
                            "my测试1",
                            null);
            // 0x584f8126db840ff0ebc37ebe5f59bf81df259bc29991505b7163a3c3c1360e8d
            // 0xf30a4b5af6be5fa66e15bf4453513545cf1f7baad236271f533b0d337457781a
            */

            // erc1155 mint
            /*
            String tokenId = "17000";
            address = "0x58CfA3CD076f8c79E411fDB87E473Dd8216713F0";
            ITransactionProposal proposal =
                    addNewErc1155MintTxpUseCase.execute(
                            tokenAddress,
                            address,
                            tokenId,
                            "1700");
            // 0xd7d68804f93ce32b5cb41b71fe117f2297820b32e71206cea7994676747279b7
            */

            ITransactionProposal publishedTxp = publishTxpUseCase.execute(proposal);
            ITransactionProposal signTxp = signTxpUseCase.execute(publishedTxp);
            ITransactionProposal broadcastTxp =  broadcastTxpUseCase.execute(signTxp.getId());
            view.updateSendDashResult("Tx done!");
            System.out.println(broadcastTxp.getTxid());
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
        /*
        String walletId = "f8914e6d-0dbe-4566-a6c3-6932f84e9335";
        String walletId1 = walletId.replace("-", "");
        String wid = Base58.encode(Utils.HEX.decode(walletId1));

        String walletPrivateKey = "3741e27b2c7e8264d8421d53ab301f727f0f87cd5f5f5711db4c769157a74e82";
        String walletPrivateKey1 = walletPrivateKey + "01";
        String wpriv1 = Base58.encodeChecked(128, Utils.HEX.decode(walletPrivateKey1));
        */

        String secret = "XhGFGvt2PPVugdr7zcZ8VJKy589JAdsRd6WEuaRnNmLMPfW9rGJPT5hepNDa3bcGw6dEgbshEFLvcl";
        String walletId = Utils.HEX.encode(Base58.decode(secret.substring(0,22)));
        String walletPrivateKey = Utils.HEX.encode(Base58.decodeChecked(secret.substring(22, 74)));
        String network = secret.substring(74,75);
        String coin = secret.substring(75, secret.length());

        if(network.compareTo("L")==0){
            network = "livenet";
        }else{
            network="testnet";
        }

        String walletId1 = walletId.substring(0,8) + "-" + walletId.substring(8, 12)+ "-" + walletId.substring(12, 16) + "-" + walletId.substring(16, 20) +"-" + walletId.substring(20, walletId.length());
        String walletPrivateKey1 = walletPrivateKey.substring(2, walletPrivateKey.length()-2);

        if(false) {
            try {
                ITransactionHistory[] txs = getTxHistoryUseCase.execute(skip, limit, tokenAddress, includeExtendedInfo);
                System.out.println(txs);
            } catch (Exception e) {
                view.showMessage(e.getMessage());
            }
        }else{
            if(false) {
                try {
                    ITransactionHistory2 txs = getTxHistory2UseCase.execute(skip, limit);
                    System.out.println(txs);
                } catch (Exception e) {
                    view.showMessage(e.getMessage());
                }
            }else{
                try {
                    String txid = mergeBalanceUseCase.execute("500", "10000");
                    System.out.println(txid);
                }catch(Exception e){
                    view.showMessage(e.getMessage());
                }
            }
        }
    }

    @Override
    public void getMasternodeStatus(String coin, String txid, String address, String payee) {
        if(false) {
            try {
                IMasternodeStatus txs = getMasternodeStatusUseCase.execute(coin, txid, address, payee);
                System.out.println(txs);
            } catch (Exception e) {
                view.showMessage(e.getMessage());
            }
        }else {
            try {
                IAssetInfo assetInfo = getAssetInfoUseCase.execute(txid);
                System.out.println(assetInfo);
            } catch (Exception e) {
                view.showMessage(e.getMessage());
            }
        }

    }

    @Override
    public void getMasternodeCollateral() {
        try {
           IMasternodeCollateral[] txs = getMasternodeCollateralUseCase.execute();
            System.out.println(txs);

            if(txs.length>0) {
                view.updateContract(txs[0].getTxid());
                view.updateSecret(String.valueOf(txs[0].getVout()));
            }
            /*
            String collateradId = "f6028461dcc19ac1b1dae4fc6e242c3b06e9f9f1edcd746236b073c84c32a2be";
            int collateralIndex = 1;
            String collateralAddress = "sys1qpr64vdg9yte0kw0dh70vjkts345qwwty5879ej";
            String path = "m/0/138";
            String collateralPrivKey = "9cd56e5dabd7098e4034c020625749a00b6906f801a3464184bac2df7914c90f";
            */
            String host = "8.136.121.88";
            // String host = "106.55.177.193";
            int port = 9090;
            String masternodePrivKey = "62051340e011dff7e6a1f152dd1de037992cd0612057dc04fc01bd9933f9f3e0";
            String masternodePubKey = "03107d379a496b89c5a38d3dbefaf92dd60bfcd41a99d5cee2bcffade091efc07ed7e142814d6e060b4986ca1a8040b5";

            String ownerAddr = "sys1q7966wg9je2tj6x4kfhy8hlh28hgthjwrdqzpr5";
            String voteAddr = "sys1q7966wg9je2tj6x4kfhy8hlh28hgthjwrdqzpr5";
            String payAddr = "sys1qg50gqqwu7g0g2xzct2wmhdks0qrdw367tqfmhe";
            GsonInput input = new GsonInput();
            String txid = "8e0fc358a92b373639321e5d6110abf9b37e73163da8a4fbe5b3e23f7b1386fd";
            input.setInput(txid, 0);
            GsonInput[] inputs = new GsonInput[1];
            inputs[0] = input;
            // MasternodeCollateralPro masternodeCollateralPro = new MasternodeCollateralPro(collateradId, collateralIndex, collateralPrivKey, host, port, masternodePrivKey, masternodePubKey, ownerAddr, voteAddr, payAddr, 0);
            // MasternodeCollateralPro masternodeCollateralPro = new MasternodeCollateralPro(collateradId, collateralIndex, collateralAddress, path, host, port, masternodePrivKey, masternodePubKey, this.credentials);
            // ProRegTx proRegTx = new ProRegTx(inputs, collateradId, collateralIndex, collateralPrivKey, host, port, masternodePubKey, ownerAddr, voteAddr, payAddr, 0, this.credentials.getNetworkParameters());
            // String msg = proRegTx.getOutScripts(true);

            MasternodeCollateralPro masternodeCollateralPro = new MasternodeCollateralPro(
                    txs[0].getTxid(),
                    txs[0].getVout(), txs[0].getAddress(), txs[0].getPath(), host, port, masternodePrivKey, masternodePubKey, this.credentials);

            /*
            MasternodeCollateralPro masternodeCollateralPro = new MasternodeCollateralPro(
                    txs[0].getTxid(),
                    txs[0].getVout(), txs[0].getAddress(), txs[0].getPath(), host, port, masternodePrivKey, masternodePubKey, this.credentials, ownerAddr,voteAddr, payAddr, 0);
            */
            ITransactionProposal proposal = addNewProRegTxpUseCase.execute("", true, masternodeCollateralPro);
            ITransactionProposal publishedTxp = publishTxpUseCase.execute(proposal);
            ITransactionProposal signTxp = signTxpUseCase.execute(publishedTxp);
            ITransactionProposal broadcastTxp = broadcastTxpUseCase.execute(signTxp.getId());
            System.out.println(broadcastTxp.getTxid());
            System.out.println("finish proRegTx");
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
            // System.out.println(txs);
            if(txs.length>0) {
                // proUpReg
                // String masternodePrivKey = "34372216419c23e933e7f847217a49698399f21db2cff4fc57f1cecc283291ef";
                // String masternodePubKey = "873a70bff1ef82cf676f62dd5e174704e3fdfd475b6385a8e080a29020f950839848b3286da40bc30473108c6bbce440";
                // ITransactionProposal proposal = addNewProUpRegTxpUseCase.execute("", true, txid, masternodePubKey, masternodePrivKey);

                // proUpService
                 String address1 = "47.99.156.96:9090";
                ITransactionProposal proposal = addNewProUpServiceTxpUseCase.execute("", true, txid, address1);

                // proRevoke
                // ITransactionProposal proposal = addNewProUpRevokeTxpUseCase.execute("", true, txid);

                ITransactionProposal publishedTxp = publishTxpUseCase.execute(proposal);
                ITransactionProposal signTxp = signTxpUseCase.execute(publishedTxp);
                ITransactionProposal broadcastTxp = broadcastTxpUseCase.execute(signTxp.getId());
                System.out.println(broadcastTxp.getTxid());
                System.out.println("finish proUpRegTx");


            }


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
        // String bb = ECKey.fromPublicOnly(Utils.HEX.decode(pubKey)).toAddress(this.credentials.getNetworkParameters()).toBase58();

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
                            dash,
                            // Coin.parseCoin(dash).value,
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
                            dash,
                            // Coin.parseCoin(dash).value,
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
        String coin = "vcl";
        String url = "http://69.234.192.199:3232/bws/api/";
        // String url = "http://192.168.246.133:3232/bws/api/";
        // john
        try {
            if(false) {
                Credentials credentials1 = createCredentials(null, coin);
                IBitcoreWalletServerAPI bitcoreWalletServerAPI1 = createBwsApi(url, credentials1);
                CreateWalletUseCase createWalletUseCase1 = new CreateWalletUseCase(
                        credentials1,
                        bitcoreWalletServerAPI1);
                JoinWalletInCreationUseCase joinWalletInCreationUseCase1 = new JoinWalletInCreationUseCase(
                        credentials1,
                        bitcoreWalletServerAPI1);
                CreateNewMainAddressesFromWalletUseCase createNewMainAddressesFromWalletUseCase1 = new CreateNewMainAddressesFromWalletUseCase(
                        bitcoreWalletServerAPI1);
                String walletId = createWalletUseCase1.execute(coin);
                IJoinWalletResponse joinWalletResponse = joinWalletInCreationUseCase1.execute(walletId, coin);
                IAddressesResponse addressesResponse = createNewMainAddressesFromWalletUseCase1.create();
                view.updateWalletId(walletId);
                view.updateWalletAddress(addressesResponse.getAddress());
                System.out.println("bbb");
                System.out.println(credentials1.getMnemonic());
                view.updateMnemonic(credentials1.getMnemonic().toString());
            }else {

                Credentials credentials1 = createCredentials(null, coin);
                IBitcoreWalletServerAPI bitcoreWalletServerAPI1 = createBwsApi(url, credentials1);
                CreateWalletUseCase createWalletUseCase1 = new CreateWalletUseCase(
                        credentials1,
                        bitcoreWalletServerAPI1);

                JoinWalletInCreationUseCase joinWalletInCreationUseCase1 = new JoinWalletInCreationUseCase(
                        credentials1,
                        bitcoreWalletServerAPI1);
                GetWalletUseCase getWalletUseCase = new GetWalletUseCase(
                        credentials1,
                        bitcoreWalletServerAPI1);

                String walletId = createWalletUseCase1.execute(2, 2, "kWallet", false, coin);
                IJoinWalletResponse joinWalletResponse = joinWalletInCreationUseCase1.execute(walletId, coin);

                IWallet response = getWalletUseCase.execute();
                String secret = CommUtils.BuildJoinWalletSecret(response.getWalletCore().getId(), credentials1.getWalletPrivateKey().getPrivateKeyAsHex(), "vcl", "livenet");

                /*
                String secret = "F4vvVDC1HbPRcAoMtFLptmL2pMjeiBYiyR3GqiiNqX9wfNFJ3Rpi53o7Nz35CcTWDiuLNY3RMELvcl";
                ECKey key = ECKey
                        .fromPrivate(Utils.HEX.decode(CommUtils.ParseWalletSecret(secret).getWalletPrivateKey()));


                // Credentials credentials2 = createCredentials("scare doctor mansion distance divert benefit anger better rich mean release audit", coin);
                 */
                Credentials credentials2 = createCredentials(null, coin);

                /*
                String aa = credentials2.getCopayersCryptUtils().signMessage("abcd", CommUtils.ParseWalletSecret(secret).getWalletPrivateKey());
                boolean bb = credentials2.getCopayersCryptUtils().verifyMessage("abcd", aa, Utils.HEX.encode(key.getPubKey()));
                 */
                System.out.println("privKey: " + credentials1.getWalletPrivateKey().getPrivateKeyAsHex());
                System.out.println("pubKey: " + credentials1.getWalletPrivateKey().getPublicKeyAsHex());
                System.out.println("seed1: " + credentials1.getMnemonic().toString());
                System.out.println("seed2: " + credentials2.getMnemonic().toString());
                System.out.println(secret);

                IBitcoreWalletServerAPI bitcoreWalletServerAPI2 = createBwsApi(url, credentials2);
                JoinWalletInCreationUseCase joinWalletInCreationUseCase2 = new JoinWalletInCreationUseCase(
                        credentials2,
                        bitcoreWalletServerAPI2);
                /*
                Credentials credentials2 = createCredentials(null, coin);
                IBitcoreWalletServerAPI bitcoreWalletServerAPI2 = createBwsApi("http://69.234.192.199:3232/bws/api/", credentials2);
                JoinWalletInCreationUseCase joinWalletInCreationUseCase2 = new JoinWalletInCreationUseCase(
                        credentials2,
                        bitcoreWalletServerAPI2);
                */

                IJoinWalletResponse joinWalletResponse1 = joinWalletInCreationUseCase2.execute(secret, "kCopayerName", true);
                System.out.println("aaa");
            }
        }catch (Exception e) {
            System.out.println(e);
        }
    }

    private Credentials createCredentials(String words, String coin) {
        Credentials credentials;

        if(words != null) {
            /*
            credentials = new Credentials(split("rotate scrap radio awesome eight fee degree fee young tone board another"),
                    "",
                    coin);

             */
            credentials = new Credentials(split(words), "", coin);

        }else{
            credentials = new Credentials(coin);
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
                                                        bws_url))
                                        .addInterceptor(
                                                new HttpLoggingInterceptor()
                                                        .setLevel(HttpLoggingInterceptor.Level.BODY))
                                        .build())
                        .build()
                        .create(IRetrofit2BwsAPI.class));

    }
}
