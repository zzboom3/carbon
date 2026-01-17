package com.carbon.chainmaker.config;

import com.google.common.io.Resources;
import org.apache.commons.io.IOUtils;
import org.chainmaker.sdk.*;
import org.chainmaker.sdk.config.NodeConfig;
import org.chainmaker.sdk.config.SdkConfig;
import org.chainmaker.sdk.utils.UtilsException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class ChainSDKConfig {

    private static final String OPENSSL_PROVIDER = "openSSL";
    private static final String TLS_NEGOTIATION = "TLS";

    @Value("${carbon.chainmaker.sdkConfigPath:}")
    private String sdkConfigPath;

    @Value("${carbon.chainmaker.cryptoBaseDir:}")
    private String cryptoBaseDir;

    @Bean
    public ChainClient chainClient() throws IOException, SdkException {

        SdkConfig sdkConfig = getSdkConfig();

        List<Node> nodeList = new ArrayList<>();
        for (NodeConfig nodeConfig : sdkConfig.getChainClient().getNodes()) {
            List<byte[]> tlsCaCertList = new ArrayList<>();
            for (String rootPath : nodeConfig.getTrustRootPaths()){
                tlsCaCertList.add(this.getResourceFileBytes(rootPath));
            }

            byte[][] tlsCaCerts = new byte[tlsCaCertList.size()][];
            tlsCaCertList.toArray(tlsCaCerts);

            String url = "";

            if (nodeConfig.isEnableTls()){
                url = "grpcs://" + nodeConfig.getNodeAddr();
            }else {
                url = "grpc://" + nodeConfig.getNodeAddr();
            }
            Node node = new Node();
            node.setClientKeyBytes(this.getResourceFileBytes(sdkConfig.getChainClient().getUserKeyFilePath()));
            node.setClientCertBytes(this.getResourceFileBytes(sdkConfig.getChainClient().getUserCrtFilePath()));
            node.setTlsCertBytes(tlsCaCerts);
            node.setHostname(nodeConfig.getTlsHostName());
            node.setGrpcUrl(url);
            node.setSslProvider(OPENSSL_PROVIDER);
            node.setNegotiationType(TLS_NEGOTIATION);
            node.setConnectCount(nodeConfig.getConnCnt());
            node.setMessageSize(sdkConfig.getChainClient().getRpcClient().getMaxReceiveMessageSize());
            nodeList.add(node);
        }

        Node[] nodes = new Node[nodeList.size()];
        nodeList.toArray(nodes);

        ChainManager chainManager = ChainManager.getInstance();
        ChainClient chainClient = chainManager.getChainClient(sdkConfig.getChainClient().getChain_id());
        User clientUser = new User(sdkConfig.getChainClient().getOrgId(), this.getResourceFileBytes(sdkConfig.getChainClient().getUserKeyFilePath()),
                this.getResourceFileBytes(sdkConfig.getChainClient().getUserCrtFilePath()));
        if (chainClient == null) {
            chainClient = chainManager.createChainClient(sdkConfig.getChainClient().getChain_id(), clientUser, nodes);
        }
        return chainClient;
    }

    private SdkConfig getSdkConfig() throws IOException{
        Yaml yaml = new Yaml();
        InputStream in;
        if (sdkConfigPath != null && !sdkConfigPath.trim().isEmpty()) {
            in = new FileInputStream(sdkConfigPath.trim());
        } else {
            in = this.getClass().getClassLoader().getResourceAsStream("sdk_config.yml");
        }
        if (in == null) {
            throw new IOException("sdk_config.yml not found");
        }
        SdkConfig sdkConfig;
        try{
            sdkConfig = yaml.loadAs(in, SdkConfig.class);
        } finally {
            in.close();
        }
        return sdkConfig;
    }


    public byte[] getResourceFileBytes(String resourcePath) throws UtilsException {
        try {
            if (cryptoBaseDir != null && !cryptoBaseDir.trim().isEmpty()) {
                Path p = Paths.get(cryptoBaseDir.trim(), resourcePath);
                return Files.readAllBytes(p);
            }
            return IOUtils.toByteArray((InputStream) Resources.getResource(resourcePath).getContent());
        } catch (IOException var2) {
            throw new UtilsException("get file by path err : " + var2.getMessage());
        }
    }

}
