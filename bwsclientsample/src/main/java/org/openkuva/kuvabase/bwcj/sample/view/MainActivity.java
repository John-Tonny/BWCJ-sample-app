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

package org.openkuva.kuvabase.bwcj.sample.view;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.GsonBuilder;

import org.bitcoinj.core.Base58;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.UnsafeByteArrayOutputStream;
import org.bitcoinj.core.Utils;

import java.math.BigDecimal;
import java.util.Date;

import org.bitcoinj.crypto.DeterministicKey;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.addNewProRegTxp.AddNewProRegTxpUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.addNewProUpRegTxp.AddNewProUpRegTxpUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.addNewProUpServiceTxp.AddNewProUpServiceTxpUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.addNewProUpRevokeTxp.AddNewProUpRevokeTxpUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.wallet.getWallet.GetWalletUseCase;
import org.openkuva.kuvabase.bwcj.domain.utils.Credentials;

import static org.openkuva.kuvabase.bwcj.domain.utils.DeriveUtils.deriveChildByPath;
import static org.openkuva.kuvabase.bwcj.domain.utils.ListUtils.split;

import org.bitcoinj.params.MainNetParams;
import org.openkuva.kuvabase.bwcj.data.entity.interfaces.credentials.ICredentials;
import org.openkuva.kuvabase.bwcj.domain.useCases.credentials.InitializeCredentialsWithRandomValueUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.exchange.getRate.GetRateUseCases;
import org.openkuva.kuvabase.bwcj.domain.useCases.getTxHistory.GetTxHistoryUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.addNewAtomicswapInitiateTxp.AddNewAtomicswapInitiateTxpUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.addNewAtomicswapParticipateTxp.AddNewAtomicswapParticipateTxpUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.addNewAtomicswapRedeemTxp.AddNewAtomicswapRedeemTxpUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.addNewAtomicswapRefundTxp.AddNewAtomicswapRefundTxpUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.addNewTxp.AddNewTxpUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.broadcastTxp.BroadcastTxpUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.deleteAllPendingTxProposals.DeleteAllPendingTxpsUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.getAllPendingAtomicswapTxProposals.GetAllPendingAtomicswapTxpsUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.getAllPendingTxProposals.GetAllPendingTxpsUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.publishTxp.PublishTxpUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.signTxp.SignTxpUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.wallet.createWallet.CreateWalletUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.wallet.getWalletAddress.GetWalletAddressesUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.wallet.getWalletBalance.GetWalletBalanceUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.wallet.joinWalletInCreation.JoinWalletInCreationUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.wallet.postWalletAddress.CreateNewMainAddressesFromWalletUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.wallet.recoveryWalletFromMnemonic.RecoveryWalletFromMnemonicUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.masternode.getMasterondes.GetMasternodesUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.masternode.removeMasternode.RemoveMasternodeUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.masternode.status.GetMasternodeStatusUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.masternode.collateral.GetMasternodeCollateralUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.masternode.signMasternode.SignMasternodeUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.masternode.broadcastMasternode.BroadcastMasternodeUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.masternode.ping.GetMasternodePingUseCase;

import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.addNewAssetSendTxp.AddNewAssetSendTxpUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.addNewAssetBurnTxp.AddNewAssetBurnTxpUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.addNewAssetMintTxp.AddNewAssetMintTxpUseCase;

import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.addNewApproveTxp.AddNewApproveTxpUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.addNewFreezeBurnTxp.AddNewFreezeBurnTxpUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.addNewRelayTxp.AddNewRelayTxpUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.addNewRelayAssetTxp.AddNewRelayAssetTxpUseCase;


import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.addNewErc721Txp.AddNewErc721TxpUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.addNewErc721MintTxp.AddNewErc721MintTxpUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.addNewErc721MintNFTTxp.AddNewErc721MintNFTTxpUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.addNewErc1155Txp.AddNewErc1155TxpUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.transactionProposal.addNewErc1155MintTxp.AddNewErc1155MintTxpUseCase;

