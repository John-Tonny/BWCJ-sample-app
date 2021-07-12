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

import android.os.Handler;

public class InMainThreadMainActivityView implements IMainActivityView {
    private final IMainActivityView origin;
    private final Handler           handler;

    public InMainThreadMainActivityView(IMainActivityView origin, Handler handler) {
        this.origin = origin;
        this.handler = handler;
    }

    @Override
    public void updateWalletId(final String id) {
        handler.post(() -> origin.updateWalletId(id));
    }


    @Override
    public void updateWalletAddress(final String address) {
        handler.post(() -> origin.updateWalletAddress(address));
    }

    @Override
    public void updateWalletBalance(String balance) {
        handler.post(() -> origin.updateWalletBalance(balance));
    }

    @Override
    public void updateSendDashResult(String result) {
        handler.post(() -> origin.updateSendDashResult(result));
    }

    @Override
    public void showMessage(String message) {
        handler.post(() -> origin.showMessage(message));
    }

    @Override
    public void showMnemonic(String words) {
        handler.post(() -> origin.showMnemonic(words));
    }

    @Override
    public void updateContract(String msg) {
        handler.post(() -> origin.updateContract(msg));
    }

    @Override
    public void updateSecret(String msg) {
        handler.post(() -> origin.updateSecret(msg));
    }

    @Override
    public void updateSecretHash(String msg) {
        handler.post(() -> origin.updateSecretHash(msg));
    }

}
