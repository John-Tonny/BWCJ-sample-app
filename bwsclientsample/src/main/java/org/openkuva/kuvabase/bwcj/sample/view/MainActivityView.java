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

import android.content.Context;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivityView implements IMainActivityView {
    private final TextView walletId;
    private final TextView walletAddress;
    private final TextView walletBalance;
    private final TextView sendDashResult;
    private final TextView tvMnemonic;
    private final Context context;

    private final EditText contract;
    private final EditText secret;
    private final EditText secretHash;

    public MainActivityView(
            TextView walletId,
            TextView walletAddress,
            TextView walletBalance,
            TextView sendDashResult,
            TextView tvMnemonic,
            Context context,
            EditText contract,
            EditText secret,
            EditText secretHash) {

        this.walletId = walletId;
        this.walletAddress = walletAddress;
        this.walletBalance = walletBalance;
        this.sendDashResult = sendDashResult;
        this.context = context;
        this.tvMnemonic = tvMnemonic;

        this.contract = contract;
        this.secret = secret;
        this.secretHash = secretHash;
    }

    @Override
    public void updateWalletId(String id) {
        walletId.setText(id);
    }

    @Override
    public void updateWalletAddress(String address) {
        walletAddress.setText(address);
    }

    @Override
    public void updateWalletBalance(String balance) {
        walletBalance.setText(balance);
    }

    @Override
    public void updateSendDashResult(String result) {
        sendDashResult.setText(result);
    }

    @Override
    public void updateMnemonic(String result) {
        tvMnemonic.setText(result);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMnemonic(String words) {
        tvMnemonic.setText(words);
    }

    @Override
    public void updateContract(String msg) {
        contract.setText(msg);
    }

    @Override
    public void updateSecret(String msg) { secret.setText(msg);
    }

    @Override
    public void updateSecretHash(String msg) {
        secretHash.setText(msg);
    }


}
