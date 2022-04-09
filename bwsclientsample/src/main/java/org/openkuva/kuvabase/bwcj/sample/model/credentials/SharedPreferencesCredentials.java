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

package org.openkuva.kuvabase.bwcj.sample.model.credentials;

import android.content.SharedPreferences;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.NetworkParameters;
import org.openkuva.kuvabase.bwcj.data.entity.gson.wallet.AddressInfo;
import org.openkuva.kuvabase.bwcj.data.entity.interfaces.credentials.ICredentials;
import org.openkuva.kuvabase.bwcj.data.repository.exception.NotFoundException;

import java.util.Arrays;
import java.util.List;

import static org.bitcoinj.core.NetworkParameters.fromID;

public class SharedPreferencesCredentials implements ICredentials {
    private final String WALLET_PRIVATE_KEY = "private_key";
    private final String WALLET_SEED = "wallet_seed";
    private final String NETWORK_PARAMS = "network_params";
    private final String SHARED_PRIVATE_KEY = "shared_private_key";
    private final String PERSONAL_PRIVATE_KEY = "personal_private_key";

    private final SharedPreferences sharedPreferences;

    public SharedPreferencesCredentials(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    private byte[] getWalletPrivateKeyBytes() {
        String stringArray = sharedPreferences.getString(WALLET_PRIVATE_KEY, null);
        byte[] privateKeyBytes = null;
        if (stringArray != null) {
            String[] split = stringArray.substring(1, stringArray.length() - 1).split(", ");
            privateKeyBytes = new byte[split.length];
            for (int i = 0; i < split.length; i++) {
                privateKeyBytes[i] = Byte.parseByte(split[i]);
            }
        }
        return privateKeyBytes;
    }

    @Override
    public List<String> getMnemonic(){
        return null;
    }

    @Override
    public ECKey getWalletPrivateKey() {
        byte[] walletPrivateKeyBytes = getWalletPrivateKeyBytes();

        return ECKey
                .fromPrivate(walletPrivateKeyBytes);
    }

    @Override
    public void setWalletPrivateKey(ECKey walletPrivateKey) {
        byte[] walletPrivateKeyBytes = walletPrivateKey.getPrivKeyBytes();
        sharedPreferences
                .edit()
                .putString(
                        WALLET_PRIVATE_KEY,
                        Arrays.toString(walletPrivateKeyBytes))
                .apply();

    }

    @Override
    public void setWalletPrivateKey(String walletPrivateKey) {
    }

    @Override
    public void setSeed(byte[] seedWords) {
        sharedPreferences.edit().putString(WALLET_SEED, Arrays.toString(seedWords)).apply();
    }

    @Override
    public byte[] getSeed() {
        return getBytes(sharedPreferences.getString(WALLET_SEED, ""));
    }

    @Override
    public NetworkParameters getNetworkParameters() {
        String network = sharedPreferences.getString(NETWORK_PARAMS, "");
        if (network.isEmpty()) {
            throw new NotFoundException("network does not set");
        }

        return fromID(network);
    }

    @Override
    public void setNetworkParameters(NetworkParameters network) {
        sharedPreferences.edit().putString(NETWORK_PARAMS, network.getId()).apply();
    }

    @Override
    public void setSharedEncryptingKey(String sharedEncryptingKey) {
        sharedPreferences.edit().putString(SHARED_PRIVATE_KEY, sharedEncryptingKey).apply();
    }


    @Override
    public void setPersonalEncryptingKey(String personalEncryptingKey) {
        sharedPreferences.edit().putString(PERSONAL_PRIVATE_KEY, personalEncryptingKey).apply();
    }

    @Override
    public String getSharedEncryptingKey() {
        return sharedPreferences.getString(SHARED_PRIVATE_KEY, null);
    }

    @Override
    public String getPersonalEncryptingKey() {
        return sharedPreferences.getString(PERSONAL_PRIVATE_KEY, null);
    }


    private byte[] getBytes(String stringArray) {
        if (stringArray == null) {
            throw new IllegalArgumentException("string can not be null");
        }
        String[] split = stringArray.substring(1, stringArray.length() - 1).split(", ");
        byte[] bytes = new byte[split.length];
        for (int i = 0; i < split.length; i++) {
            bytes[i] = Byte.parseByte(split[i]);
        }
        return bytes;
    }

    @Override
    public AddressInfo getPrivateByPath(String path){
        return null;
    }

    @Override
    public AddressInfo getPrivateByPath(String path, boolean segwitMode){
        return null;
    }
}
