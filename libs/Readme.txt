创建多签：

        String coin = "vcl";
        String url = "http://69.234.192.199:3232/bws/api/";
	int n = 3;	// 签名总数
	int m = 2;	// 需要签名数
	int walletName = "mulitWallet";  // 钱包名字
	int copayerName1 = "copayer1";  // copayer名字
	int copayerName2 = "copayer2";  // copayer名字
	int copayerName3 = "copayer3";  // copayer名字
        try {

                Credentials credentials1 = createCredentials(null, coin);
                IBitcoreWalletServerAPI bitcoreWalletServerAPI1 = createBwsApi(url, credentials1);
                CreateWalletUseCase createWalletUseCase1 = new CreateWalletUseCase(
                        credentials1,
                        bitcoreWalletServerAPI1);

                JoinWalletInCreationUseCase joinWalletInCreationUseCase1 = new JoinWalletInCreationUseCase(
                        credentials1,
                        bitcoreWalletServerAPI1);

                String walletId = createWalletUseCase1.execute(m, n, walletName, false, coin);			//创建多签钱包
		// 钱包1加入
                IJoinWalletResponse joinWalletResponse1 = joinWalletInCreationUseCase1.execute(walletId, copayerName1, coin);  //自己加入钱包
		
		//生成加入多签钱包的secret
                String secret = CommUtils.BuildJoinWalletSecret(joinWalletResponse1.getWalletCore().getId(), credentials1.getWalletPrivateKey().getPrivateKeyAsHex(), "vcl", "livenet");

		
		//钱包2加入
                Credentials credentials2 = createCredentials(null, coin);
                IBitcoreWalletServerAPI bitcoreWalletServerAPI2 = createBwsApi(url, credentials2);
                JoinWalletInCreationUseCase joinWalletInCreationUseCase2 = new JoinWalletInCreationUseCase(
                        credentials2,
                        bitcoreWalletServerAPI2);
                IJoinWalletResponse joinWalletResponse2 = joinWalletInCreationUseCase2.execute(secret, copayerName2, true);

		// 钱包3加入
                Credentials credentials3 = createCredentials(null, coin);
                IBitcoreWalletServerAPI bitcoreWalletServerAPI3 = createBwsApi(url, credentials3);
                JoinWalletInCreationUseCase joinWalletInCreationUseCase3 = new JoinWalletInCreationUseCase(
                        credentials3,
                        bitcoreWalletServerAPI3);
                IJoinWalletResponse joinWalletResponse3 = joinWalletInCreationUseCase3.execute(secret, copayerName3, true);

                System.out.println(joinWalletResponse3.getWalletCore().getStatus());
       }catch (Exception e) {
            System.out.println(e);
        }



单签钱包小金额合并
	String coin = "vcl";
        String url = "http://69.234.192.199:3232/bws/api/";
	
	String word = "bone casual observe virus prepare system aunt bamboo horror police vault floor";
	Credentials credentials = createCredentials(word, coin);
        IBitcoreWalletServerAPI bitcoreWalletServerAPI = createBwsApi(url, credentials);
	IMergeBalanceUseCase mergeBalanceUseCase= new MergeBalanceUseCase(credentials, bitcoreWalletServerAPI);
	String minAmount = "500";    //小于这个值则合并
	String maxAmount = "10000";  //合并的金额如果达到这个值，则开始合并，否则不合并
        String txid = mergeBalanceUseCase.execute("500", "10000");  // txid为交易id



多签钱包小金额合并：（3/2多签）
	
	ICredentials credentials[] = new ICredentials[2];
	IBitcoreWalletServerAPI  bitcoreWalletServerAPI  = new IBitcoreWalletServerAPI[2];
 
	String coin = "vcl";
        String url = "http://69.234.192.199:3232/bws/api/";
	
	String word1 = "bone casual observe virus prepare system aunt bamboo horror police vault floor";
	credentials[0] = createCredentials(word1, coin);
        bitcoreWalletServerAPI[0] = createBwsApi(url, credentials[0]);

	String word2 = "bone casual observe virus prepare system aunt bamboo horror police vault floor";
	credentials[1] = createCredentials(word2, coin);
        bitcoreWalletServerAPI[1] = createBwsApi(url, credentials[1]);

	IMergeBalanceUseCase mergeBalanceUseCase= new MergeBalanceUseCase(credentials, bitcoreWalletServerAPI);
	String minAmount = "500";    //小于这个值则合并
	String maxAmount = "10000";  //合并的金额如果达到这个值，则开始合并，否则不合并
        String txid = mergeBalanceUseCase.execute("500", "10000");  // txid为交易id



多签钱包多个输出交易的创建、签名和广播
	String coin = "vcl";
        String url = "http://69.234.192.199:3232/bws/api/";

	String word1 = "bone casual observe virus prepare system aunt bamboo horror police vault floor";
	ICredentials credentials1 = createCredentials(word1, coin);
        IBitcoreWalletServerAPI bitcoreWalletServerAPI1 = createBwsApi(url, credentials1);

	String word2 = "bone casual observe virus prepare system aunt bamboo horror police vault floor";
	ICredentials credentials2 = createCredentials(word2, coin);
        IBitcoreWalletServerAPI bitcoreWalletServerAPI2 = createBwsApi(url, credentials[1]);

	IAddNewTxpUseCase addNewTxpUseCase = new AddNewTxpUseCase(
                                        credentials,
                                        bitcoreWalletServerAPI);
        IPublishTxpUseCase publishTxpUseCase = new PublishTxpUseCase(
                                        bitcoreWalletServerAPI,
                                        credentials);

        ISignTxpUseCase signTxpUseCase1 = new SignTxpUseCase(
                                        bitcoreWalletServerAPI1,
                                        credentials1);
        ISignTxpUseCase signTxpUseCase2 = new SignTxpUseCase(
                                        bitcoreWalletServerAPI2,
                                        credentials2);

        IBroadcastTxpUseCase broadcastTxpUseCase2 = new BroadcastTxpUseCase(
                                        credentials2,
                                        bitcoreWalletServerAPI2);

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

	String msg = "merge";
        ITransactionProposal proposal = addNewTxpUseCase.execute(outputs,  "", false, "send", msg, true);
    	ITransactionProposal publishedTxp = publishTxpUseCase.execute(proposal);	
     	ITransactionProposal signTxp1 = signTxpUseCase1.execute(publishedTxp);

     	ITransactionProposal signTxp2 = signTxpUseCase2.execute(publishedTxp);
       	ITransactionProposal broadcastTxp2 =  broadcastTxpUseCase.execute(signTxp2.getId());






