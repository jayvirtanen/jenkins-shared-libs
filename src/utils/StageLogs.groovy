package utils
import java.util.regex.Matcher
import java.util.regex.Pattern

import org.jenkinsci.plugins.workflow.actions.LabelAction
import org.jenkinsci.plugins.workflow.actions.LogAction
import org.jenkinsci.plugins.workflow.graph.FlowNode
import org.jenkinsci.plugins.workflow.graphanalysis.DepthFirstScanner
import org.jenkinsci.plugins.workflow.cps.nodes.StepStartNode
import org.jenkinsci.plugins.workflow.job.WorkflowRun
import java.util.stream.Collectors

class StageLogs implements Serializable {

    StageLogs(){
        println("init")
    }
    // Recursively check flowNode parents until we find a stage
    @NonCPS
    static String getFlowNodeStage(FlowNode flowNode) {
        for (FlowNode parent : flowNode.getParents()) {
            if (parent instanceof StepStartNode && isNamedStageStartNode(parent)) {
                return parent.getAction(LabelAction.class).getDisplayName()
            } else {
                return getFlowNodeStage(parent)
            }
        }
        // Return null if no stage found. Null will be passed through all recursion levels
        return null
    }

    // Collect logs of each flow node that belongs to stage
    @NonCPS
    static List<String> collectLogsForStage(WorkflowRun run, String stageName) {
        run.save()
        List<String> logs = []
        DepthFirstScanner scanner = new DepthFirstScanner()

        scanner.setup(run.getExecution().getCurrentHeads())

        for (FlowNode flowNode : scanner) {
            // Skip flow nodes that are not part of a requested stage
            // If stage not found for the current flow node, getFlowNodeStage() will return null
            if(stageName.equals(getFlowNodeStage(flowNode))) {
                LogAction logAction = flowNode.getAction(LogAction.class)
                if (logAction != null) {
                    def reader = new BufferedReader(logAction.getLogText().readAll())
                    List<String> flowNodeLogs = reader.lines().collect(Collectors.toList())
                    logs.addAll(0, flowNodeLogs)
                }
            }
        }
        return logs
    }

    @NonCPS
    static private boolean isNamedStageStartNode(FlowNode node) {
        return Objects.equals(((StepStartNode) node).getStepName(), "Stage") && !Objects.equals(node.getDisplayFunctionName(), "stage");
    }
}