import org.openkuva.kuvabase.bwcj.domain.useCases.asset.GetAssetInfoUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.getTxHistory.GetTxHistory2UseCase;

import org.openkuva.kuvabase.bwcj.domain.useCases.wallet.getUtxos.GetUtxosUseCase;
import org.openkuva.kuvabase.bwcj.domain.useCases.wallet.mergeBalance.MergeBalanceUseCase;

import org.openkuva.kuvabase.bwcj.domain.utils.CommonNetworkParametersBuilder;
import org.openkuva.kuvabase.bwcj.domain.utils.CopayersCryptUtils;
import org.openkuva.kuvabase.bwcj.domain.utils.VircleCoinTypeRetriever;
import org.openkuva.kuvabase.bwcj.domain.utils.EthCoinTypeRetriever;
import org.openkuva.kuvabase.bwcj.domain.utils.transactions.TransactionBuilder;
import org.openkuva.kuvabase.bwcj.sample.ApiUrls;
import org.openkuva.kuvabase.bwcj.sample.R;
import org.openkuva.kuvabase.bwcj.sample.model.rate.RateApiProvider;
import org.openkuva.kuvabase.bwcj.sample.model.wallet.WalletRepositoryProvider;
import org.openkuva.kuvabase.bwcj.sample.presenter.AsyncMainActivityPresenter;
import org.openkuva.kuvabase.bwcj.sample.presenter.IMainActivityPresenter;
import org.openkuva.kuvabase.bwcj.sample.presenter.MainActivityPresenter;
import org.openkuva.kuvabase.bwcj.service.bitcoreWalletService.interfaces.IBitcoreWalletServerAPI;
import org.openkuva.kuvabase.bwcj.service.bitcoreWalletService.interfaces.exception.InvalidParamsException;
import org.openkuva.kuvabase.bwcj.service.bitcoreWalletService.interfaces.wallets.IJoinWalletResponse;
import org.openkuva.kuvabase.bwcj.service.bitcoreWalletService.retrofit2.IRetrofit2BwsAPI;
import org.openkuva.kuvabase.bwcj.service.bitcoreWalletService.retrofit2.Retrofit2BwsApiBridge;
import org.openkuva.kuvabase.bwcj.service.bitcoreWalletService.retrofit2.interceptors.BWCRequestSignatureInterceptor;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import org.openkuva.kuvabase.bwcj.data.entity.gson.wallet.AddressInfo;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private long CLIENT_VERSION = 1000000;
    private long CLIENT_SENTINEL_VERSION = 1000000;
    private long CLIENT_MASTERNODE_VERSION = 1010191;

    private Button createWalletBtn;
    private Button getAddressBtn;
    private Button getBalanceBtn;
    private Button sendDash;

    private TextView walletIdTextTv;
    private TextView walletAddressTv;
    private TextView walletBalanceTv;
    private TextView sendDashResultTv;

    private EditText walletAddressToSendEt;
    private EditText dashToSendEt;
    private EditText messageToSendEt;

    private EditText txidEt;
    private EditText addressEt;
    private EditText payeeEt;

    private IMainActivityPresenter mainActivityPresenter;
    private IBitcoreWalletServerAPI bitcoreWalletServerAPI;
    private IBitcoreWalletServerAPI[] bitcoreWalletServerAPIs;
    private Credentials credentials;
    private Credentials[] credentialss;
    private CopayersCryptUtils copayersCryptUtilsVcl;
    private CopayersCryptUtils copayersCryptUtilsEth;

    private Button swapInitiate;
    private Button swapParticipate;
    private Button swapRedeem;
    private Button swapRefund;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean isElectrum = true;
        copayersCryptUtilsVcl = new CopayersCryptUtils(new VircleCoinTypeRetriever());
        copayersCryptUtilsVcl.setElectrum(isElectrum);

        copayersCryptUtilsEth = new CopayersCryptUtils(new EthCoinTypeRetriever());

        setupCredentials();
        setupBwsApi();
        initializeView();
        setupPresenter();
    }

    private void setupCredentials() {
        /*credentials = new Credentials("",   copayersCryptUtils);*/

        String words = "bone casual observe virus prepare system aunt bamboo horror police vault floor";
        // words = "rotate scrap radio awesome eight fee degree fee young tone board another";
        // words = "square spray mother unusual foam casino fall include pulp arm soul scorpion";
        words = "omit embark obscure food fault notable smoke crowd bicycle surge bone opera";

        if(true) {
            credentials = new Credentials(split(words), "", copayersCryptUtilsVcl);
            credentialss = new Credentials[2];
            credentialss[0] = credentials;
            credentialss[1] = new Credentials(split(words1), "", copayersCryptUtilsVcl);
            credentialss[1].setNetworkParameters(MainNetParams.get());
        }else{
            credentials = new Credentials(split(words), "", copayersCryptUtilsEth);
        }
        credentials.setNetworkParameters(MainNetParams.get());

        AddressInfo a1 = credentials.getPrivateByPath("m/0/0");
        AddressInfo a2 = credentials.getPrivateByPath("m/0/1");
        AddressInfo a3 = credentials.getPrivateByPath("m/0/2");
        AddressInfo a4 = credentials.getPrivateByPath("m/0/3");
        System.out.println(a1);
        System.out.println(a2);
        System.out.println(a3);
        System.out.println(a4);

    }

    private void setupPresenter() {
        mainActivityPresenter =
                new AsyncMainActivityPresenter(
                        new MainActivityPresenter(
                                new InMainThreadMainActivityView(
                                        new MainActivityView(
                                                walletIdTextTv,
                                                walletAddressTv,
                                                walletBalanceTv,
                                                sendDashResultTv,
                                                findViewById(R.id.tv_words),
                                                this,
                                                txidEt,
                                                addressEt,
                                                payeeEt),
                                        new Handler(Looper.getMainLooper())),
                                credentials,
                                new CreateWalletUseCase(
                                        credentials,
                                        bitcoreWalletServerAPI),
                                new JoinWalletInCreationUseCase(
                                        credentials,
                                        bitcoreWalletServerAPI),
                                new GetWalletAddressesUseCase(
                                        bitcoreWalletServerAPI),
                                new GetWalletBalanceUseCase(
                                        credentials,
                                        bitcoreWalletServerAPI,
                                        WalletRepositoryProvider.get()),
                                new GetRateUseCases(
                                        RateApiProvider.get()),
                                new AddNewTxpUseCase(
                                        credentials,
                                        bitcoreWalletServerAPI),
                                new PublishTxpUseCase(
                                        bitcoreWalletServerAPI,
                                        credentials),
                                new SignTxpUseCase(
                                        bitcoreWalletServerAPI,
                                        credentials),
                                new BroadcastTxpUseCase(
                                        credentials,
                                        bitcoreWalletServerAPI),
                                new DeleteAllPendingTxpsUseCase(
                                        credentials,
                                        bitcoreWalletServerAPI),
                                new RecoveryWalletFromMnemonicUseCase(
                                        credentials,
                                        bitcoreWalletServerAPI),
                                bitcoreWalletServerAPI,
                                new InitializeCredentialsWithRandomValueUseCase(
                                        credentials),
                                new CreateNewMainAddressesFromWalletUseCase(
                                        bitcoreWalletServerAPI),
                                new GetTxHistoryUseCase(
                                        credentials,
                                        bitcoreWalletServerAPI),
                                new GetMasternodeStatusUseCase(bitcoreWalletServerAPI),
                                new GetMasternodeCollateralUseCase(bitcoreWalletServerAPI),
                                new SignMasternodeUseCase(bitcoreWalletServerAPI),
                                new BroadcastMasternodeUseCase(bitcoreWalletServerAPI),
                                new GetMasternodesUseCase(bitcoreWalletServerAPI),
                                new RemoveMasternodeUseCase(bitcoreWalletServerAPI),
                                new GetMasternodePingUseCase(bitcoreWalletServerAPI),
                                new AddNewAtomicswapInitiateTxpUseCase(
                                        credentials,
                                        bitcoreWalletServerAPI),
                                new AddNewAtomicswapParticipateTxpUseCase(
                                        credentials,
                                        bitcoreWalletServerAPI),
                                new AddNewAtomicswapRedeemTxpUseCase(
                                        credentials,
                                        bitcoreWalletServerAPI),
                                new AddNewAtomicswapRefundTxpUseCase(
                                        credentials,
                                        bitcoreWalletServerAPI),
                                new GetAllPendingAtomicswapTxpsUseCase(
                                        credentials,
                                        bitcoreWalletServerAPI),
                                new GetWalletUseCase(
                                        credentials,
                                        bitcoreWalletServerAPI),
                                new AddNewProRegTxpUseCase(
                                        credentials,
                                        this.copayersCryptUtilsVcl,
                                        bitcoreWalletServerAPI),
                                new AddNewProUpRegTxpUseCase(
                                        credentials,
                                        this.copayersCryptUtilsVcl,
                                        bitcoreWalletServerAPI),
                                new AddNewProUpServiceTxpUseCase(
                                        credentials,
                                        this.copayersCryptUtilsVcl,
                                        bitcoreWalletServerAPI),
                                new AddNewProUpRevokeTxpUseCase(
                                        credentials,
                                        this.copayersCryptUtilsVcl,
                                        bitcoreWalletServerAPI),
                                new AddNewAssetSendTxpUseCase(
                                        credentials,
                                        this.copayersCryptUtilsVcl,
                                        bitcoreWalletServerAPI),
                                new AddNewAssetBurnTxpUseCase(
                                        credentials,
                                        this.copayersCryptUtilsVcl,
                                        bitcoreWalletServerAPI),
                                new AddNewAssetMintTxpUseCase(
                                        credentials,
                                        this.copayersCryptUtilsVcl,
                                        bitcoreWalletServerAPI),
                                new AddNewApproveTxpUseCase(
                                        credentials,
                                        this.copayersCryptUtilsEth,
                                        bitcoreWalletServerAPI),
                                new AddNewFreezeBurnTxpUseCase(
                                        credentials,
                                        this.copayersCryptUtilsEth,
                                        bitcoreWalletServerAPI),
                                new AddNewRelayTxpUseCase(
                                        credentials,
                                        this.copayersCryptUtilsEth,
                                        bitcoreWalletServerAPI),
                                new AddNewRelayAssetTxpUseCase(
                                        credentials,
                                        this.copayersCryptUtilsEth,
                                        bitcoreWalletServerAPI),
                                new GetAssetInfoUseCase(
                                        credentials,
                                        this.copayersCryptUtilsEth,
                                        bitcoreWalletServerAPI),
                                new GetTxHistory2UseCase(
                                        credentials,
                                        bitcoreWalletServerAPI),
                                new AddNewErc721TxpUseCase(
                                        credentials,
                                        this.copayersCryptUtilsEth,
                                        bitcoreWalletServerAPI),
                                new AddNewErc721MintTxpUseCase(
                                        credentials,
                                        this.copayersCryptUtilsEth,
                                        bitcoreWalletServerAPI),
                                new AddNewErc721MintNFTTxpUseCase(
                                        credentials,
                                        this.copayersCryptUtilsEth,
                                        bitcoreWalletServerAPI),
                                new AddNewErc1155TxpUseCase(
                                        credentials,
                                        this.copayersCryptUtilsEth,
                                        bitcoreWalletServerAPI),
                                new AddNewErc1155MintTxpUseCase(
                                        credentials,
                                        this.copayersCryptUtilsEth,
                                        bitcoreWalletServerAPI),
                                new GetAllPendingTxpsUseCase(
                                        credentials,
                                        bitcoreWalletServerAPI),
                                new GetUtxosUseCase(credentials, bitcoreWalletServerAPI),
                                new MergeBalanceUseCase(credentialss, bitcoreWalletServerAPIs)
                                ),
                        Executors.newCachedThreadPool());
    }


    private void setupBwsApi() {
        bitcoreWalletServerAPI =
                new Retrofit2BwsApiBridge(
                        new Retrofit.Builder()
                                .baseUrl(ApiUrls.URL_BWS)
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
                                                                this.credentials,
                                                                ApiUrls.URL_BWS))
                                                .addInterceptor(
                                                        new HttpLoggingInterceptor()
                                                                .setLevel(HttpLoggingInterceptor.Level.BODY))
                                                .readTimeout(60, TimeUnit.SECONDS)
                                                .writeTimeout(60, TimeUnit.SECONDS)
                                                .connectTimeout(60, TimeUnit.SECONDS)
                                                .build())
                                .build()
                                .create(IRetrofit2BwsAPI.class));

        bitcoreWalletServerAPIs = new IBitcoreWalletServerAPI[2];
        bitcoreWalletServerAPIs[0]= bitcoreWalletServerAPI;
        bitcoreWalletServerAPIs[1] =
                new Retrofit2BwsApiBridge(
                        new Retrofit.Builder()
                                .baseUrl(ApiUrls.URL_BWS)
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
                                                                this.credentialss[1],
                                                                ApiUrls.URL_BWS))
                                                .addInterceptor(
                                                        new HttpLoggingInterceptor()
                                                                .setLevel(HttpLoggingInterceptor.Level.BODY))
                                                .readTimeout(60, TimeUnit.SECONDS)
                                                .writeTimeout(60, TimeUnit.SECONDS)
                                                .connectTimeout(60, TimeUnit.SECONDS)
                                                .build())
                                .build()
                                .create(IRetrofit2BwsAPI.class));


    }

    private void initializeView() {
        createWalletBtn = findViewById(R.id.ma_create_wallet_btn);
        createWalletBtn.setOnClickListener(this);
        walletIdTextTv = findViewById(R.id.ma_wallet_id_tv);

        getAddressBtn = findViewById(R.id.ma_wallet_address_btn);
        getAddressBtn.setOnClickListener(this);
        walletAddressTv = findViewById(R.id.ma_wallet_address_tv);

        getBalanceBtn = findViewById(R.id.ma_wallet_balance_btn);
        getBalanceBtn.setOnClickListener(this);
        walletBalanceTv = findViewById(R.id.ma_wallet_balance_tv);

        sendDash = findViewById(R.id.ma_send_dash_btn);
        sendDash.setOnClickListener(this);
        walletAddressToSendEt = findViewById(R.id.ma_wallet_address_to_send_et);
        dashToSendEt = findViewById(R.id.ma_dash_to_send_et);
        sendDashResultTv = findViewById(R.id.ma_send_dash_callback_tv);
        messageToSendEt = findViewById(R.id.ma_msg_to_send_et);

        walletAddressToSendEt.setText("Sa9T57cng8Sg9fFXLgfASxpygXUemYStKF");
        dashToSendEt.setText("108.6543");

        txidEt = findViewById(R.id.ma_wallet_txid_et);
        addressEt = findViewById(R.id.ma_wallet_address_et);
        payeeEt = findViewById(R.id.ma_wallet_payee_et);

       swapInitiate  = findViewById(R.id.ma_swap_initiate_btn);
       swapInitiate.setOnClickListener(this);
       swapParticipate  = findViewById(R.id.ma_swap_participate_btn);
       swapParticipate.setOnClickListener(this);
       swapRedeem  = findViewById(R.id.ma_swap_redeem_btn);
       swapRedeem.setOnClickListener(this);
       swapRefund  = findViewById(R.id.ma_swap_refund_btn);
       swapRefund.setOnClickListener(this);



    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ma_create_wallet_btn:
                mainActivityPresenter.testSingle();
                // mainActivityPresenter.createWallet();
                break;
            case R.id.ma_wallet_address_btn:
                mainActivityPresenter.getAddress(walletAddressToSendEt.getText().toString());
                break;
            case R.id.ma_wallet_balance_btn:
                mainActivityPresenter.getBalance();
                break;
            case R.id.ma_send_dash_btn:
                mainActivityPresenter.sendDashToAddress(
                        walletAddressToSendEt.getText().toString(),
                        dashToSendEt.getText().toString(),
                        messageToSendEt.getText().toString());
                break;
            case R.id.ma_swap_initiate_btn:
                mainActivityPresenter.atomicswapInitiate(
                        walletAddressToSendEt.getText().toString(),
                        dashToSendEt.getText().toString(),
                        messageToSendEt.getText().toString());
                break;
            case R.id.ma_swap_participate_btn:
                mainActivityPresenter.atomicswapParticipate(
                        walletAddressToSendEt.getText().toString(),
                        dashToSendEt.getText().toString(),
                        messageToSendEt.getText().toString(),
                        payeeEt.getText().toString());
                break;
            case R.id.ma_swap_redeem_btn:
                mainActivityPresenter.atomicswapRedeem(
                        walletAddressToSendEt.getText().toString(),
                        messageToSendEt.getText().toString(),
                        txidEt.getText().toString(),
                        addressEt.getText().toString(), this.credentials.getNetworkParameters());
                break;
            case R.id.ma_swap_refund_btn:
                mainActivityPresenter.atomicswapRefund(
                        walletAddressToSendEt.getText().toString(),
                        messageToSendEt.getText().toString(),
                        txidEt.getText().toString(),
                        this.credentials.getNetworkParameters());
                break;
        }
    }

    public void onRecovery(View view) {
        Editable mnemonic = ((EditText) findViewById(R.id.ma_wallet_recovery_et)).getText();
        mainActivityPresenter.onRecovery(mnemonic.toString());
    }

    public void onDeleteAllPendingTxp(View view) {
        mainActivityPresenter.deleteAllPendingTxp();
    }

    public void onGetTxHistory(View view){
        mainActivityPresenter.getTxHistory(null, null, null, 1);
    }

    public void onGetPendingAtomicswap(View view){
        mainActivityPresenter.getPendingAtomicswap();
    }

    public void onGetMasternodeStatus(View view) {
        String txid = null;
        String address = null;
        String payee = null;
        if (!txidEt.getText().toString().equalsIgnoreCase("")){
            txid = txidEt.getText().toString();
        }
        if (!addressEt.getText().toString().equalsIgnoreCase("")){
            address = addressEt.getText().toString();
        }
        if (!payeeEt.getText().toString().equalsIgnoreCase("")){
            payee = payeeEt.getText().toString();
        }
        mainActivityPresenter.getMasternodeStatus("vcl", txid, address, payee);
    }

    public void onGetMasternodeCollateral(View view) {
        mainActivityPresenter.getMasternodeCollateral();


    }

    public void onGetMasternodePing(View view){
        String txid ;
        int vout;
        if (txidEt.getText().toString().equalsIgnoreCase("")){
            return;
        }
        txid = txidEt.getText().toString();
        if (addressEt.getText().toString().equalsIgnoreCase("")){
            return;
        }
        vout = Integer.parseInt(addressEt.getText().toString());
        mainActivityPresenter.getMasternodePing("vcl", txid, vout);
    }

    public void onGetMasternodes(View view){
        String txid = null;
        String address = null;
        String payee = null;
        if (!txidEt.getText().toString().equalsIgnoreCase("")){
            txid = txidEt.getText().toString();
        }
        if (!addressEt.getText().toString().equalsIgnoreCase("")){
            address = addressEt.getText().toString();
        }
        if (!payeeEt.getText().toString().equalsIgnoreCase("")){
            payee = payeeEt.getText().toString();
        }
        mainActivityPresenter.getMasternodes("vcl", txid, address, payee);
    }

    public void onRemoveMasternodes(View view){
        String txid=null;
        int vout = 0;
        if (!txidEt.getText().toString().equalsIgnoreCase("")){
            txid = txidEt.getText().toString();
            if (addressEt.getText().toString().equalsIgnoreCase("")){
                return;
            }
            vout = Integer.parseInt(addressEt.getText().toString());
        }
        mainActivityPresenter.removeMasternodes("vcl", txid, vout);
    }

    public void onActivateMasternode(View view) {
        String txid ;
        int vout;
        if (txidEt.getText().toString().equalsIgnoreCase("")){
            return;
        }
        txid = txidEt.getText().toString();
        if (addressEt.getText().toString().equalsIgnoreCase("")){
            return;
        }
        vout = Integer.parseInt(addressEt.getText().toString());

        // String signPrivKey = "fd1e37c2b9194d28a95b78414f4fef76621bc4641bd4d6fffa6ce580e112ae85";
        String masternodeKey = "5KGv1unWpomtQDBM8nSdsRctd3A38z9eDRp1mdHo7i3fLbjS3xS";
        String address = "161.189.98.179";
        int port = 9900;

        mainActivityPresenter.activateMasternode("vcl", txid, vout, masternodeKey, address, port, credentials);



        /*
        UnsafeByteArrayOutputStream outputStream = new UnsafeByteArrayOutputStream();
        UnsafeByteArrayOutputStream outputStream1 = new UnsafeByteArrayOutputStream();
        UnsafeByteArrayOutputStream outputStream2 = new UnsafeByteArrayOutputStream();

        String txid = "a60d49836e45058feb6d0acc3c087a991e5ea37c2b5a5c81f89ad61cac58c8a5";
        int vout = 0;
        String signPrivKey = "fd1e37c2b9194d28a95b78414f4fef76621bc4641bd4d6fffa6ce580e112ae85";
        String pingHash = "000000000cd81817d2ba200003e243b0797530370b93d40b4eee0b5445f1adfc";
        String privKey = "5KGv1unWpomtQDBM8nSdsRctd3A38z9eDRp1mdHo7i3fLbjS3xS";
        // String privKey = "c0289dc514e1c3ddc32ad4328d2c6fb980f9185ff6a8c025b6328b4fc24fc0e8";
        long curTime = 1620876547;

        byte[] abcd = Base58.decodeChecked(privKey);

        String address = "161.189.98.179";
        int port = 9900;

        try {
            serialize_input(txid, vout, outputStream);
            hash_decode(pingHash, outputStream);
            Date now = new Date();
            // Utils.uint64ToByteStreamLE(BigInteger.valueOf(curTime), outputStream);
            Utils.uint64ToByteStreamLE(BigInteger.valueOf(now.getTime()/1000), outputStream);
            outputStream.write(Utils.HEX.decode("01"));
            Utils.uint32ToByteStreamLE(CLIENT_SENTINEL_VERSION / 1000000, outputStream);
            Utils.uint32ToByteStreamLE(CLIENT_MASTERNODE_VERSION / 1000000, outputStream);
            String pingSig = this.signMessage(Utils.HEX.encode(outputStream.toByteArray()), privKey);
            // String msg = "a5c858ac1cd69af8815c5a2b7ca35e1e997a083ccc0a6deb8f05456e83490da6000000000ccd2e9293b377f398fd9eaec4f724202de6a278feedf5000263c103000000006dbd9c6000000000010100000001000000";
            // String pingSig = this.signMessage(msg, privKey);

            serialize_input(txid, vout, outputStream1);
            get_address(address, port, outputStream1);
            byte[] signPubKey = ECKey.fromPrivate(signPrivKey, true).getPubKey();
            outputStream1.write(Utils.HEX.decode(Integer.toHexString(signPubKey.length)));
            outputStream1.write(signPubKey);
            byte[] pubKey = ECKey.fromPrivate(privKey, true).getPubKey();
            outputStream1.write(Utils.HEX.decode(Integer.toHexString(pubKey.length)));
            outputStream1.write(pubKey);
            // Utils.uint64ToByteStreamLE(BigInteger.valueOf(curTime), outputStream1);
            Utils.uint64ToByteStreamLE(BigInteger.valueOf(now.getTime()/1000), outputStream1);
            Utils.uint32ToByteStreamLE(31800, outputStream1);
            String msgSig = this.signMessage(Utils.HEX.encode(outputStream1.toByteArray()), signPrivKey);

            outputStream2.write(Utils.HEX.decode("01"));
            serialize_input(txid, vout, outputStream2);
            get_address(address, port, outputStream2);
            outputStream2.write(Utils.HEX.decode(Integer.toHexString(signPubKey.length)));
            outputStream2.write(signPubKey);
            outputStream2.write(Utils.HEX.decode(Integer.toHexString(pubKey.length)));
            outputStream2.write(pubKey);
            outputStream2.write(Utils.HEX.decode(Integer.toHexString(msgSig.length()/2)));
            outputStream2.write(Utils.HEX.decode(msgSig));
            // Utils.uint64ToByteStreamLE(BigInteger.valueOf(curTime), outputStream2);
            Utils.uint64ToByteStreamLE(BigInteger.valueOf(now.getTime()/1000), outputStream2);
            Utils.uint32ToByteStreamLE(31800, outputStream2);
            serialize_input(txid, vout, outputStream2);
            hash_decode(pingHash, outputStream2);
            Utils.uint64ToByteStreamLE(BigInteger.valueOf(now.getTime()/1000), outputStream2);
            outputStream2.write(Utils.HEX.decode(Integer.toHexString(pingSig.length()/2)));
            outputStream2.write(Utils.HEX.decode(pingSig));
            outputStream2.write(Utils.HEX.decode("01"));
            Utils.uint32ToByteStreamLE(CLIENT_SENTINEL_VERSION , outputStream2);
            Utils.uint32ToByteStreamLE(CLIENT_MASTERNODE_VERSION , outputStream2);
            Utils.uint32ToByteStreamLE(0 , outputStream2);

            String aaa = Utils.HEX.encode(outputStream2.toByteArray());
            System.out.println(aaa);
        }catch(IOException e){
            System.out.println(e);
        }
        */
    }

    public static byte[] reverseArray(byte[] arr){
        for(int i=0;i<arr.length/2;i++){
            //两个数组元素互换
            byte temp = arr[i];
            arr[i] = arr[arr.length-i-1];
            arr[arr.length-i-1] = temp;
        }
        return arr;
    }

    public static void serialize_input(String txid, int vout, OutputStream outputStream) throws IOException {
        outputStream.write(reverseArray(Utils.HEX.decode(txid)));
        Utils.uint32ToByteStreamLE(vout, outputStream);
    }

    public static void hash_decode(String pingHash, OutputStream outputStream) throws  IOException {
       outputStream.write(reverseArray(Utils.HEX.decode(pingHash)));
    }

    public static void get_address(String address, int port, OutputStream outputStream) throws IOException {
        String[] strIp = address.split("\\.");
        byte[] btIp= new byte[4];
        for(int i=0;i<strIp.length;i++) {
            btIp[i] = (byte)Integer.parseInt(strIp[i]);
        }
        outputStream.write(Utils.HEX.decode("00000000000000000000ffff"));
        outputStream.write(btIp);
        Utils.uint16ToByteStreamBE(port, outputStream);
    }

    private String signMessage(String message, String privKey) {
        return ECKey.fromPrivate(privKey, true).signMessage1(message);
        /*return ECKey.fromPrivate(
                Utils.HEX.decode(privKey), false)
                .signMessage1(message);
         */
    }

}
