package com.scc.runner.task;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfig;
import org.eclipse.milo.opcua.sdk.client.api.identity.UsernameProvider;
import org.eclipse.milo.opcua.sdk.client.api.nodes.Node;
import org.eclipse.milo.opcua.stack.client.UaStackClient;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.springframework.stereotype.Component;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

@Component
public class Task {
  private static String  EndPointUrl = "opc.tcp://milo.digitalpetri.com:62541/milo";

  public void run(){
    SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
    Date date = new Date(System.currentTimeMillis());
    System.out.println(formatter.format(date)+" Task 定时任务！");

    try {

//      UaStackClient.create()
//      EndpointDescription[] endpointDescription = UaStackClient.getEndpoints(EndPointUrl).get();
//      EndpointDescription endpoint = Arrays.stream(endpointDescription)
//          .filter(e -> e.getSecurityPolicyUri().equals(SecurityPolicy.None.getUri()))
//          .findFirst().orElseThrow(() -> new Exception("no desired endpoints returned"));
//
//      OpcUaClientConfig config = OpcUaClientConfig.builder()
//          .setApplicationName(LocalizedText.english("OPCAPP"))
//          .setApplicationUri("urn:LAPTOP-AQ90KJVR:OPCAPP")
////          .setIdentityProvider(new UsernameProvider("user1","password"))
//          .setEndpoint(endpoint)
//          .setRequestTimeout(uint(5000))
//          .build();
//
//
//
//      OpcUaClient.create()
//
//      OpcUaClient opcClient = new OpcUaClient(config);
//      browseNode(opcClient);
//      opcClient.disconnect();



    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public void browseNode(OpcUaClient client) throws Exception{
    //开启连接
    client.connect().get();
    List<Node> nodes = client.getAddressSpace().browse(Identifiers.RootFolder).get();
    for(Node node:nodes){
      System.out.println("Node= " + node.getBrowseName().get().getName());
    }
  }

}
