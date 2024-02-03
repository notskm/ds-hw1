package csx55.overlay.node;

import csx55.overlay.wireformats.*;

public class Node {
    public void onEvent(Event event) {
        Protocol eventType = Protocol.values()[event.getType()];

        switch (eventType) {
            case REGISTER_REQUEST:
                onRegisterRequest((Register) event);
                break;
            case REGISTER_RESPONSE:
                onRegisterResponse((RegisterResponse) event);
                break;
            case DEREGISTER_REQUEST:
                onDeregisterRequest((Deregister) event);
                break;
            case MESSAGING_NODES_LIST:
                onMessagingNodesList((MessagingNodesList) event);
                break;
            case LINK_WEIGHTS:
                onLinkWeights((LinkWeights) event);
                break;
            case TASK_INITIATE:
                onTaskInitiate((TaskInitiate) event);
                break;
            case TASK_COMPLETE:
                onTaskComplete((TaskComplete) event);
                break;
            case FULL_TRAFFIC_SUMMARY:
                onTaskSummaryRequest((TaskSummaryRequest) event);
                break;
            case TRAFFIC_SUMMARY:
                onTaskSummaryResponse((TaskSummaryResponse) event);
                break;
        }
    }

    protected void onRegisterRequest(Register event) {
    }

    protected void onRegisterResponse(RegisterResponse event) {
    }

    protected void onDeregisterRequest(Deregister event) {

    }

    protected void onMessagingNodesList(MessagingNodesList event) {

    }

    protected void onLinkWeights(LinkWeights event) {

    }

    protected void onTaskInitiate(TaskInitiate event) {

    }

    protected void onTaskComplete(TaskComplete event) {

    }

    protected void onTaskSummaryRequest(TaskSummaryRequest event) {

    }

    protected void onTaskSummaryResponse(TaskSummaryResponse event) {

    }
}
