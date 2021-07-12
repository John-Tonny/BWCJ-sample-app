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

import org.bitcoinj.core.NetworkParameters;
import org.openkuva.kuvabase.bwcj.data.entity.interfaces.credentials.ICredentials;
import org.openkuva.kuvabase.bwcj.data.entity.interfaces.masternode.IMasternode;
import org.openkuva.kuvabase.bwcj.data.entity.interfaces.masternode.IMasternodeBroadcast;
import org.openkuva.kuvabase.bwcj.data.entity.interfaces.masternode.IMasternodeCollateral;
import org.openkuva.kuvabase.bwcj.data.entity.interfaces.masternode.IMasternodePing;
import org.openkuva.kuvabase.bwcj.data.entity.interfaces.masternode.IMasternodeRemove;
import org.openkuva.kuvabase.bwcj.data.entity.interfaces.masternode.IMasternodeStatus;
import org.openkuva.kuvabase.bwcj.data.entity.interfaces.transaction.ITransactionHistory;

import java.util.concurrent.ExecutorService;

public class AsyncMainActivityPresenter implements IMainActivityPresenter {
    private final IMainActivityPresenter origin;
    private final ExecutorService service;

    public AsyncMainActivityPresenter(
            IMainActivityPresenter origin,
            ExecutorService service) {

        this.origin = origin;
        this.service = service;
    }


    @Override
    public void createWallet() {
        service.execute(origin::createWallet);
    }

    @Override
    public void getAddress() {
        service.execute(origin::getAddress);
    }

    @Override
    public void getBalance() {
        service.execute(origin::getBalance);
    }

    @Override
    public void sendDashToAddress(String address, String dash, String msg) {
        service.execute(() -> origin.sendDashToAddress(address, dash, msg));
    }

    @Override
    public void onRecovery(String mnemonic) {
        service.execute(() -> origin.onRecovery(mnemonic));
    }

    @Override
    public void deleteAllPendingTxp() {
        service.execute(origin::deleteAllPendingTxp);
    }

    @Override
    public void getTxHistory(Integer skip, Integer limit, String tokenAddress, Integer includeExtendedInfo) {
        service.execute(() -> origin.getTxHistory(skip, limit, tokenAddress, includeExtendedInfo));
    }

    @Override
    public void getMasternodeStatus(String coin, String txid, String address, String payee) {
        service.execute(() -> origin.getMasternodeStatus(coin, txid, address, payee ));
    }

    @Override
    public void getMasternodeCollateral() {
        service.execute(() -> origin.getMasternodeCollateral());
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
        service.execute(() -> origin.signMasternode(coin, txid, vout, signPrivKey, pingHeight, pingHash, privKey, address, port));
    }

    @Override
    public void broadcastMasternode(String coin, String rawTx) {
        service.execute(() -> origin.broadcastMasternode(coin, rawTx));
    }

    @Override
    public void getMasternodes(String coin, String txid, String address, String payee) {
        service.execute(() -> origin.getMasternodes(coin, txid, address, payee));
    }

    @Override
    public void removeMasternodes(String coin, String txid, int vout) {
        service.execute(() -> origin.removeMasternodes(coin, txid, vout));
    }

    @Override
    public void getMasternodePing(String coin, String txid, int vout) {
        service.execute(() ->  origin.getMasternodePing(coin, txid, vout));
    }

    @Override
    public void activateMasternode(String coin, String txid, int vout, String masternodeKey, String address, int port, ICredentials credentials) {
        service.execute(() ->  origin.activateMasternode(coin, txid, vout, masternodeKey, address, port, credentials));
    }

    @Override
    public void atomicswapInitiate(String address, String dash, String msg) {
        service.execute(() ->  origin.atomicswapInitiate(address, dash, msg));
    }

    @Override
    public void atomicswapParticipate(String address, String dash, String msg, String secretHash) {
        service.execute(() ->  origin.atomicswapParticipate(address, dash, msg, secretHash));
    }

    @Override
    public void atomicswapRedeem(String address, String msg, String contract, String secret, NetworkParameters parameters) {
        service.execute(() ->  origin.atomicswapRedeem(address, msg, contract, secret, parameters));
    }

    @Override
    public void atomicswapRefund(String address, String msg, String contract, NetworkParameters parameters) {
        service.execute(() ->  origin.atomicswapRefund(address, msg, contract, parameters));
    }

    @Override
    public void getPendingAtomicswap() {
        service.execute(() -> origin.getPendingAtomicswap());
    }

    @Override
    public void testSingle() {service.execute(() -> origin.testSingle());}
 }
