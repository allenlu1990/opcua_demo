package com.scc.runner.task;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.nodes.Node;
import org.eclipse.milo.opcua.sdk.client.api.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.client.model.nodes.objects.ServerTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.nodes.variables.ServerStatusTypeNode;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.BuildInfo;
import org.eclipse.milo.opcua.stack.core.types.structured.ServerStatusDataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

public class ReadNodeExample implements ClientExample {
    private  final Logger logger = LoggerFactory.getLogger(getClass());

    public static void main(String[] args) throws Exception {

         final CompletableFuture<OpcUaClient> future = new CompletableFuture<>();
        ReadNodeExample example = new ReadNodeExample();

        OpcUaClient client = OpcUaClient.create(
            example.getEndpointUrl(),
            endpoints ->
                endpoints.stream()
                    .filter(example.endpointFilter())
                    .findFirst(),
            configBuilder ->
                configBuilder
//                    .setApplicationName(LocalizedText.english("eclipse milo opc-ua client"))
//                    .setApplicationUri("urn:eclipse:milo:examples:client")
                    .setIdentityProvider(example.getIdentityProvider())
                    .setRequestTimeout(uint(5000))
                    .build()
        );
        example.run(client, future);
        future.get(15, TimeUnit.SECONDS);

    }


    @Override
    public void run(OpcUaClient client, CompletableFuture<OpcUaClient> future) throws Exception {
        // synchronous connect
        client.connect().get();

        // Get a typed reference to the Server object: ServerNode
        ServerTypeNode serverNode = client.getAddressSpace().getObjectNode(
            Identifiers.Server,
            ServerTypeNode.class
        ).get();


        // Read properties of the Server object...
        String[] serverArray = serverNode.getServerArray().get();
        String[] namespaceArray = serverNode.getNamespaceArray().get();

        logger.info("ServerArray={}", Arrays.toString(serverArray));
        logger.info("NamespaceArray={}", Arrays.toString(namespaceArray));

        // Read the value of attribute the ServerStatus variable component
        ServerStatusDataType serverStatus = serverNode.getServerStatus().get();

        logger.info("ServerStatus={}", serverStatus);

        // Get a typed reference to the ServerStatus variable
        // component and read value attributes individually
        ServerStatusTypeNode serverStatusNode = serverNode.getServerStatusNode().get();
        BuildInfo buildInfo = serverStatusNode.getBuildInfo().get();
        DateTime startTime = serverStatusNode.getStartTime().get();
        DateTime currentTime = serverStatusNode.getCurrentTime().get();
        ServerState state = serverStatusNode.getState().get();

        logger.info("ServerStatus.BuildInfo={}", buildInfo);
        logger.info("ServerStatus.StartTime={}", startTime);
        logger.info("ServerStatus.CurrentTime={}", currentTime);
        logger.info("ServerStatus.State={}", state);


        VariableNode node = client.getAddressSpace().createVariableNode(
            new NodeId(0, "85")
        );

        browseNode("", client, Identifiers.RootFolder);
        // Read the current value
        DataValue value = node.readValue().get();
        logger.info("Value={}", value);

        future.complete(client);

    }
    private void browseNode(String indent, OpcUaClient client, NodeId browseRoot) {
        try {
            List<Node> nodes = client.getAddressSpace().browse(browseRoot).get();

            for (Node node : nodes) {
                logger.info("{} Node={}", indent, node.getBrowseName().get().getName());

                // recursively browse to children
                browseNode(indent + "  ", client, node.getNodeId().get());
            }
        } catch (InterruptedException | ExecutionException e) {
            logger.error("Browsing nodeId={} failed: {}", browseRoot, e.getMessage(), e);
        }
    }

}